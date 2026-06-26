import request from '@/utils/request'

export function getPlansByRange(start, end) {
  return request.get('/meal-plan/range', { params: { start, end } })
}

export function getPlansByDate(date) {
  return request.get(`/meal-plan/day/${date}`)
}

export function createPlan(data) {
  return request.post('/meal-plan', data)
}

export function updatePlan(data) {
  return request.put('/meal-plan', data)
}

export function deletePlan(id) {
  return request.delete(`/meal-plan/${id}`)
}

export function toggleCompleted(id) {
  return request.put(`/meal-plan/toggle/${id}`)
}
