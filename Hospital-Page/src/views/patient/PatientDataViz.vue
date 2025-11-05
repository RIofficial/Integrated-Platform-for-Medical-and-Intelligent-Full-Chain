<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: 'patientdataviz' }">患者大数据</el-breadcrumb-item>
    </el-breadcrumb>
    <h2>患者年龄分布</h2>
    <div id="ageChart" style="width: 600px; height: 400px;"></div>
    <h2>患者病情分布</h2>
    <div id="disasterChart" style="width: 600px; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';

const ageData = ref([]);
const disasterData = ref([]);

const fetchPatientData = async () => {
  try {
    // 获取患者年龄分布数据
    const ageResponse = await axios.get('http://localhost:8081/patient/countByAge');
    if (ageResponse.data.status === 200) {
      ageData.value = ageResponse.data.data.map(item => ({
        name: `${item.age} 岁`, // 假设age字段是年龄
        value: item.count
      }));
      initAgeChart();
    } else {
      ElMessage.error("年龄数据获取失败: " + ageResponse.data.errorMsg);
    }

    // 获取患者病情分布数据
    const disasterResponse = await axios.get('http://localhost:8081/patient/countByDisaster');
    if (disasterResponse.data.status === 200) {
      disasterData.value = disasterResponse.data.data.map(item => ({
        name: item.disaster, // 假设disaster字段是病情
        value: item.count
      }));
      initDisasterChart();
    } else {
      ElMessage.error("病情数据获取失败: " + disasterResponse.data.errorMsg);
    }

  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching patient data:", error);
  }
};

const initAgeChart = () => {
  const chartDom = document.getElementById('ageChart');
  if (chartDom) {
    const myChart = echarts.init(chartDom);
    const option = {
      title: {
        text: '患者年龄分布',
        subtext: '按年龄统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ageData.value.map(item => item.name)
      },
      series: [
        {
          name: '年龄',
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: ageData.value,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    myChart.setOption(option);
  }
};

const initDisasterChart = () => {
  const chartDom = document.getElementById('disasterChart');
  if (chartDom) {
    const myChart = echarts.init(chartDom);
    const option = {
      title: {
        text: '患者病情分布',
        subtext: '按病情类型统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: disasterData.value.map(item => item.name)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '病情',
          type: 'bar',
          data: disasterData.value.map(item => item.value)
        }
      ]
    };
    myChart.setOption(option);
  }
};

onMounted(() => {
  fetchPatientData();
});
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
