<template>
  <div class="chat-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>医疗大模型问诊</span>
        </div>
      </template>
      <div class="messages-display" ref="messagesDisplay">
        <div v-for="(message, index) in messages" :key="index" :class="['message-item', message.sender]">
          <el-avatar v-if="message.sender === 'bot'" :src="botAvatarUrl" class="avatar"></el-avatar>
          <div :class="['message-content', message.sender === 'user' ? 'user-message' : 'bot-message']">
            {{ message.text }}
          </div>
          <el-avatar v-if="message.sender === 'user'" :src="userAvatarUrl" class="avatar"></el-avatar>
        </div>
      </div>
      <div class="input-area">
        <el-input
          v-model="inputMessage"
          placeholder="请输入您的问题..."
          @keyup.enter="sendMessage"
          clearable
        >
          <template #append>
            <el-button @click="sendMessage">发送</el-button>
          </template>
        </el-input>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

import botAvatar from '../../assets/gpt.png';
import userAvatar from '../../assets/虫大.png';

const messages = ref([
  { sender: 'bot', text: '您好，我是医疗大模型，有什么可以帮助您的吗？' }
]);
const inputMessage = ref('');
const botAvatarUrl = botAvatar;
const userAvatarUrl = userAvatar;
const messagesDisplay = ref(null);

const sendMessage = async () => {
  if (inputMessage.value.trim() === '') {
    ElMessage.warning('请输入您的问题！');
    return;
  }

  const userMessage = { sender: 'user', text: inputMessage.value };
  messages.value.push(userMessage);
  scrollToBottom();

  const query = inputMessage.value;
  inputMessage.value = ''; // 清空输入框

  try {
    const response = await axios.post('http://localhost:8081/medicalmodel/chat', { prompt: query });
    const botResponse = { sender: 'bot', text: response.data.data }; // 假设后端返回的数据在 data 字段中
    messages.value.push(botResponse);
    scrollToBottom();
  } catch (error) {
    console.error('与大模型交互失败:', error);
    ElMessage.error('与大模型交互失败，请稍后再试或检查后端服务。');
    messages.value.push({ sender: 'bot', text: '抱歉，与模型交互时出现错误。' });
    scrollToBottom();
  }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesDisplay.value) {
      messagesDisplay.value.scrollTop = messagesDisplay.value.scrollHeight;
    }
  });
};
</script>

<style scoped>
.chat-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.box-card {
  width: 80%;
  height: 90%;
  display: flex;
  flex-direction: column;
}

.box-card ::v-deep .el-card__body {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0;
  min-height: 0;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  padding: 15px 20px;
  flex-shrink: 0;
}

.messages-display {
  flex: 1;
  min-height: 0;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 10px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.message-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
}

.message-item.user {
  justify-content: flex-end;
}

.message-item.bot {
  justify-content: flex-start;
}

.avatar {
  margin: 0 10px;
  flex-shrink: 0;
}

.message-content {
  padding: 10px 15px;
  border-radius: 10px;
  max-width: 70%;
  word-wrap: break-word;
}

.user-message {
  background-color: #e1f3d8;
  color: #303133;
}

.bot-message {
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.input-area {
  margin-top: 0;
  flex-shrink: 0;
  padding-bottom: 5px;
}

</style>
