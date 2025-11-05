<template>
  <div class="loginbox">
    <h2>医智全链一体化平台</h2>
    <el-form
        ref="ruleFormRef"
        style="max-width: 600px;"
        :model="ruleForm"
        :rules="rules"
        label-width="auto"
    >
      <el-form-item class="marginInput" label="用户名" prop="name">
        <el-input class="sizeInput" v-model="ruleForm.username" />
      </el-form-item>
      <el-form-item label="密码" prop="name">
        <el-input v-model="ruleForm.password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm(ruleFormRef)">
          登录
        </el-button>
        <el-button @click="resetForm(ruleFormRef)">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data()
  {
    return {
      ruleForm: {
        username: '',
        password: '',
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度为3到10字符', trigger: 'blur' },
        ],
      }
    };
  },
  methods: {
    async submitForm() {
      let params = new URLSearchParams();
      params.append('username',this.ruleForm.username);
      params.append('password',this.ruleForm.password);
      const result = await this.$axios.post('http://localhost:8081/user/login',params);
      if(result.data.status === 200){
        this.$router.push({
          name: 'home',
        });
      }else{
        this.$message.error(result.data.message);
      }
    }
  }
}

</script>

<style>
.loginbox {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url("../assets/Big_border.png") no-repeat;
  background-size: cover;
  flex-direction: column;
}
.el-form {
  background: url("../assets/Small-border.png") no-repeat;
  background-size: cover;
  width: 450px;
  height: 250px;
}
.el-input__wrapper{
  max-width: 300px;
}
.el-form-item__content {
  display: flex;
  justify-content: center;
  gap: 20px;
}
.marginInput{
  margin-top: 70px;
}
h2{
  color: white;
  margin-bottom: 10px;
}
.el-form-item__label {
  margin-left: 40px;
}
</style>