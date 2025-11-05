<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'doctormanage' }">信息管理</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增用户</el-button>

  <el-drawer v-model="drawer" title="新增用户" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="uid"
            label="编号"
        >
          <el-input v-model="dynamicValidateForm.uid" />
        </el-form-item>
        <el-form-item
            prop="username"
            label="用户名"
        >
          <el-input v-model="dynamicValidateForm.username" />
        </el-form-item>
        <el-form-item
            prop="password"
            label="密码"
        >
          <el-input v-model="dynamicValidateForm.password" />
        </el-form-item>
        <el-form-item
            prop="role"
            label="职责"
        >
          <el-input v-model="dynamicValidateForm.role" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" :loading="loading" style="width: 100%">
    <el-table-column label="编号" prop="uid" />
    <el-table-column label="用户名" prop="username" />
    <el-table-column label="职责" prop="role" />
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
      title="编辑用户"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="编号" prop="uid">
        <el-input v-model="editForm.uid" />
      </el-form-item>

      <el-form-item label="用户名" prop="username">
        <el-input v-model="editForm.username" />
      </el-form-item>

      <el-form-item label="角色" prop="role">
        <el-input v-model="editForm.role" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditForm">提交修改</el-button>
    </template>
  </el-dialog>
  <div>
    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 15, 20]"
        layout="sizes, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
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
const currentPage = ref(1);
const pageSize = ref(5);
const loading = ref(false);
const total = ref(0);



const dynamicValidateForm = reactive({
  uid: "",
  username: "",
  password: "",
  role: ""
})
const editForm = reactive({
  uid: '',
  username: '',
  role: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.username.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.uid = row.uid;
  editForm.username = row.username;
  editForm.role = row.role;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/user/changeUser', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchUserList();
    editDialogVisible.value = false;
    editForm.username = "";
    editForm.uid = "";
    editForm.role = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
}

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/user/deleteUser/${row.uid}`);
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
    const response = await axios.get('http://localhost:8081/user/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching user list:", error);
  }
};
onMounted(()=> {
  // fetchUserList();
  getPageData();
});

const submitForm = async () => {
  const response = await axios.post('http://localhost:8081/user/insertUser', dynamicValidateForm);
  if(response.data.status === 200) {
    ElMessage.success('用户新增成功！');
    await fetchUserList();
    dynamicValidateForm.username="";
    dynamicValidateForm.password="";
    dynamicValidateForm.uid="";
    dynamicValidateForm.role="";
  }
  else {
    ElMessage.error('新增失败~');
  }
}

const getPageData = async () => {
  loading.value = true;
  try {
    const response = await axios.get('http://localhost:8081/user/divide', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    });

    const result = response.data;
    if (result.status === 200) {
      const pageInfo = result.data;
      tableData.value = pageInfo.list;
      total.value = pageInfo.total;
    } else {
      ElMessage.error(result.errorMsg || '获取数据失败');
    }
  } catch (error) {
    ElMessage.error('网络错误：' + (error.message || '请检查后端是否启动'));
  } finally {
    loading.value = false;
  }
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
  getPageData();
}
const handleCurrentChange = (val) => {
  currentPage.value = val;
  getPageData();
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