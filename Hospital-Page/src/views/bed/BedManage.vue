<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'bedmanage' }">床位管理</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增床位</el-button>

  <el-drawer v-model="drawer" title="新增床位" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="bid"
            label="床位编号"
        >
          <el-input v-model="dynamicValidateForm.bid" />
        </el-form-item>
        <el-form-item
            prop="status"
            label="状态"
        >
          <el-select v-model="dynamicValidateForm.status" placeholder="请选择床位状态">
            <el-option label="空闲" value="空闲" />
            <el-option label="占用" value="占用" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" style="width: 100%">
    <el-table-column label="床位编号" prop="bid" />
    <el-table-column label="状态" prop="status" />
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="输入床位编号搜索" />
      </template>
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
          编辑
        </el-button>
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
      v-model="editDialogVisible"
      title="编辑床位信息"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="床位编号" prop="bid">
        <el-input v-model="editForm.bid" disabled />
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="editForm.status" placeholder="请选择床位状态">
          <el-option label="空闲" value="空闲" />
          <el-option label="占用" value="占用" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditForm">提交修改</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, computed, onMounted, reactive} from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const search = ref('');
const tableData = ref([]);
const drawer = ref(false);
const editDialogVisible = ref(false);
const dynamicValidateForm = reactive({
  bid: "",
  status: "空闲"
});
const editForm = reactive({
  bid: '',
  status: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.bid.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.bid = row.bid;
  editForm.status = row.status;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/bed/changeBed', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchBedList();
    editDialogVisible.value = false;
    // 清空编辑表单
    editForm.bid = "";
    editForm.status = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
};

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/bed/deleteBed/${row.bid}`);
  if(response.data.status === 200) {
    ElMessage.success('删除信息成功！');
    await fetchBedList();
  }
  else {
    ElMessage.error('删除失败~');
  }
};

const fetchBedList = async() => {
  try {
    const response = await axios.get('http://localhost:8081/bed/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching bed list:", error);
  }
};

onMounted(()=> {
  fetchBedList();
});

const submitForm = async () => {
  const response = await axios.post('http://localhost:8081/bed/insertBed', dynamicValidateForm);
  if(response.data.status === 200) {
    ElMessage.success('床位新增成功！');
        await fetchBedList();
        drawer.value = false;
        dynamicValidateForm.bid="";
        dynamicValidateForm.status="空闲";
      }
      else {
        ElMessage.error('新增失败~');
      }
};

const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>

<style scoped>
.el-form-item__label {
  color: black;
}
.el-form {
  background: white;
  padding: 0;
}
</style>
