<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: 'treatmentdataviz' }">医疗大数据</el-breadcrumb-item>
    </el-breadcrumb>
    <h2>按医生统计治疗次数</h2>
    <div id="uidChart" style="width: 600px; height: 400px;"></div>
    <h2>按患者统计治疗次数</h2>
    <div id="ridChart" style="width: 600px; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';

const uidData = ref([]);
const ridData = ref([]);

const fetchTreatmentData = async () => {
  try {
    // 获取按医生统计治疗次数数据
    const uidResponse = await axios.get('http://localhost:8081/treatment/countByUid');
    if (uidResponse.data.status === 200) {
      uidData.value = uidResponse.data.data.map(item => ({
        name: `医生编号: ${item.uid}`, // 假设uid字段是医生编号
        value: item.count
      }));
      initUidChart();
    } else {
      ElMessage.error("医生治疗次数数据获取失败: " + uidResponse.data.errorMsg);
    }

    // 获取按患者统计治疗次数数据
    const ridResponse = await axios.get('http://localhost:8081/treatment/countByRid');
    if (ridResponse.data.status === 200) {
      ridData.value = ridResponse.data.data.map(item => ({
        name: `患者编号: ${item.rid}`, // 假设rid字段是患者编号
        value: item.count
      }));
      initRidChart();
    } else {
      ElMessage.error("患者治疗次数数据获取失败: " + ridResponse.data.errorMsg);
    }

  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching treatment data:", error);
  }
};

const initUidChart = () => {
  const chartDom = document.getElementById('uidChart');
  if (chartDom) {
    const myChart = echarts.init(chartDom);
    const option = {
      title: {
        text: '按医生统计治疗次数',
        subtext: '按医生编号统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: uidData.value.map(item => item.name)
      },
      series: [
        {
          name: '医生',
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: uidData.value,
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

const initRidChart = () => {
  const chartDom = document.getElementById('ridChart');
  if (chartDom) {
    const myChart = echarts.init(chartDom);
    const option = {
      title: {
        text: '按患者统计治疗次数',
        subtext: '按患者编号统计',
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
        data: ridData.value.map(item => item.name)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '患者',
          type: 'bar',
          data: ridData.value.map(item => item.value)
        }
      ]
    };
    myChart.setOption(option);
  }
};

onMounted(() => {
  fetchTreatmentData();
});
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
