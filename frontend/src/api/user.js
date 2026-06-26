import request from '@/utils/request'

export function getUserInfo() {
  return request.get('/user/info')
}

export function updateUserInfo(data) {
  return request.put('/user/info', data)
}

export function changePassword(oldPassword, newPassword) {
  return request.put('/user/password', null, { params: { oldPassword, newPassword } })
}
