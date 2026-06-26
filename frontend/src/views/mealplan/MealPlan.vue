<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>膳食计划</span>
          <el-button type="primary" @click="showAddDialog = true">添加计划</el-button>
        </div>
      </template>

      <!-- 日期选择 -->
      <div class="date-bar">
        <el-button-group>
          <el-button @click="changeWeek(-1)">上一周</el-button>
          <el-button>{{ weekRange }}</el-button>
          <el-button @click="changeWeek(1)">下一周</el-button>
        </el-button-group>
        <el-button type="success" @click="generateShoppingList">生成购物清单</el-button>
      </div>

      <!-- 日历视图 -->
      <el-row :gutter="10" class="week-view">
        <el-col :span="24/7" v-for="day in weekDays" :key="day.date">
          <div class="day-card">
            <div class="day-header">
              <span class="day-name">{{ day.name }}</span>
              <span class="day-date">{{ day.dateLabel }}</span>
            </div>
            <div class="day-content">
              <div v-for="plan in getPlansByDate(day.date)" :key="plan.id" class="plan-item"
                   :class="{ completed: plan.completed }">
                <el-tag size="small" :type="mealTypeTag(plan.mealType)">{{ mealTypeText(plan.mealType) }}</el-tag>
                <span class="plan-recipe" @click="$router.push(`/recipe/detail/${plan.recipeId}`)">
                  {{ getRecipeName(plan.recipeId) }}
                </span>
                <el-icon class="check-icon" @click.stop="toggleCompleted(plan)">
                  <CircleCheck v-if="plan.completed" />
                  <CircleClose v-else />
                </el-icon>
              </div>
              <el-empty v-if="getPlansByDate(day.date).length === 0" :image-size="40" description="无计划" />
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 添加计划对话框 -->
    <el-dialog v-model="showAddDialog" title="添加膳食计划" width="500px">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="日期">
          <el-date-picker v-model="addForm.planDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="餐次">
          <el-select v-model="addForm.mealType">
            <el-option label="早餐" :value="1" />
            <el-option label="午餐" :value="2" />
            <el-option label="晚餐" :value="3" />
            <el-option label="加餐" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜谱">
          <el-select v-model="addForm.recipeId" filterable placeholder="搜索选择菜谱">
            <el-option v-for="r in recipes" :key="r.id" :label="r.title" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="addForm.note" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getPlansByRange, createPlan, toggleCompleted as toggleCompletedApi } from '@/api/mealPlan'
import { getMyRecipes } from '@/api/recipe'
import { generateFromMealPlan } from '@/api/shopping'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const plans = ref([])
const recipes = ref([])
const showAddDialog = ref(false)
const weekStart = ref(dayjs().startOf('week'))

const addForm = reactive({
  planDate: dayjs().format('YYYY-MM-DD'),
  mealType: 1,
  recipeId: null,
  note: ''
})

const weekRange = computed(() => {
  const s = weekStart.value
  return `${s.format('MM-DD')} ~ ${s.add(6, 'day').format('MM-DD')}`
})

const weekDays = computed(() => {
  const names = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return Array.from({ length: 7 }, (_, i) => {
    const d = weekStart.value.add(i, 'day')
    return { name: names[d.day()], date: d.format('YYYY-MM-DD'), dateLabel: d.format('MM/DD') }
  })
})

function mealTypeText(t) { return { 1: '早餐', 2: '午餐', 3: '晚餐', 4: '加餐' }[t] || '' }
function mealTypeTag(t) { return { 1: 'success', 2: 'warning', 3: 'danger', 4: 'info' }[t] || '' }

function getPlansByDate(date) { return plans.value.filter(p => p.planDate === date) }
function getRecipeName(id) { return recipes.value.find(r => r.id === id)?.title || '未知菜谱' }

function changeWeek(delta) {
  weekStart.value = weekStart.value.add(delta * 7, 'day')
  loadPlans()
}

async function loadPlans() {
  const start = weekStart.value.format('YYYY-MM-DD')
  const end = weekStart.value.add(6, 'day').format('YYYY-MM-DD')
  const res = await getPlansByRange(start, end)
  plans.value = res.data
}

async function handleAdd() {
  await createPlan(addForm)
  ElMessage.success('添加成功')
  showAddDialog.value = false
  loadPlans()
}

async function toggleCompleted(plan) {
  await toggleCompletedApi(plan.id)
  plan.completed = plan.completed === 0 ? 1 : 0
}

async function generateShoppingList() {
  const start = weekStart.value.format('YYYY-MM-DD')
  const end = weekStart.value.add(6, 'day').format('YYYY-MM-DD')
  await generateFromMealPlan(start, end)
  ElMessage.success('购物清单已生成')
}

onMounted(async () => {
  const res = await getMyRecipes()
  recipes.value = res.data
  loadPlans()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.date-bar { display: flex; justify-content: space-between; margin-bottom: 20px; }
.week-view { margin-top: 10px; }
.day-card { border: 1px solid #ebeef5; border-radius: 4px; min-height: 250px; }
.day-header { padding: 10px; text-align: center; background: #f5f7fa; border-bottom: 1px solid #ebeef5; }
.day-name { font-weight: bold; display: block; }
.day-date { font-size: 12px; color: #999; }
.day-content { padding: 8px; }
.plan-item { display: flex; align-items: center; gap: 4px; margin-bottom: 8px; font-size: 13px; flex-wrap: wrap; }
.plan-item.completed { opacity: 0.5; text-decoration: line-through; }
.plan-recipe { cursor: pointer; color: #409EFF; flex: 1; }
.check-icon { cursor: pointer; }
</style>
