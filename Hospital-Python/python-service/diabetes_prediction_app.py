from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import pandas as pd
import os

app = Flask(__name__)
CORS(app)

current_dir = os.path.dirname(os.path.abspath(__file__))

class DiabetesPredictor:
    def __init__(self):
        model_path = os.path.join(current_dir, 'diabetes_model.pkl')
        quantiles_path = os.path.join(current_dir, 'feature_quantiles.pkl')
        scaler_path = os.path.join(current_dir, 'standard_scaler.pkl')

        try:
            self.model = joblib.load(model_path)
            self.quantiles = joblib.load(quantiles_path)
            self.scaler = joblib.load(scaler_path)
            print("模型和预处理参数加载成功！")
        except FileNotFoundError as e:
            print(f"错误：模型文件未找到。请确保 {model_path}, {quantiles_path}, {scaler_path} 存在。错误信息: {e}")
            raise e
        except Exception as e:
            print(f"加载模型或预处理参数时发生错误: {e}")
            raise e

    def preprocess(self, input_data):
        df = pd.DataFrame([input_data])

        medical_features = ['Glucose', 'BloodPressure', 'SkinThickness', 'Insulin', 'BMI']
        for col in medical_features:
            if df[col].iloc[0] == 0:
                df[col] = self.quantiles[col]

        df['Glucose_BMI_Ratio'] = df['Glucose'] / df['BMI']
        df['BP_Age_Ratio'] = df['BloodPressure'] * df['Age']
        df['Insulin_Glucose_Product'] = df['Insulin'] * df['Glucose']

        scaled_features = self.scaler.transform(df[medical_features])
        df[medical_features] = scaled_features

        return df

predictor = DiabetesPredictor()

@app.route('/predict_diabetes', methods=['POST'])
def predict_diabetes():
    try:
        data = request.json
        if not data:
            return jsonify({"error": "请求体为空或不是有效的 JSON"}), 400

        required_fields = ['Pregnancies', 'Glucose', 'BloodPressure', 'SkinThickness', 'Insulin', 'BMI', 'DiabetesPedigreeFunction', 'Age']
        for field in required_fields:
            if field not in data:
                return jsonify({"error": f"缺少必需字段: {field}"}), 400
            try:
                data[field] = float(data[field])
            except ValueError:
                return jsonify({"error": f"字段 '{field}' 的值无效，必须是数字"}), 400

        processed_data = predictor.preprocess(data)
        prediction = predictor.model.predict(processed_data)[0]

        result = "高风险（建议就医检查）" if prediction == 1 else "低风险（保持健康）"
        return jsonify({"prediction": result, "status": 200})

    except Exception as e:
        print(f"预测发生错误: {e}")
        return jsonify({"error": "预测失败，请检查输入数据和服务器日志。", "details": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
