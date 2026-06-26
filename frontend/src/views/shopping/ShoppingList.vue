<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>购物清单</span>
          <el-button type="primary" @click="showAddDialog = true">添加项目</el-button>
        </div>
      </template>

      <el-table :data="items" v-loading="loading" border stripe>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-checkbox v-model="row.purchased" :true-value="1" :false-value="0" @change="togglePurchased(row)" />
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name" />
        <el-table-column label="数量" width="120">
          <template #default="{ row }">{{ row.amount }} {{ row.unit }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="editItem(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteItem(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="summary">
        <span>共 {{ items.length }} 项</span>
        <span>已购买 {{ purchasedCount }} 项</span>
        <span>待购买 {{ items.length - purchasedCount }} 项</span>
      </div>
    </el-card>

    <el-dialog v-model="showAddDialog" :title="editingItem ? '编辑项目' : '添加项目'" width="400px">
      <el-form :model="itemForm" label-width="60px">
        <el-form-item label="名称"><el-input v-model="itemForm.name" /></el-form-item>
        <el-form-item label="数量"><el-input-number v-model="itemForm.amount" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="itemForm.unit" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getShoppingList, addShoppingItem, updateShoppingItem, deleteShoppingItem, togglePurchased as togglePurchasedApi } from '@/api/shopping'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const items = ref([])
const showAddDialog = ref(false)
const editingItem = ref(null)

const itemForm = reactive({ id: null, name: '', amount: 1, unit: '个' })
const purchasedCount = computed(() => items.value.filter(i => i.purchased === 1).length)

async function loadData() {
  loading.value = true
  try {
    const res = await getShoppingList()
    items.value = res.data
  } finally { loading.value = false }
}

function editItem(row) {
  editingItem.value = row
  Object.assign(itemForm, row)
  showAddDialog.value = true
}

async function handleSave() {
  if (editingItem.value) {
    await updateShoppingItem(itemForm)
    ElMessage.success('更新成功')
  } else {
    await addShoppingItem(itemForm)
    ElMessage.success('添加成功')
  }
  showAddDialog.value = false
  editingItem.value = null
  Object.assign(itemForm, { id: null, name: '', amount: 1, unit: '个' })
  loadData()
}

async function deleteItem(row) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await deleteShoppingItem(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function togglePurchased(row) {
  await togglePurchasedApi(row.id)
}

onMounted(() => loadData())
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.summary { margin-top: 15px; display: flex; gap: 30px; color: #666; }
</style>
