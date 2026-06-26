<template>
  <div class="page-container">
    <el-page-header @back="$router.back()" :content="isEdit ? '编辑菜谱' : '新建菜谱'" class="page-header" />
    
    <el-card>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="菜谱名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入菜谱名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="简单描述菜谱" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-input v-model="form.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="难度">
              <el-select v-model="form.difficulty">
                <el-option label="简单" :value="1" />
                <el-option label="中等" :value="2" />
                <el-option label="困难" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="烹饪时间">
              <el-input-number v-model="form.cookTime" :min="1" :max="999" /> 分钟
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="份数">
              <el-input-number v-model="form.servings" :min="1" :max="99" /> 人份
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="卡路里">
              <el-input-number v-model="form.calories" :min="0" /> 千卡
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="蛋白质(g)">
              <el-input-number v-model="form.protein" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="脂肪(g)">
              <el-input-number v-model="form.fat" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="碳水(g)">
              <el-input-number v-model="form.carbohydrate" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">食材列表</el-divider>
        <div v-for="(item, index) in form.ingredients" :key="index" class="ingredient-row">
          <el-input v-model="item.name" placeholder="食材名称" style="width: 200px" />
          <el-input-number v-model="item.amount" :min="0" :precision="2" placeholder="用量" style="width: 150px" />
          <el-input v-model="item.unit" placeholder="单位" style="width: 100px" />
          <el-button type="danger" :icon="Delete" circle @click="form.ingredients.splice(index, 1)" />
        </div>
        <el-button type="primary" plain :icon="Plus" @click="addIngredient">添加食材</el-button>

        <el-divider content-position="left">烹饪步骤</el-divider>
        <div v-for="(step, index) in form.steps" :key="index" class="step-row">
          <div class="step-num">{{ index + 1 }}</div>
          <el-input v-model="step.description" type="textarea" :rows="2" placeholder="步骤描述" style="flex: 1" />
          <el-button type="danger" :icon="Delete" circle @click="form.steps.splice(index, 1)" />
        </div>
        <el-button type="primary" plain :icon="Plus" @click="addStep">添加步骤</el-button>

        <el-divider />
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getRecipeDetail, createRecipe, updateRecipe } from '@/api/recipe'
import { getCategoryList } from '@/api/category'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const saving = ref(false)
const categories = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null,
  title: '',
  categoryId: null,
  description: '',
  coverImage: '',
  difficulty: 1,
  cookTime: 30,
  servings: 2,
  calories: 0,
  protein: 0,
  fat: 0,
  carbohydrate: 0,
  ingredients: [],
  steps: []
})

const rules = {
  title: [{ required: true, message: '请输入菜谱名称', trigger: 'blur' }]
}

function addIngredient() {
  form.ingredients.push({ ingredientId: null, name: '', amount: 1, unit: '克' })
}

function addStep() {
  form.steps.push({ stepNumber: form.steps.length + 1, description: '', imageUrl: '' })
}

async function loadData() {
  if (isEdit.value) {
    const res = await getRecipeDetail(route.params.id)
    Object.assign(form, res.data)
  }
  const catRes = await getCategoryList()
  categories.value = catRes.data
}

async function handleSave() {
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      if (isEdit.value) {
        await updateRecipe(form)
        ElMessage.success('更新成功')
      } else {
        await createRecipe(form)
        ElMessage.success('创建成功')
      }
      router.push('/recipe')
    } finally {
      saving.value = false
    }
  })
}

onMounted(() => loadData())
</script>

<style scoped>
.page-header { margin-bottom: 20px; }
.ingredient-row { display: flex; gap: 10px; margin-bottom: 10px; align-items: center; }
.step-row { display: flex; gap: 10px; margin-bottom: 15px; align-items: flex-start; }
.step-num { width: 32px; height: 32px; background: #409EFF; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
</style>
