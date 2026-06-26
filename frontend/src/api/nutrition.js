import request from '@/utils/request'

export function getDailyStats(date) {
  return request.get(`/nutrition/daily/${date}`)
}

export function getWeeklyStats(startDate) {
  return request.get('/nutrition/weekly', { params: { startDate } })
}
