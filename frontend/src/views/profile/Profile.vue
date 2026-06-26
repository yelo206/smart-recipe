<template>
  <div class="page-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="user-card">
            <el-avatar :size="80" :src="userInfo.avatar || undefined">
              {{ userInfo.nickname?.charAt(0) }}
            </el-avatar>
            <h3>{{ userInfo.nickname }}</h3>
            <p>{{ userInfo.username }}</p>
            <el-tag>{{ genderText(userInfo.gender) }}</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header><span>个人信息</span></template>
          <el-form :model="userInfo" label-width="100px">
            <el-form-item label="昵称"><el-input v-model="userInfo.nickname" /></el-form-item>
            <el-form-item label="邮箱"><el-input v-model="userInfo.email" /></el-form-item>
            <el-form-item label="手机号"><el-input v-model="userInfo.phone" /></el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userInfo.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
                <el-radio :value="0">保密</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="每日卡路里">
              <el-input-number v-model="userInfo.dailyCalorieGoal" :min="500" :max="5000" :step="100" /> 千卡
            </el-form-item>
            <el-form-item label="饮食偏好">
              <el-input v-model="userInfo.dietaryPreference" placeholder="如：素食、低卡、低脂" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="pwd-card">
          <template #header><span>修改密码</span></template>
          <el-form :model="pwdForm" label-width="100px">
            <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
            <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
            <el-form-item label="确认密码"><el-input v-model="pwdForm.confirmPassword" type="password" show-password /></el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getUserInfo, updateUserInfo, changePassword } from '@/api/user'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const userInfo = ref({})
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

function genderText(g) { return { 0: '保密', 1: '男', 2: '女' }[g] || '保密' }

async function loadData() {
  const res = await getUserInfo()
  userInfo.value = res.data
}

async function handleSave() {
  await updateUserInfo(userInfo.value)
  ElMessage.success('保存成功')
  authStore.fetchUserInfo()
}

async function handleChangePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请填写完整')
    return
  }
  if (pwdForm.newPassword !== pwdForm.confirmPassword) {
    ElMessage.error('两次密码不一致')
    return
  }
  await changePassword(pwdForm.oldPassword, pwdForm.newPassword)
  ElMessage.success('密码修改成功')
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
  pwdForm.confirmPassword = ''
}

onMounted(() => loadData())
</script>

<style scoped>
.user-card { text-align: center; padding: 20px 0; }
.user-card h3 { margin: 15px 0 5px; }
.user-card p { color: #999; margin-bottom: 10px; }
.pwd-card { margin-top: 20px; }
</style>
