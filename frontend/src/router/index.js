import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/recipe',
    children: [
      {
        path: 'recipe',
        name: 'RecipeList',
        component: () => import('@/views/recipe/RecipeList.vue'),
        meta: { title: '菜谱管理', icon: 'Food' }
      },
      {
        path: 'ingredient-search',
        name: 'IngredientSearch',
        component: () => import('@/views/recipe/IngredientSearch.vue'),
        meta: { title: '食材找菜谱', icon: 'Search' }
      },
      {
        path: 'recipe/detail/:id',
        name: 'RecipeDetail',
        component: () => import('@/views/recipe/RecipeDetail.vue'),
        meta: { title: '菜谱详情', hidden: true }
      },
      {
        path: 'recipe/edit/:id?',
        name: 'RecipeEdit',
        component: () => import('@/views/recipe/RecipeEdit.vue'),
        meta: { title: '编辑菜谱', hidden: true }
      },
      {
        path: 'meal-plan',
        name: 'MealPlan',
        component: () => import('@/views/mealplan/MealPlan.vue'),
        meta: { title: '膳食计划', icon: 'Calendar' }
      },
      {
        path: 'shopping',
        name: 'ShoppingList',
        component: () => import('@/views/shopping/ShoppingList.vue'),
        meta: { title: '购物清单', icon: 'ShoppingCart' }
      },
      {
        path: 'nutrition',
        name: 'NutritionStats',
        component: () => import('@/views/nutrition/NutritionStats.vue'),
        meta: { title: '营养统计', icon: 'DataAnalysis' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: '个人中心', icon: 'User' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth === false) {
    next()
  } else if (!authStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
