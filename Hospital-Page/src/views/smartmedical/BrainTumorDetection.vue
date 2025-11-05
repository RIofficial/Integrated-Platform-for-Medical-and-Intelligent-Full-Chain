<template>
  <div class="brain-tumor-detection-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>脑肿瘤检测</span>
        </div>
      </template>
      <div class="content-area">
        <el-upload
          class="upload-demo"
          drag
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          :show-file-list="false"
          accept="image/*"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将图片拖拽到此处或 <em>点击上传</em>
          </div>
        </el-upload>

        <div v-if="imageUrl" class="image-preview">
          <h3>预览图片:</h3>
          <img :src="imageUrl" alt="Uploaded Image" class="uploaded-image" />
        </div>

        <el-button type="primary" :loading="loading" @click="detectTumor" :disabled="!selectedFile">
          {{ loading ? '检测中...' : '开始检测' }}
        </el-button>

        <div v-if="detectionResult" class="detection-result">
          <h3>检测结果:</h3>
          <img v-if="detectionResult.startsWith('data:image')" :src="detectionResult" alt="Detection Result" class="detection-image" />
          <p v-else>{{ detectionResult }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { UploadFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

const selectedFile = ref(null);
const imageUrl = ref('');
const fileList = ref([]);
const loading = ref(false);
const detectionResult = ref('');

const handleFileChange = (file) => {
  selectedFile.value = file.raw;
  imageUrl.value = URL.createObjectURL(file.raw);
  fileList.value = [file];
  detectionResult.value = ''; t
};

const detectTumor = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先上传图片！');
    return;
  }

  loading.value = true;
  detectionResult.value = '';

  const formData = new FormData();
  formData.append('file', selectedFile.value);

  try {
    const response = await axios.post('http://localhost:8081/braintumor/detect', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    if (response.data.status === 200) {
      const pythonResponse = JSON.parse(response.data.data);
      
      if (pythonResponse.status === 200 && pythonResponse.data) {
        detectionResult.value = 'data:image/png;base64,' + pythonResponse.data;
        ElMessage.success('检测成功！');
      } else {
        detectionResult.value = '检测失败: ' + (pythonResponse.errorMsg || 'Python 服务返回错误');
        ElMessage.error('检测失败！');
      }
    } else {
      detectionResult.value = '检测失败: ' + (response.data.errorMsg || '未知错误');
      ElMessage.error('检测失败！');
    }

  } catch (error) {
    console.error('脑肿瘤检测失败:', error);
    detectionResult.value = '检测过程中发生错误。';
    ElMessage.error('检测过程中发生错误，请稍后再试或检查后端服务。');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.brain-tumor-detection-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.box-card {
  width: 80%;
  max-width: 800px;
  height: 90%;
  display: flex;
  flex-direction: column;
}

.box-card ::v-deep .el-card__body {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0;
  min-height: 0;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  padding: 15px 20px;
  flex-shrink: 0;
}

.content-area {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.upload-demo {
  width: 100%;
  margin-bottom: 20px;
}

.el-upload-dragger {
  width: 100% !important;
  height: 180px !important;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.image-preview {
  margin-top: 20px;
  text-align: center;
  max-width: 100%;
}

.uploaded-image {
  max-width: 100%;
  max-height: 300px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  margin-top: 10px;
}

.el-button {
  margin-top: 20px;
}

.detection-result {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #ffffff;
  width: 100%;
  max-width: 600px;
  text-align: center;
}

.detection-image {
  max-width: 100%;
  max-height: 400px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  margin-top: 10px;
}
</style>
