<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: 'doctordataviz' }">医生数据可视化</el-breadcrumb-item>
    </el-breadcrumb>
    <h2>医生职责分布</h2>
    <div id="roleChart" style="width: 600px; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';

const roleData = ref([]);

const fetchRoleData = async () => {
  try {
    const response = await axios.get('http://localhost:8081/user/countByRole');
    if (response.data.status === 200) {
      roleData.value = response.data.data.map(item => ({
        name: item.role,
        value: item.count
      }));
      initChart();
    } else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching role data:", error);
  }
};

const initChart = () => {
  const chartDom = document.getElementById('roleChart');
  if (chartDom) {
    const myChart = echarts.init(chartDom);
    const option = {
      title: {
        text: '医生职责分布',
        subtext: '按角色统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: roleData.value.map(item => item.name)
      },
      series: [
        {
          name: '职责',
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: roleData.value,
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

onMounted(() => {
  fetchRoleData();
});
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
