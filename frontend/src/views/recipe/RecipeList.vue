<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="搜索菜谱名称" clearable @keyup.enter="loadData" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部分类" clearable>
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="$router.push('/recipe/edit')">新建菜谱</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 菜谱列表 -->
    <el-row :gutter="20" class="recipe-list" v-loading="loading">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="recipe in recipeList" :key="recipe.id">
        <el-card class="recipe-card" shadow="hover" @click="$router.push(`/recipe/detail/${recipe.id}`)">
          <div class="recipe-cover">
            <el-image v-if="recipe.coverImage" :src="recipe.coverImage" fit="cover" class="cover-img" />
            <div v-else class="cover-placeholder">
              <el-icon size="40"><Food /></el-icon>
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

    <!-- 空状态 -->
    <el-empty v-if="!loading && recipeList.length === 0" description="暂无菜谱" />

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[8, 12, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRecipePage } from '@/api/recipe'
import { getCategoryList } from '@/api/category'

const loading = ref(false)
const recipeList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const categories = ref([])

const searchForm = reactive({
  keyword: '',
  categoryId: null
})

function difficultyText(d) {
  const map = { 1: '简单', 2: '中等', 3: '困难' }
  return map[d] || '简单'
}

async function loadData() {
  loading.value = true
  try {
    const res = await getRecipePage({
      current: currentPage.value,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      categoryId: searchForm.categoryId || undefined
    })
    recipeList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  searchForm.keyword = ''
  searchForm.categoryId = null
  currentPage.value = 1
  loadData()
}

async function loadCategories() {
  const res = await getCategoryList()
  categories.value = res.data
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.search-card {
  margin-bottom: 20px;
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

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
