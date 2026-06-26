<template>
  <div class="page-container" v-loading="loading">
    <el-page-header @back="$router.back()" content="菜谱详情" class="page-header" />
    
    <div v-if="recipe" class="recipe-detail">
      <!-- 基本信息 -->
      <el-card class="info-card">
        <div class="recipe-header">
          <div class="recipe-cover">
            <el-image v-if="recipe.coverImage" :src="recipe.coverImage" fit="cover" class="cover-img" />
            <div v-else class="cover-placeholder">
              <el-icon size="60"><Food /></el-icon>
            </div>
          </div>
          <div class="recipe-main">
            <h1>{{ recipe.title }}</h1>
            <p class="description">{{ recipe.description }}</p>
            <div class="meta-info">
              <el-tag type="warning">{{ difficultyText(recipe.difficulty) }}</el-tag>
              <span class="meta-item"><el-icon><Timer /></el-icon> {{ recipe.cookTime }}分钟</span>
              <span class="meta-item"><el-icon><User /></el-icon> {{ recipe.servings }}人份</span>
              <span class="meta-item"><el-icon><Coin /></el-icon> {{ recipe.calories }}千卡</span>
            </div>
            <div class="nutrition-info">
              <el-descriptions :column="3" border size="small">
                <el-descriptions-item label="蛋白质">{{ recipe.protein }}g</el-descriptions-item>
                <el-descriptions-item label="脂肪">{{ recipe.fat }}g</el-descriptions-item>
                <el-descriptions-item label="碳水">{{ recipe.carbohydrate }}g</el-descriptions-item>
              </el-descriptions>
            </div>
            <div class="actions">
              <el-button type="primary" @click="$router.push(`/recipe/edit/${recipe.id}`)">编辑</el-button>
              <el-button type="success" @click="addToMealPlan">加入膳食计划</el-button>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 食材列表 -->
      <el-card class="section-card">
        <template #header><span>所需食材</span></template>
        <el-table :data="recipe.ingredients" border stripe>
          <el-table-column label="序号" type="index" width="60" />
          <el-table-column label="食材名称" prop="name" />
          <el-table-column label="用量" width="120">
            <template #default="{ row }">{{ row.amount }} {{ row.unit }}</template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 烹饪步骤 -->
      <el-card class="section-card">
        <template #header><span>烹饪步骤</span></template>
        <div class="steps-list">
          <div v-for="step in recipe.steps" :key="step.stepNumber" class="step-item">
            <div class="step-number">{{ step.stepNumber }}</div>
            <div class="step-content">
              <p>{{ step.description }}</p>
              <el-image v-if="step.imageUrl" :src="step.imageUrl" fit="cover" class="step-img" />
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getRecipeDetail } from '@/api/recipe'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const recipe = ref(null)

function difficultyText(d) {
  const map = { 1: '简单', 2: '中等', 3: '困难' }
  return map[d] || '简单'
}

async function loadData() {
  loading.value = true
  try {
    const res = await getRecipeDetail(route.params.id)
    recipe.value = res.data
  } finally {
    loading.value = false
  }
}

function addToMealPlan() {
  router.push({ path: '/meal-plan', query: { recipeId: route.params.id } })
}

onMounted(() => loadData())
</script>

<style scoped>
.page-header {
  margin-bottom: 20px;
}

.recipe-header {
  display: flex;
  gap: 30px;
}

.recipe-cover {
  width: 300px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
}

.cover-img {
  width: 100%;
  height: 200px;
}

.cover-placeholder {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #c0c4cc;
}

.recipe-main h1 {
  margin-bottom: 10px;
}

.description {
  color: #666;
  margin-bottom: 15px;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
}

.actions {
  margin-top: 20px;
}

.section-card {
  margin-top: 20px;
}

.steps-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.step-item {
  display: flex;
  gap: 15px;
}

.step-number {
  width: 32px;
  height: 32px;
  background: #409EFF;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  flex-shrink: 0;
}

.step-content p {
  line-height: 1.8;
  margin-bottom: 10px;
}

.step-img {
  max-width: 300px;
  max-height: 200px;
  border-radius: 4px;
}
</style>
