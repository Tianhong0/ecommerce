<template>
  <div class="product-container">
    <div class="header">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleAdd">
            <i class="sfont system-add"></i>新增商品
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="商品图片" width="100">
        <template #default="{ row }">
          <el-image
            v-if="row.mainImage"
            :src="row.mainImage"
            :preview-src-list="[row.mainImage]"
            fit="cover"
            style="width: 50px; height: 50px; border-radius: 4px;"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div v-else class="image-error">
            <el-icon><Picture /></el-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="categoryName" label="所属分类">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">
          ¥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
          <el-switch
            :model-value="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(row)"
            style="margin-left: 8px"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增商品' : '编辑商品'"
      width="800px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="所属分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number
            v-model="form.price"
            :precision="2"
            :step="0.1"
            :min="0"
          />
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number
            v-model="form.stock"
            :min="0"
            :precision="0"
          />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input-number
            v-model="form.sort"
            :min="0"
            :precision="0"
          />
        </el-form-item>
        <el-form-item label="商品状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="商品图片" prop="mainImage">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleUploadRemove"
            :on-preview="handlePictureCardPreview"
            :file-list="fileList"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <el-dialog v-model="previewVisible" title="预览">
            <img :src="previewUrl" alt="Preview" style="width: 100%" />
          </el-dialog>
        </el-form-item>
        <el-form-item label="SKU列表">
          <el-button type="primary" @click="handleSkuList(form)">查看SKU列表</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- SKU列表对话框 -->
    <el-dialog
      v-model="skuDialogVisible"
      title="SKU列表"
      width="800px"
    >
      <div class="sku-header">
        <el-button type="primary" @click="addSku">
          <i class="sfont system-add"></i>新增SKU
        </el-button>
      </div>
      <el-table :data="form.skuList" border style="width: 100%">
        <el-table-column prop="skuCode" label="SKU编码" width="180">
          <template #default="{ row }">
            <el-input v-model="row.skuCode" placeholder="请输入SKU编码" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="SKU名称" width="180">
          <template #default="{ row }">
            <el-input v-model="row.name" placeholder="请输入SKU名称" />
          </template>
        </el-table-column>
        <el-table-column prop="attributes" label="规格属性" width="180">
          <template #default="{ row }">
            <el-input v-model="row.attributes" placeholder="请输入规格属性" />
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            <el-input-number
              v-model="row.price"
              :precision="2"
              :step="0.1"
              :min="0"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="120">
          <template #default="{ row }">
            <el-input-number
              v-model="row.stock"
              :min="0"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ $index }">
            <el-button type="danger" link @click="removeSku($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="skuDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除库存调整对话框 -->
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, UploadFile } from 'element-plus'
import { Plus, Picture } from '@element-plus/icons-vue'
import {
  getProductList,
  createProduct,
  updateProduct,
  deleteProduct,
  updateProductStatus,
  updateProductStock,
  getCategoryList
} from '@/api/product'
import { uploadFile } from '@/api/upload'

interface ProductItem {
  id: string | number;
  name: string;
  categoryId: string | number;
  description: string;
  mainImage: string;
  price: number;
  stock: number;
  status: number;
  skuList: any[];
  sort: number;
}

interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  msg?: string;
  code?: number;
}

// 表格数据
const loading = ref(false)
const tableData = ref<ProductItem[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 分类选项
const categoryOptions = ref<any[]>([])

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const formRef = ref<FormInstance>()
const form = reactive<ProductItem>({
  id: '',
  name: '',
  categoryId: '',
  description: '',
  mainImage: '',
  price: 0,
  stock: 0,
  status: 1,
  skuList: [],
  sort: 0
})

// 库存对话框相关
const stockDialogVisible = ref(false)
const stockFormRef = ref<FormInstance>()
const currentProduct = ref<any>(null)
const stockForm = reactive({
  stock: 0
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择所属分类', trigger: 'change' }
  ],
  mainImage: [
    { required: true, message: '请上传商品主图', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于0', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入商品库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存必须大于0', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ],
  description: [
    { max: 500, message: '长度不能超过 500 个字符', trigger: 'blur' }
  ]
}

// 库存表单验证规则
const stockRules = {
  stock: [
    { required: true, message: '请输入调整数量', trigger: 'blur' }
  ]
}

// 文件列表
const fileList = ref<any[]>([])

// SKU对话框相关
const skuDialogVisible = ref(false)

// 搜索表单
const searchForm = reactive({
  name: ''
})

// 图片预览相关
const previewVisible = ref(false)
const previewUrl = ref('')

// 获取商品列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      size: pageSize.value,
      page: currentPage.value,
      name: searchForm.name
    }
    const res = await getProductList(params)
    if (res.success && res.data) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
      currentPage.value = res.data.current || 1
      pageSize.value = res.data.size || 10
    } else {
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 获取分类名称
const getCategoryName = (categoryId: string | number) => {
  const category = categoryOptions.value.find(item => item.id === categoryId)
  return category ? category.name : '-'
}

// 获取分类列表
const fetchCategoryList = async () => {
  try {
    const res = await getCategoryList()
    if (res.success && res.data) {
      categoryOptions.value = res.data.records || []
    } else {
      categoryOptions.value = []
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    categoryOptions.value = []
  }
}

// 新增商品
const handleAdd = () => {
  dialogType.value = 'add'
  form.id = ''
  form.name = ''
  form.categoryId = ''
  form.description = ''
  form.mainImage = ''
  form.price = 0
  form.stock = 0
  form.status = 1
  form.skuList = []
  fileList.value = []
  dialogVisible.value = true
}

// 编辑商品
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  form.id = row.id
  form.name = row.name
  form.categoryId = row.categoryId
  form.description = row.description
  form.mainImage = row.mainImage || ''
  form.price = row.price
  form.stock = row.stock
  form.status = row.status
  form.skuList = row.skuList || []
  form.sort = row.sort || 0
  
  // 设置图片预览
  if (row.mainImage) {
    fileList.value = [{
      name: '商品主图',
      url: row.mainImage,
      status: 'success'
    }]
  } else {
    fileList.value = []
  }
  
  dialogVisible.value = true
}

// 删除商品
const handleDelete = (row: ProductItem) => {
  ElMessageBox.confirm('确认删除该商品吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProduct(String(row.id))
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除商品失败:', error)
    }
  })
}

// 更新商品状态
const handleStatusChange = async (row: ProductItem) => {
  // 先更新本地状态
  const newStatus = row.status === 1 ? 0 : 1
  row.status = newStatus

  try {
    // 使用编辑商品接口来更新状态
    await updateProduct(String(row.id), { ...row, status: newStatus })
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('更新商品状态失败:', error)
    // 恢复状态
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

// 调整库存
const handleStock = (row: any) => {
  currentProduct.value = row
  stockForm.stock = row.stock
  stockDialogVisible.value = true
}

// 提交库存调整
const handleStockSubmit = async () => {
  if (!stockFormRef.value) return
  await stockFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateProductStock(currentProduct.value.id, stockForm.stock)
        ElMessage.success('库存更新成功')
        stockDialogVisible.value = false
        getList()
      } catch (error) {
        console.error('更新库存失败:', error)
      }
    }
  })
}

// 打开SKU列表
const handleSkuList = (row: any) => {
  form.skuList = row.skuList || []
  skuDialogVisible.value = true
}

// 添加SKU
const addSku = () => {
  form.skuList.push({
    skuCode: '',
    name: '',
    attributes: '',
    price: 0,
    stock: 0,
    status: 1
  })
}

// 删除SKU
const removeSku = (index: number) => {
  form.skuList.splice(index, 1)
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          ...form,
          // 确保图片URL正确传递
          mainImage: form.mainImage || ''
        }
        
        if (dialogType.value === 'add') {
          await createProduct(submitData)
          ElMessage.success('新增成功')
        } else {
          await updateProduct(String(form.id), submitData)
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

// 分页相关
const handleSizeChange = (val: number) => {
  pageSize.value = val
  getList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  getList()
}

// 处理图片选择
const handleImageChange = async (file: UploadFile) => {
  const isImage = file.raw?.type.startsWith('image/')
  const isLt10M = file.raw?.size && file.raw.size / 1024 / 1024 < 10

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB！')
    return false
  }

  try {
    // 显示上传中状态
    file.status = 'uploading'
    
    // 上传文件
    const res = await uploadFile(file.raw as File)
    if (res.data) {
      // 上传成功，更新图片URL
      form.mainImage = res.data
      fileList.value = [{
        name: file.name,
        url: res.data,
        status: 'success'
      }]
      ElMessage.success('图片上传成功')
    } else {
      file.status = 'fail'
      ElMessage.error('图片上传失败')
    }
  } catch (error) {
    console.error('图片上传失败:', error)
    file.status = 'fail'
    ElMessage.error('图片上传失败')
  }
}

// 处理图片预览
const handlePictureCardPreview = (file: UploadFile) => {
  previewUrl.value = file.url as string
  previewVisible.value = true
}

// 处理图片移除
const handleUploadRemove = () => {
  form.mainImage = ''
  fileList.value = []
  // 如果是编辑模式，需要标记图片已被删除
  if (dialogType.value === 'edit') {
    form.mainImage = ''
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
  handleSearch()
}

onMounted(() => {
  getList()
  fetchCategoryList()
})
</script>

<style lang="scss" scoped>
.product-container {
  padding: 20px;

  .header {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .sku-item {
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
    align-items: center;

    .el-input,
    .el-input-number {
      width: 200px;
    }
  }

  .image-error {
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f7fa;
    border-radius: 4px;
    color: #909399;
    font-size: 20px;
  }
}
</style> 