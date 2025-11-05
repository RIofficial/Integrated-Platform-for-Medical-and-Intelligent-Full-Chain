<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'treatmentmanage' }">医疗记录管理</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增医疗记录</el-button>

  <el-drawer v-model="drawer" title="新增医疗记录" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="tid"
            label="治疗编号"
        >
          <el-input v-model="dynamicValidateForm.tid" />
        </el-form-item>
        <el-form-item
            prop="rid"
            label="患者编号"
        >
          <el-input v-model="dynamicValidateForm.rid" />
        </el-form-item>
        <el-form-item
            prop="uid"
            label="医生编号"
        >
          <el-input v-model="dynamicValidateForm.uid" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" style="width: 100%">
    <el-table-column label="治疗编号" prop="tid" />
    <el-table-column label="患者编号" prop="rid" />
    <el-table-column label="医生编号" prop="uid" />
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="输入患者编号搜索" />
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
      title="编辑医疗记录"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="治疗编号" prop="tid">
        <el-input v-model="editForm.tid" disabled />
      </el-form-item>

      <el-form-item label="患者编号" prop="rid">
        <el-input v-model="editForm.rid" />
      </el-form-item>

      <el-form-item label="医生编号" prop="uid">
        <el-input v-model="editForm.uid" />
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
  tid: "",
  rid: "",
  uid: ""
});
const editForm = reactive({
  tid: '',
  rid: '',
  uid: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.rid.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.tid = row.tid;
  editForm.rid = row.rid;
  editForm.uid = row.uid;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/treatment/changeTreatment', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchTreatmentList();
    editDialogVisible.value = false;
    // 清空编辑表单
    editForm.tid = "";
    editForm.rid = "";
    editForm.uid = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
};

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/treatment/deleteTreatment/${row.tid}`);
  if(response.data.status === 200) {
    ElMessage.success('删除信息成功！');
    await fetchTreatmentList();
  }
  else {
    ElMessage.error('删除失败~');
  }
};

const fetchTreatmentList = async() => {
  try {
    const response = await axios.get('http://localhost:8081/treatment/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching treatment list:", error);
  }
};

onMounted(()=> {
  fetchTreatmentList();
});

const submitForm = async (formEl) => {
      const response = await axios.post('http://localhost:8081/treatment/insertTreatment', dynamicValidateForm);
      if(response.data.status === 200) {
        ElMessage.success('医疗记录新增成功！');
        await fetchTreatmentList();
        drawer.value = false;
        // 清空新增表单
        dynamicValidateForm.tid="";
        dynamicValidateForm.rid="";
        dynamicValidateForm.uid="";
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
