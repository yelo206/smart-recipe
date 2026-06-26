<template>
  <div class="page-container">
    <!-- 食材选择区 -->
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>选择你有的食材</span>
          <el-input
            v-model="ingredientKeyword"
            placeholder="搜索食材"
            clearable
            style="width: 200px"
            :prefix-icon="Search"
          />
        </div>
      </template>

      <div class="ingredient-tags" v-loading="loadingIngredients">
        <el-check-tag
          v-for="item in filteredIngredients"
          :key="item.id"
          :checked="selectedIds.includes(item.id)"
          @change="toggleIngredient(item.id)"
          class="ingredient-tag"
        >
          {{ item.name }}
        </el-check-tag>
        <el-empty v-if="filteredIngredients.length === 0" description="未找到匹配食材" :image-size="60" />
      </div>

      <div class="search-actions">
        <el-button type="primary" :disabled="selectedIds.length === 0" @click="searchRecipes">
          <el-icon><Search /></el-icon>
          搜索菜谱（已选{{ selectedIds.length }}种）
        </el-button>
        <el-button @click="clearSelection" :disabled="selectedIds.length === 0">清空选择</el-button>
      </div>
    </el-card>

    <!-- 搜索结果 -->
    <div v-if="searched" class="result-section">
      <div class="result-header">
        <span>找到 {{ recipeList.length }} 道菜谱</span>
      </div>

      <el-row :gutter="20" class="recipe-list" v-loading="loading">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="recipe in recipeList" :key="recipe.id">
          <el-card class="recipe-card" shadow="hover" @click="$router.push(`/recipe/detail/${recipe.id}`)">
            <div class="recipe-cover">
              <el-image v-if="recipe.coverImage" :src="recipe.coverImage" fit="cover" class="cover-img" />
              <div v-else class="cover-placeholder">
                <el-icon size="40"><Food /></el-icon>
              </div>
              <!-- 匹配数量角标 -->
              <div class="match-badge">
                匹配{{ recipe.matchCount }}种
              </div>
            </div>
            <div class="recipe-info">
              <h3 class="recipe-title">{{ recipe.title }}</h3>
              <p class="recipe-desc">{{ recipe.description }}</p>
              <div class="recipe-meta">
                <el-tag size="small" type="warning">{{ difficultyText(recipe.difficulty) }}</el-tag>
                <span class="meta-item"><el-icon><Timer /></el-icon> {{ recipe.cookTime }}分钟</span>
                <span class="meta-item"><el-icon><User /></el-icon> {{ recipe.servings }}人份</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && recipeList.length === 0" description="没有找到匹配的菜谱，试试选择更多食材" />
    </div>

    <!-- 初始提示 -->
    <el-empty v-if="!searched" description="选择上方食材后点击搜索，帮你找到能做的菜" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search, Food, Timer, User } from '@element-plus/icons-vue'
import { getIngredientList } from '@/api/ingredient'
import { searchByIngredients } from '@/api/recipe'

// 食材列表
const allIngredients = ref([])
const loadingIngredients = ref(false)
const ingredientKeyword = ref('')

// 选中的食材ID
const selectedIds = ref([])

// 搜索结果
const recipeList = ref([])
const loading = ref(false)
const searched = ref(false)

// 过滤后的食材列表
const filteredIngredients = computed(() => {
  if (!ingredientKeyword.value) return allIngredients.value
  return allIngredients.value.filter(i =>
    i.name.includes(ingredientKeyword.value)
  )
})

// 切换食材选中状态
function toggleIngredient(id) {
  const idx = selectedIds.value.indexOf(id)
  if (idx > -1) {
    selectedIds.value.splice(idx, 1)
  } else {
    selectedIds.value.push(id)
  }
}

// 清空选择
function clearSelection() {
  selectedIds.value = []
}

// 搜索菜谱
async function searchRecipes() {
  if (selectedIds.value.length === 0) return
  loading.value = true
  searched.value = true
  try {
    const res = await searchByIngredients(selectedIds.value)
    recipeList.value = res.data
  } finally {
    loading.value = false
  }
}

// 难度文本
function difficultyText(d) {
  const map = { 1: '简单', 2: '中等', 3: '困难' }
  return map[d] || '简单'
}

// 加载全部食材
async function loadIngredients() {
  loadingIngredients.value = true
  try {
    const res = await getIngredientList()
    allIngredients.value = res.data
  } finally {
    loadingIngredients.value = false
  }
}

onMounted(() => {
  loadIngredients()
})
</script>

<style scoped>
.search-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ingredient-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
  min-height: 60px;
}

.ingredient-tag {
  margin: 0;
}

.search-actions {
  display: flex;
  gap: 10px;
}

.result-section {
  margin-top: 20px;
}

.result-header {
  margin-bottom: 16px;
  font-size: 14px;
  color: #606266;
}

.recipe-list {
  margin-bottom: 20px;
}

.recipe-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.2s;
}

.recipe-card:hover {
  transform: translateY(-3px);
}

.recipe-cover {
  height: 180px;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 12px;
  background: #f5f7fa;
  position: relative;
}

.cover-img {
  width: 100%;
  height: 100%;
}

.cover-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
}

.match-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #67c23a;
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.recipe-title {
  font-size: 16px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.recipe-desc {
  font-size: 13px;
  color: #999;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recipe-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #666;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
