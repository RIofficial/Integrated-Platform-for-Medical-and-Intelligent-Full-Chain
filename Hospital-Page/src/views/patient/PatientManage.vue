<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'patientmanage' }">患者信息管理</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增患者</el-button>

  <el-drawer v-model="drawer" title="新增患者" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="rid"
            label="编号"
        >
          <el-input v-model="dynamicValidateForm.rid" />
        </el-form-item>
        <el-form-item
            prop="username"
            label="姓名"
        >
          <el-input v-model="dynamicValidateForm.username" />
        </el-form-item>
        <el-form-item
            prop="age"
            label="年龄"
        >
          <el-input v-model="dynamicValidateForm.age" />
        </el-form-item>
        <el-form-item
            prop="disaster"
            label="病情"
        >
          <el-input v-model="dynamicValidateForm.disaster" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" style="width: 100%">
    <el-table-column label="编号" prop="rid" />
    <el-table-column label="姓名" prop="username" />
    <el-table-column label="年龄" prop="age" />
    <el-table-column label="病情" prop="disaster" />
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="输入姓名搜索" />
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
      title="编辑患者信息"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="编号" prop="rid">
        <el-input v-model="editForm.rid" disabled />
      </el-form-item>

      <el-form-item label="姓名" prop="username">
        <el-input v-model="editForm.username" />
      </el-form-item>

      <el-form-item label="年龄" prop="age">
        <el-input v-model="editForm.age" />
      </el-form-item>

      <el-form-item label="病情" prop="disaster">
        <el-input v-model="editForm.disaster" />
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
  rid: "",
  username: "",
  age: "",
  disaster: ""
});
const editForm = reactive({
  rid: '',
  username: '',
  age: '',
  disaster: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.username.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.rid = row.rid;
  editForm.username = row.username;
  editForm.age = row.age;
  editForm.disaster = row.disaster;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/patient/changePatient', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchPatientList();
    editDialogVisible.value = false;
    // 清空编辑表单
    editForm.rid = "";
    editForm.username = "";
    editForm.age = "";
    editForm.disaster = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
};

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/patient/deletePatient/${row.rid}`);
  if(response.data.status === 200) {
    ElMessage.success('删除信息成功！');
    await fetchPatientList();
  }
  else {
    ElMessage.error('删除失败~');
  }
};

const fetchPatientList = async() => {
  try {
    const response = await axios.get('http://localhost:8081/patient/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching patient list:", error);
  }
};

onMounted(()=> {
  fetchPatientList();
});

const submitForm = async (formEl) => {
  const response = await axios.post('http://localhost:8081/patient/insertPatient', dynamicValidateForm);
      if(response.data.status === 200) {
        ElMessage.success('患者新增成功！');
        await fetchPatientList();
        drawer.value = false;
        // 清空新增表单
        dynamicValidateForm.rid="";
        dynamicValidateForm.username="";
        dynamicValidateForm.age="";
        dynamicValidateForm.disaster="";
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
