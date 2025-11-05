<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: 'home' }">主页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: 'drugmanage' }">药物库存</el-breadcrumb-item>
  </el-breadcrumb>
  <el-button type="primary" @click="drawer = true" style="margin-top: 10px">新增药品</el-button>

  <el-drawer v-model="drawer" title="新增药品" size="50%">
    <div>
      <el-form
          ref="formRef"
          style="max-width: 600px"
          :model="dynamicValidateForm"
          label-width="auto"
          class="demo-dynamic"
      >
        <el-form-item
            prop="did"
            label="药品编号"
        >
          <el-input v-model="dynamicValidateForm.did" />
        </el-form-item>
        <el-form-item
            prop="drugname"
            label="药品名称"
        >
          <el-input v-model="dynamicValidateForm.drugname" />
        </el-form-item>
        <el-form-item
            prop="stock"
            label="库存"
        >
          <el-input v-model="dynamicValidateForm.stock" />
        </el-form-item>
        <el-form-item
            prop="price"
            label="价格"
        >
          <el-input v-model="dynamicValidateForm.price" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">提交</el-button>
          <el-button @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>
  <el-table :data="filterTableData" style="width: 100%">
    <el-table-column label="药品编号" prop="did" />
    <el-table-column label="药品名称" prop="drugname" />
    <el-table-column label="库存" prop="stock" />
    <el-table-column label="价格" prop="price" />
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="输入药品名称搜索" />
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
      title="编辑药品信息"
      width="500px"
  >
    <el-form
        style="padding-top: 25px"
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
    >
      <el-form-item label="药品编号" prop="did">
        <el-input v-model="editForm.did" disabled />
      </el-form-item>

      <el-form-item label="药品名称" prop="drugname">
        <el-input v-model="editForm.drugname" />
      </el-form-item>

      <el-form-item label="库存" prop="stock">
        <el-input v-model="editForm.stock" />
      </el-form-item>

      <el-form-item label="价格" prop="price">
        <el-input v-model="editForm.price" />
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
  did: "",
  drugname: "",
  stock: "",
  price: ""
});
const editForm = reactive({
  did: '',
  drugname: '',
  stock: '',
  price: ''
});

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.drugname.toLowerCase().includes(search.value.toLowerCase())
    )
);

const handleEdit = (index, row) => {
  editForm.did = row.did;
  editForm.drugname = row.drugname;
  editForm.stock = row.stock;
  editForm.price = row.price;
  editDialogVisible.value = true;
};

const submitEditForm = async () => {
  const response = await axios.put('http://localhost:8081/drug/changeDrug', editForm);
  if(response.data.status === 200) {
    ElMessage.success('修改信息成功！');
    await fetchDrugList();
    editDialogVisible.value = false;
    // 清空编辑表单
    editForm.did = "";
    editForm.drugname = "";
    editForm.stock = "";
    editForm.price = "";
  }
  else {
    ElMessage.error('修改失败~');
  }
};

const handleDelete = async (index, row) => {
  const response = await axios.delete(`http://localhost:8081/drug/deleteDrug/${row.did}`);
  if(response.data.status === 200) {
    ElMessage.success('删除信息成功！');
    await fetchDrugList();
  }
  else {
    ElMessage.error('删除失败~');
  }
};

const fetchDrugList = async() => {
  try {
    const response = await axios.get('http://localhost:8081/drug/findAll');
    if(response.data.status === 200) {
      tableData.value = response.data.data;
      console.log(tableData.value);
    }
    else {
      ElMessage.error("数据获取失败: " + response.data.errorMsg);
    }
  } catch (error) {
    ElMessage.error("数据获取失败，网络错误或服务器无响应");
    console.error("Error fetching drug list:", error);
  }
};

onMounted(()=> {
  fetchDrugList();
});

const submitForm = async () => {
      const response = await axios.post('http://localhost:8081/drug/insertDrug', dynamicValidateForm);
      if(response.data.status === 200) {
        ElMessage.success('药品新增成功！');
        await fetchDrugList();
        drawer.value = false;
        // 清空新增表单
        dynamicValidateForm.did="";
        dynamicValidateForm.drugname="";
        dynamicValidateForm.stock="";
        dynamicValidateForm.price="";
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
