<template>
  <div class="page-container" v-loading="loading">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>营养统计</span>
          <el-date-picker v-model="selectedDate" type="date" value-format="YYYY-MM-DD" @change="loadData" />
        </div>
      </template>

      <!-- 今日营养概览 -->
      <el-row :gutter="20" v-if="dailyStats">
        <el-col :span="6">
          <div class="stat-card calorie">
            <div class="stat-value">{{ dailyStats.totalCalories }}</div>
            <div class="stat-label">卡路里 (千卡)</div>
            <el-progress :percentage="Math.min(dailyStats.caloriePercentage, 100)" :color="calorieColor" />
            <div class="stat-goal">目标: {{ dailyStats.calorieGoal }} 千卡</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card protein">
            <div class="stat-value">{{ dailyStats.totalProtein?.toFixed(1) }}</div>
            <div class="stat-label">蛋白质 (克)</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card fat">
            <div class="stat-value">{{ dailyStats.totalFat?.toFixed(1) }}</div>
            <div class="stat-label">脂肪 (克)</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card carb">
            <div class="stat-value">{{ dailyStats.totalCarb?.toFixed(1) }}</div>
            <div class="stat-label">碳水 (克)</div>
          </div>
        </el-col>
      </el-row>

      <!-- 周卡路里趋势图 -->
      <div class="chart-section" ref="chartRef">
        <h3>近7天卡路里摄入趋势</h3>
        <div ref="echartsRef" style="width: 100%; height: 300px;"></div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { getDailyStats, getWeeklyStats } from '@/api/nutrition'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const loading = ref(false)
const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const dailyStats = ref(null)
const echartsRef = ref(null)
let chartInstance = null

const calorieColor = computed(() => {
  const p = dailyStats.value?.caloriePercentage || 0
  if (p < 80) return '#67c23a'
  if (p < 100) return '#e6a23c'
  return '#f56c6c'
})

async function loadData() {
  loading.value = true
  try {
    const [daily, weekly] = await Promise.all([
      getDailyStats(selectedDate.value),
      getWeeklyStats(selectedDate.value)
    ])
    dailyStats.value = daily.data
    await nextTick()
    renderChart(weekly.data.weeklyCalories)
  } finally { loading.value = false }
}

function renderChart(weeklyData) {
  if (!echartsRef.value) return
  if (!chartInstance) {
    chartInstance = echarts.init(echartsRef.value)
  }
  chartInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: weeklyData.map(d => d.date)
    },
    yAxis: { type: 'value', name: '千卡' },
    series: [{
      name: '卡路里',
      type: 'bar',
      data: weeklyData.map(d => d.calories),
      itemStyle: { color: '#409EFF' }
    }]
  })
}

onMounted(() => loadData())
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.stat-card { text-align: center; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
.stat-card.calorie { background: #f0f9ff; }
.stat-card.protein { background: #f0f9eb; }
.stat-card.fat { background: #fdf6ec; }
.stat-card.carb { background: #fef0f0; }
.stat-value { font-size: 28px; font-weight: bold; color: #333; }
.stat-label { color: #666; margin: 5px 0 10px; }
.stat-goal { font-size: 12px; color: #999; margin-top: 5px; }
.chart-section { margin-top: 30px; }
.chart-section h3 { margin-bottom: 15px; }
</style>
