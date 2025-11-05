<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div class="header-left">
          <img src="@/assets/hospital.png" alt="Hospital Logo" class="hospital-logo"/>
          <h1>医智全链一体化平台</h1>
        </div>
        <span class="whitecolor">用户名&nbsp注册</span>
      </el-header>
      <el-container>
        <el-aside width="200px"><el-menu
            router
            default-active="2"
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
        >
          <el-sub-menu index="1">
            <template #title>
              <span style="margin-left: 29px">医生管理</span>
            </template>
              <el-menu-item style="margin-left: 10px" index="/doctormanage">信息管理</el-menu-item>
              <el-menu-item style="margin-left: 10px" index="/doctorwork">医生排班</el-menu-item>
              <el-menu-item style="margin-left: 10px" index="/doctordataviz">医生大数据</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <span style="margin-left: 29px">患者管理</span>
            </template>
            <el-menu-item style="margin-left: 10px" index="/patientmanage">信息管理</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/bedmanage">床位管理</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/patientdataviz">患者大数据</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <span style="margin-left: 29px">医疗记录</span>
            </template>
            <el-menu-item style="margin-left: 10px" index="/treatmentmanage">信息管理</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/treatmentdataviz">医疗大数据</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <span style="margin-left: 29px">财务管理</span>
            </template>
            <el-menu-item style="margin-left: 10px" index="/financemanage">信息管理</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/drugmanage">药物库存</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="6">
            <template #title>
              <span style="margin-left: 29px">智慧医疗</span>
            </template>
            <el-menu-item style="margin-left: 10px" index="/medicalchat">大模型问诊</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/braintumordetection">脑肿瘤检测</el-menu-item>
            <el-menu-item style="margin-left: 10px" index="/blockchainwork">区块链排班</el-menu-item> <!-- 新增区块链排班链接 -->
            <el-menu-item style="margin-left: 10px" index="/diabetes-prediction">糖尿病风险预测</el-menu-item>
          </el-sub-menu>

        </el-menu></el-aside>
        <el-container>
          <el-main>
            <router-view></router-view>
          </el-main>
          <el-footer style="display: flex; justify-content: center; color: gray">医院信息管理系统 辽网文〔2028〕114-514号</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style>
h1{
  background-color: transparent;
  color: #ffffff;
  font-size: 28px;
  margin: 0;
}

.el-header{
  padding: 0 20px !important;
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-between;
  align-items: center;
  background-color: #409EFF;
  color: #fff;
}
.header-left{
  display: flex;
  align-items: center;
}
.hospital-logo{
  height: 50px;
  margin-right: 10px;
}
.whitecolor{
  color: white !important;
}
</style>