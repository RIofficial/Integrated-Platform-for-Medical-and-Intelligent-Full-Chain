<template>
  <div class="blockchain-work-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
      <el-breadcrumb-item>区块链排班</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>排班记录区块链状态</span>
          <el-button class="button" text @click="fetchBlockchainWorkRecords">刷新</el-button>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column label="编号" prop="pid" width="100" />
        <el-table-column label="姓名" prop="username" width="120" />
        <el-table-column label="起始时间" prop="date" width="180" />
        <el-table-column label="截止时间" prop="endtime" width="180" />
        <el-table-column label="地点" prop="position" width="120" />
        <el-table-column label="区块链交易哈希" prop="blockchainTxHash" min-width="250">
          <template #default="scope">
            <el-tooltip
              class="box-item"
              effect="dark"
              :content="scope.row.blockchainTxHash"
              placement="top-start"
            >
              <span class="hash-display">{{ truncateHash(scope.row.blockchainTxHash) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!tableData.length && !loading" description="暂无排班区块链数据" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const tableData = ref([]);
const loading = ref(false);

const fetchBlockchainWorkRecords = async () => {
  loading.value = true;
  try {
    const response = await axios.get('http://localhost:8081/blockchain-work/records');
    if (response.data.status === 200) {
      tableData.value = response.data.data;
      ElMessage.success('区块链排班数据加载成功！');
    } else {
      ElMessage.error('加载区块链排班数据失败: ' + (response.data.errorMsg || '未知错误'));
    }
  } catch (error) {
    console.error('Error fetching blockchain work records:', error);
    ElMessage.error('加载区块链排班数据失败，请检查后端服务。');
  } finally {
    loading.value = false;
  }
};

const truncateHash = (hash) => {
  if (hash && hash.length > 20) {
    return hash.substring(0, 10) + '...' + hash.substring(hash.length - 10);
  }
  return hash;
};

onMounted(() => {
  fetchBlockchainWorkRecords();
});
</script>

<style scoped>
.blockchain-work-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hash-display {
  font-family: 'Courier New', Courier, monospace;
  font-size: 0.9em;
  color: #606266;
}
</style>
