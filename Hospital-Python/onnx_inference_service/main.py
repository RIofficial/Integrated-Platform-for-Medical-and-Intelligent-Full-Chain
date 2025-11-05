import os
import io
import numpy as np
from flask import Flask, request, jsonify
from PIL import Image, ImageDraw, ImageFont
import onnxruntime as ort
from flask_cors import CORS
import base64

app = Flask(__name__)
CORS(app)

MODEL_PATH = 'best.onnx'

try:
    providers = ['CUDAExecutionProvider', 'CPUExecutionProvider']
    session = ort.InferenceSession(MODEL_PATH, providers=providers)

    input_name = session.get_inputs()[0].name
    output_name = session.get_outputs()[0].name

    input_shape = session.get_inputs()[0].shape
    target_height = input_shape[2]
    target_width = input_shape[3]

    print(f"ONNX 模型 '{MODEL_PATH}' 加载成功！")
    print(f"模型输入名称: {input_name}")
    print(f"模型输入形状: {input_shape}")
    print(f"模型输出名称: {output_name}")

except Exception as e:
    session = None
    input_name = None
    output_name = None
    target_height = None
    target_width = None
    print(f"加载 ONNX 模型 '{MODEL_PATH}' 失败: {e}")
    print("请确保模型文件存在且 ONNX Runtime 已正确安装。")


def preprocess_image(image: Image.Image, target_width, target_height) -> np.ndarray:
    if target_width is None or target_height is None:
        raise ValueError("Target width and height must be specified for preprocessing.")

    image = image.resize((target_width, target_height))

    if image.mode != 'RGB':
        image = image.convert('RGB')

    img_array = np.array(image).astype(np.float32)

    img_array = img_array / 255.0

    img_array = np.transpose(img_array, (2, 0, 1))
    img_array = np.expand_dims(img_array, axis=0)

    return img_array


def get_scalar_value(value):
    if isinstance(value, np.ndarray) and value.size == 1:
        return value.item()
    return value

def draw_boxes_on_image(image: Image.Image, predictions: np.ndarray, original_width, original_height) -> str:
    draw = ImageDraw.Draw(image)
    try:
        font = ImageFont.truetype("arial.ttf", 15)
    except IOError:
        font = ImageFont.load_default()
        print("警告: 无法加载 arial.ttf 字体，使用默认字体。")

    print(
        f"在 draw_boxes_on_image 中接收到的 predictions 形状: {predictions.shape if predictions is not None else 'None'}")
    print(
        f"在 draw_boxes_on_image 中接收到的 predictions 内容 (前5行): {predictions[:5] if predictions is not None and predictions.ndim >= 2 else predictions}")

    if predictions is not None and predictions.ndim >= 3 and predictions.shape[2] >= 5:
        for batch_item_predictions in predictions:
            for i, pred in enumerate(batch_item_predictions):
                print(f"正在处理第 {i} 个预测结果: {pred}")

                x1 = get_scalar_value(pred[0])
                y1 = get_scalar_value(pred[1])
                x2 = get_scalar_value(pred[2])
                y2 = get_scalar_value(pred[3])
                confidence = get_scalar_value(pred[4])
                class_id = int(get_scalar_value(pred[5])) if len(pred) > 5 else 0

                x1_orig = int(x1 / target_width * original_width)
                y1_orig = int(y1 / target_height * original_height)
                x2_orig = int(x2 / target_width * original_width)
                y2_orig = int(y2 / target_height * original_height)

                if confidence > 0.5:
                    color = "red"
                    label = f"肿瘤: {confidence:.2f}"
                    draw.rectangle([x1_orig, y1_orig, x2_orig, y2_orig], outline=color, width=3)
                    draw.text((x1_orig, y1_orig - 18), label, fill=color, font=font)
                    print(f"绘制检测框: {label} @ ({x1_orig},{y1_orig})-({x2_orig},{y2_orig})")
    else:
        print("警告: predictions 格式不符合预期 (期望形状为 (batch, num_detections, values))，未能绘制检测框。")

    buffered = io.BytesIO()
    image.save(buffered, format="PNG")
    img_str = base64.b64encode(buffered.getvalue()).decode("utf-8")
    return img_str


@app.route('/predict', methods=['POST'])
def predict():
    if session is None:
        return jsonify({"status": 500, "errorMsg": "ONNX 模型未加载成功，无法进行推理。"}), 500

    if 'file' not in request.files:
        return jsonify({"status": 400, "errorMsg": "没有找到图片文件"}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({"status": 400, "errorMsg": "未选择文件"}), 400

    try:
        img_bytes = file.read()
        image = Image.open(io.BytesIO(img_bytes))

        original_width, original_height = image.size

        preprocessed_image = preprocess_image(image, target_width, target_height)

        ort_inputs = {input_name: preprocessed_image}
        ort_outs = session.run([output_name], ort_inputs)

        predictions = ort_outs[0]

        print(f"ONNX 模型原始输出 (ort_outs[0]) 形状: {predictions.shape}")
        print(
            f"ONNX 模型原始输出 (ort_outs[0]) 内容 (前5行): {predictions[:5] if predictions.ndim >= 2 else predictions}")

        original_pil_image = Image.open(io.BytesIO(img_bytes)).convert("RGB")

        drawn_image_base64 = draw_boxes_on_image(original_pil_image, predictions, original_width, original_height)

        return jsonify({"status": 200, "data": drawn_image_base64})

    except Exception as e:
        print(f"推理过程中发生错误: {e}")
        return jsonify({"status": 500, "errorMsg": f"推理过程中发生错误: {e}"}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)
