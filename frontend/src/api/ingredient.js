import request from '@/utils/request'

export function getIngredientPage(params) {
  return request.get('/ingredient/page', { params })
}

export function getIngredientList() {
  return request.get('/ingredient/list')
}

export function createIngredient(data) {
  return request.post('/ingredient', data)
}

export function updateIngredient(data) {
  return request.put('/ingredient', data)
}

export function deleteIngredient(id) {
  return request.delete(`/ingredient/${id}`)
}
