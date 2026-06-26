import request from '@/utils/request'

export function getRecipePage(params) {
  return request.get('/recipe/page', { params })
}

export function getRecipeDetail(id) {
  return request.get(`/recipe/detail/${id}`)
}

export function createRecipe(data) {
  return request.post('/recipe', data)
}

export function updateRecipe(data) {
  return request.put('/recipe', data)
}

export function deleteRecipe(id) {
  return request.delete(`/recipe/${id}`)
}

export function getMyRecipes() {
  return request.get('/recipe/my')
}

export function searchByIngredients(ingredientIds) {
  return request.get('/recipe/by-ingredients', { params: { ingredientIds: ingredientIds.join(',') } })
}
