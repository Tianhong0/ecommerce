<template>
  <div class="role-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>角色列表</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handlePermission(row)">分配权限</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增角色' : '编辑角色'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
    >
      <template #default>
        <div class="permission-dialog-content">
          <div class="permission-options">
            <el-checkbox v-model="defaultCheckedStrategy" label="选中主菜单时自动选择子菜单">
              选中主菜单时自动选择子菜单
            </el-checkbox>
          </div>
          <el-tree
            ref="permissionTree"
            :data="permissionTreeData"
            show-checkbox
            node-key="id"
            :props="{
              label: 'name',
              children: 'children'
            }"
            @check="handleNodeCheck"
          />
        </div>
      </template>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePermissionSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  getRoleList,
  createRole,
  updateRole,
  deleteRole,
  getRolePermissions,
  assignRolePermissions,
  getPermissionTree
} from '@/api/role'

// 定义API响应接口
interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  msg?: string;
  data?: T;
  code?: number;
}

interface RoleItem {
  id: string | number;
  name: string;
  code: string;
  description: string;
  status: number;
  createTime: string;
}

// 搜索表单
const searchForm = reactive({
  name: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<RoleItem[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const formRef = ref<FormInstance>()
const form = reactive({
  id: '',
  name: '',
  code: '',
  description: '',
  status: 1
})

// 权限树相关
const permissionDialogVisible = ref(false)
const permissionTree = ref()
const permissionTreeData = ref([])
const currentRoleId = ref('')
const defaultCheckedStrategy = ref(true)

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}

// 获取角色列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getRoleList({
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as unknown as ApiResponse<{
      records: RoleItem[];
      total: number;
      size: number;
      current: number;
    }>
    
    if (res.success && res.data) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  searchForm.status = ''
  handleSearch()
}

// 新增角色
const handleAdd = () => {
  dialogType.value = 'add'
  form.id = ''
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = 1
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = (row: RoleItem) => {
  dialogType.value = 'edit'
  form.id = String(row.id)
  form.name = row.name
  form.code = row.code
  form.description = row.description
  form.status = row.status
  dialogVisible.value = true
}

// 删除角色
const handleDelete = (row: RoleItem) => {
  ElMessageBox.confirm('确认删除该角色吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(String(row.id))
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除角色失败:', error)
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await createRole(form) as unknown as ApiResponse
          ElMessage.success('新增成功')
        } else {
          await updateRole(String(form.id), form) as unknown as ApiResponse
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('提交失败:', error)
      }
    }
  })
}

// 分配权限
const handlePermission = async (row: RoleItem) => {
  currentRoleId.value = String(row.id)
  try {
    // 获取权限树
    const treeRes = await getPermissionTree() as unknown as ApiResponse
    if (treeRes.success) {
      permissionTreeData.value = treeRes.data || []
    }
    // 获取角色已有权限
    const roleRes = await getRolePermissions(String(row.id)) as unknown as ApiResponse
    if (roleRes.success) {
      permissionTree.value?.setCheckedKeys(roleRes.data || [])
    }
    permissionDialogVisible.value = true
  } catch (error) {
    console.error('获取权限数据失败:', error)
  }
}

// 递归获取所有子节点ID
const getAllChildrenIds = (node: any): string[] => {
  let ids: string[] = []
  if (node.children && node.children.length > 0) {
    node.children.forEach((child: any) => {
      ids.push(child.id)
      ids = ids.concat(getAllChildrenIds(child))
    })
  }
  return ids
}

// 递归查找节点
const findNodeById = (nodes: any[], id: string): any => {
  for (const node of nodes) {
    if (node.id === id) {
      return node
    }
    if (node.children && node.children.length > 0) {
      const found = findNodeById(node.children, id)
      if (found) return found
    }
  }
  return null
}

// 提交权限分配
const handlePermissionSubmit = async () => {
  try {
    // 获取选中的节点ID
    const checkedKeys = permissionTree.value?.getCheckedKeys() || []
    const halfCheckedKeys = permissionTree.value?.getHalfCheckedKeys() || []
    
    // 如果启用关联选择策略，确保选中父节点时也选中所有子节点
    let finalPermissionIds: string[] = [...checkedKeys]
    
    if (defaultCheckedStrategy.value) {
      // 处理主菜单与子菜单的关联
      const processedIds = new Set<string>()
      
      // 对每个选中的节点，添加其所有子节点
      checkedKeys.forEach((id: string) => {
        const node = findNodeById(permissionTreeData.value, id)
        if (node) {
          const childrenIds = getAllChildrenIds(node)
          childrenIds.forEach((childId: string) => processedIds.add(childId))
        }
      })
      
      // 合并所有ID
      finalPermissionIds = Array.from(new Set([...checkedKeys, ...Array.from(processedIds)]))
    }
    
    // 提交分配权限请求
    await assignRolePermissions(currentRoleId.value, finalPermissionIds) as unknown as ApiResponse
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('权限分配失败:', error)
  }
}

// 节点选中处理
const handleNodeCheck = (node: any, checked: any) => {
  if (defaultCheckedStrategy.value && checked.checkedNodes && checked.checkedNodes.length > 0) {
    // 如果节点被选中且有子节点，自动选中所有子节点
    if (node.children && node.children.length > 0) {
      const childrenIds = getAllChildrenIds(node)
      childrenIds.forEach((id: string) => {
        permissionTree.value?.setChecked(id, true, false)
      })
    }
  }
}

// 分页相关
const handleSizeChange = (val: number) => {
  pageSize.value = val
  getList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  getList()
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.role-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  
  .permission-dialog-content {
    .permission-options {
      margin-bottom: 15px;
    }
  }
}
</style>
 