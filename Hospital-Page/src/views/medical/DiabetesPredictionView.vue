<template>
  <div class="diabetes-prediction-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
      <el-breadcrumb-item>糖尿病风险预测</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>糖尿病风险预测</span>
          <el-button class="button" text @click="resetForm">重置</el-button>
        </div>
      </template>

      <el-form :model="form" label-width="180px" label-position="left" class="diabetes-form">
        <el-form-item label="怀孕次数 (Pregnancies)">
          <el-input-number v-model="form.Pregnancies" :min="0" :max="20" controls-position="right" />
        </el-form-item>
        <el-form-item label="血糖值 (Glucose mg/dL)">
          <el-input-number v-model="form.Glucose" :min="0" :max="200" controls-position="right" />
        </el-form-item>
        <el-form-item label="血压 (BloodPressure mmHg)">
          <el-input-number v-model="form.BloodPressure" :min="0" :max="120" controls-position="right" />
        </el-form-item>
        <el-form-item label="皮脂厚度 (SkinThickness mm)">
          <el-input-number v-model="form.SkinThickness" :min="0" :max="50" controls-position="right" />
        </el-form-item>
        <el-form-item label="胰岛素 (Insulin μU/mL)">
          <el-input-number v-model="form.Insulin" :min="0" :max="300" controls-position="right" />
        </el-form-item>
        <el-form-item label="体重指数 (BMI)">
          <el-input-number v-model="form.BMI" :min="0" :max="50" :precision="2" :step="0.1" controls-position="right" />
        </el-form-item>
        <el-form-item label="糖尿病遗传函数 (DiabetesPedigreeFunction)">
          <el-input-number v-model="form.DiabetesPedigreeFunction" :min="0" :max="2.5" :precision="3" :step="0.001" controls-position="right" />
        </el-form-item>
        <el-form-item label="年龄 (Age)">
          <el-input-number v-model="form.Age" :min="0" :max="100" controls-position="right" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="predictDiabetes" :loading="loading">开始预测</el-button>
        </el-form-item>
      </el-form>

      <el-divider />

      <div v-if="predictionResult" class="prediction-result">
        <h3>预测结果：</h3>
        <p :style="{ color: predictionColor }">{{ predictionResult }}</p>
      </div>
      <el-empty v-else description="请填写上方数据进行预测" />

    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const initialForm = {
  Pregnancies: 0,
  Glucose: 0,
  BloodPressure: 0,
  SkinThickness: 0,
  Insulin: 0,
  BMI: 0.0,
  DiabetesPedigreeFunction: 0.0,
  Age: 0
};

const form = reactive({ ...initialForm });
const predictionResult = ref('');
const predictionColor = ref('#333');
const loading = ref(false);

const resetForm = () => {
  Object.assign(form, initialForm);
  predictionResult.value = '';
  predictionColor.value = '#333';
  ElMessage.info('表单已重置。');
};

const predictDiabetes = async () => {
  loading.value = true;
  predictionResult.value = '';
  predictionColor.value = '#333';

  try {
    const response = await axios.post('http://localhost:5000/predict_diabetes', form);
    if (response.data.status === 200) {
      predictionResult.value = response.data.prediction;
      predictionColor.value = response.data.prediction.includes('高风险') ? 'red' : 'green';
      ElMessage.success('预测成功！');
    } else {
      ElMessage.error('预测失败: ' + (response.data.error || '未知错误'));
    }
  } catch (error) {
    console.error('Error during diabetes prediction:', error);
    ElMessage.error('预测请求失败，请检查Python服务是否运行在 http://localhost:5000。');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.diabetes-prediction-container {
  padding: 20px;
  min-height: calc(100vh - 80px);
  overflow-y: auto;
}

.el-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 20px;
}

.diabetes-form {
  background: #ffffff;
  padding: 0;
  border-radius: 8px;
  box-shadow: none;
  display: block;
  overflow: visible;
}

.diabetes-form .el-form-item {
  margin-bottom: 25px;
}

.diabetes-form .el-input-number {
  width: 100%;
  text-align: left;
}

.el-form-item__content {
  display: flex;
  align-items: center;
}

.diabetes-form .el-form-item:last-of-type {
  margin-top: 30px;
  margin-bottom: 0;
}

.el-button {
  width: 150px;
}

.el-divider {
  margin-top: 40px;
  margin-bottom: 40px;
}

.prediction-result {
  text-align: center;
  margin-top: 0;
  padding-bottom: 20px;
}

.prediction-result h3 {
  color: #333;
  margin-bottom: 15px;
}

.prediction-result p {
  font-size: 1.6em;
  font-weight: bold;
}

.el-form-item__label {
  color: #606266;
  font-weight: bold;
}

.el-card__body {
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.el-form {
  height: 500px;
}
</style>
