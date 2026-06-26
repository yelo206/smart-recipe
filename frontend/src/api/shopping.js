import request from '@/utils/request'

export function getShoppingList() {
  return request.get('/shopping/list')
}

export function addShoppingItem(data) {
  return request.post('/shopping', data)
}

export function updateShoppingItem(data) {
  return request.put('/shopping', data)
}

export function deleteShoppingItem(id) {
  return request.delete(`/shopping/${id}`)
}

export function togglePurchased(id) {
  return request.put(`/shopping/toggle/${id}`)
}

export function generateFromMealPlan(start, end) {
  return request.post('/shopping/generate', null, { params: { start, end } })
}
