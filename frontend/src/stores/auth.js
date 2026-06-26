import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi } from '@/api/auth'
import { getUserInfo } from '@/api/user'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const avatar = ref(localStorage.getItem('avatar') || '')

  const isLoggedIn = computed(() => !!token.value)

  async function login(loginForm) {
    const res = await loginApi(loginForm)
    token.value = res.data.token
    userId.value = res.data.userId
    username.value = res.data.username
    nickname.value = res.data.nickname || res.data.username
    avatar.value = res.data.avatar || ''

    localStorage.setItem('token', token.value)
    localStorage.setItem('userId', userId.value)
    localStorage.setItem('username', username.value)
    localStorage.setItem('nickname', nickname.value)
    localStorage.setItem('avatar', avatar.value)
  }

  async function register(registerForm) {
    await registerApi(registerForm)
  }

  async function fetchUserInfo() {
    const res = await getUserInfo()
    nickname.value = res.data.nickname || username.value
    avatar.value = res.data.avatar || ''
    localStorage.setItem('nickname', nickname.value)
    localStorage.setItem('avatar', avatar.value)
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    nickname.value = ''
    avatar.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('nickname')
    localStorage.removeItem('avatar')
  }

  return { token, userId, username, nickname, avatar, isLoggedIn, login, register, fetchUserInfo, logout }
})
