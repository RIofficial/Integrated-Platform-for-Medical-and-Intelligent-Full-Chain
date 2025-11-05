<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'doctorwork' }">医生排班</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增排班</el-button>

  <el-drawer v-model="drawer" title="新增排班" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="pid"
            label="编号"
        >
          <el-input v-model="dynamicValidateForm.pid" />
        </el-form-item>
        <el-form-item
            prop="username"
            label="姓名"
        >
          <el-input v-model="dynamicValidateForm.username" />
        </el-form-item>
        <el-form-item
            prop="date"
            label="起始时间"
        >
          <el-date-picker
              v-model="dynamicValidateForm.date"
              type="datetime"
              placeholder="选择起始时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item
            prop="endtime"
            label="截止时间"
        >
          <el-date-picker
              v-model="dynamicValidateForm.endtime"
              type="datetime"
              placeholder="选择截止时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item
            prop="position"
            label="地点"
        >
          <el-input v-model="dynamicValidateForm.position" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" style="width: 100%">
    <el-table-column label="编号" prop="pid" />
    <el-table-column label="姓名" prop="username" />
    <el-table-column label="起始时间" prop="date" />
    <el-table-column label="截止时间" prop="endtime" />
    <el-table-column label="地点" prop="position" />
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="Type to search" />
      </template>
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
          Edit
        </el-button>
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
        >
          Delete
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
      v-model="editDialogVisible"
      title="编辑排班"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="编号" prop="pid">
        <el-input v-model="editForm.pid" />
      </el-form-item>

      <el-form-item label="姓名" prop="username">
        <el-input v-model="editForm.username" />
      </el-form-item>

      <el-form-item label="起始时间" prop="date">
        <el-date-picker
            v-model="editForm.date"
            type="datetime"
            placeholder="选择起始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="截止时间" prop="endtime">
        <el-date-picker
            v-model="editForm.endtime"
            type="datetime"
            placeholder="选择截止时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="地点" prop="position">
        <el-input v-model="editForm.position" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditForm">提交修改</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
</style>

<script setup>
import {ref, computed, onMounted, reactive} from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const search = ref('');
const tableData = ref([]);
const drawer = ref(false)
const editDialogVisible = ref(false)
const dynamicValidateForm = reactive({
  pid: "",
  username: "",
  date: "",
  endtime: "",
  position: ""
})
const editForm = reactive({
  pid: '',
  username: '',
  date: '',
  endtime: "",
  position: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.username.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.pid = row.pid;
  editForm.username = row.username;
  editForm.date = row.date;
  editForm.endtime = row.endtime;
  editForm.position = row.position;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/work/changeWork', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchUserList();
    editDialogVisible.value = false;
    editForm.username = "";
    editForm.pid = "";
    editForm.date = "";
    editForm.endtime = "";
    editForm.position = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
}

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/work/deleteWork/${row.pid}`);
  if(response.data.status === 200) {
    ElMessage.success('删除信息成功！');
    await fetchUserList();
  }
  else {
    ElMessage.error('修改失败~');
  }
};

const fetchUserList = async() => {
  try {
    const response = await axios.get('http://localhost:8081/work/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching work list:", error);
  }
};
onMounted(()=> {
  fetchUserList();
});
const submitForm = async () => {
  const response = await axios.post('http://localhost:8081/work/insertWork', dynamicValidateForm);
  if(response.data.status === 200) {
    ElMessage.success('排班新增成功！');
    await fetchUserList();
    dynamicValidateForm.username="";
    dynamicValidateForm.date="";
    dynamicValidateForm.pid="";
    dynamicValidateForm.endtime="";
    dynamicValidateForm.position="";
  }
  else {
    ElMessage.error('新增失败~');
  }
}

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