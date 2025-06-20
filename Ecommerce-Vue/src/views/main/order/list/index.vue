<template>
  <div class="order-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
            <el-option label="已退款" :value="5" />
            <el-option label="待处理退款" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleCreate">创建订单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column prop="orderNo" label="订单编号" min-width="180" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="订单金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="payType" label="支付方式" width="100" align="center">
          <template #default="{ row }">
            {{ row.payType === 1 ? '微信支付' : '支付宝' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" width="100" align="center" />
        <el-table-column prop="receiverPhone" label="联系电话" width="120" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button 
              v-if="row.status === 1" 
              type="success" 
              link 
              @click="handleDelivery(row)"
            >发货</el-button>
            <el-button 
              v-if="row.status === 2" 
              type="warning" 
              link 
              @click="handleReceive(row)"
            >确认收货</el-button>
            <el-button 
              v-if="row.status === 3 && row.orderRefund === null" 
              type="danger" 
              link 
              @click="handleRefund(row)"
            >申请退款</el-button>
            <el-button 
              v-if="row.orderRefund !== null && row.orderRefund.status === 0" 
              type="warning" 
              link 
              @click="handleRefundProcess(row)"
            >处理退款</el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleDelete(row)"
            >删除</el-button>
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

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ currentOrder.payType === 1 ? '微信支付' : '支付宝' }}
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrder.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="订单备注" :span="2">{{ currentOrder.note || '无' }}</el-descriptions-item>
      </el-descriptions>

      <div class="order-items">
        <h3>商品信息</h3>
        <el-table :data="currentOrder.orderItems || []" border>
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="skuName" label="规格" />
          <el-table-column prop="price" label="单价" width="120">
            <template #default="{ row }">
              ¥{{ row.price.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column prop="totalAmount" label="小计" width="120">
            <template #default="{ row }">
              ¥{{ row.totalAmount.toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-if="currentOrder.orderDelivery" class="delivery-info">
        <h3>物流信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="物流公司">{{ currentOrder.orderDelivery.deliveryCompany }}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{ currentOrder.orderDelivery.deliverySn }}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{ currentOrder.orderDelivery.deliveryTime }}</el-descriptions-item>
          <el-descriptions-item label="签收时间">{{ currentOrder.orderDelivery.receiveTime || '未签收' }}</el-descriptions-item>
          <el-descriptions-item label="物流状态">
            <el-tag :type="getDeliveryStatusType(currentOrder.orderDelivery.status)">
              {{ getDeliveryStatusText(currentOrder.orderDelivery.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="收货人">{{ currentOrder.orderDelivery.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.orderDelivery.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.orderDelivery.receiverAddress }}</el-descriptions-item>
        </el-descriptions>

        <!-- 物流轨迹信息 -->
        <div v-if="currentOrder.orderDelivery.deliveryInfo" class="delivery-timeline">
          <h4>物流轨迹</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(info, index) in JSON.parse(currentOrder.orderDelivery.deliveryInfo)"
              :key="index"
              :timestamp="info.time"
              :type="index === 0 ? 'primary' : ''"
            >
              {{ info.info }}
            </el-timeline-item>
          </el-timeline>
        </div>

        <!-- 更新物流信息按钮 -->
        <div class="delivery-actions" v-if="currentOrder.status === 1 || currentOrder.status === 2">
          <el-button type="primary" @click="handleUpdateDelivery">更新物流信息</el-button>
        </div>
      </div>

      <div v-if="currentOrder.orderRefund" class="refund-info">
        <h3>退款信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="退款单号">{{ currentOrder.orderRefund.refundNo }}</el-descriptions-item>
          <el-descriptions-item label="退款金额">¥{{ currentOrder.orderRefund.refundAmount.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="退款原因">{{ currentOrder.orderRefund.reason }}</el-descriptions-item>
          <el-descriptions-item label="退款状态">
            <el-tag :type="getRefundStatusType(currentOrder.orderRefund.status)">
              {{ getRefundStatusText(currentOrder.orderRefund.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ currentOrder.orderRefund.applyTime }}</el-descriptions-item>
          <el-descriptions-item label="处理时间">{{ currentOrder.orderRefund.handleTime || '未处理' }}</el-descriptions-item>
          <el-descriptions-item label="退款说明" :span="2">{{ currentOrder.orderRefund.description }}</el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.orderRefund.rejectReason" label="拒绝原因" :span="2">
            {{ currentOrder.orderRefund.rejectReason }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.orderRefund.proofImages" label="凭证图片" :span="2">
            <div class="proof-images">
              <el-image
                v-for="(image, index) in JSON.parse(currentOrder.orderRefund.proofImages)"
                :key="index"
                :src="image"
                :preview-src-list="JSON.parse(currentOrder.orderRefund.proofImages)"
                fit="cover"
                class="proof-image"
              >
                <template #error>
                  <div class="image-error">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
            </div>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 退款处理按钮 -->
        <div v-if="currentOrder.orderRefund.status === 0" class="refund-actions">
          <el-button type="primary" @click="handleRefundProcess(currentOrder)">处理退款</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog
      v-model="deliveryDialogVisible"
      title="订单发货"
      width="500px"
    >
      <el-form
        ref="deliveryFormRef"
        :model="deliveryForm"
        :rules="deliveryRules"
        label-width="100px"
      >
        <el-form-item label="物流公司" prop="deliveryCompany">
          <el-input v-model="deliveryForm.deliveryCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="deliverySn">
          <el-input v-model="deliveryForm.deliverySn" placeholder="请输入物流单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deliveryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitDelivery">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 退款对话框 -->
    <el-dialog
      v-model="refundDialogVisible"
      :title="currentOrder.orderRefund ? '处理退款' : '申请退款'"
      width="500px"
    >
      <el-form
        ref="refundFormRef"
        :model="refundForm"
        :rules="refundRules"
        label-width="100px"
      >
        <template v-if="currentOrder.orderRefund">
          <!-- 退款处理表单 -->
          <el-form-item label="退款单号">{{ currentOrder.orderRefund.refundNo }}</el-form-item>
          <el-form-item label="退款金额">¥{{ currentOrder.orderRefund.refundAmount.toFixed(2) }}</el-form-item>
          <el-form-item label="退款原因">{{ currentOrder.orderRefund.reason }}</el-form-item>
          <el-form-item label="退款说明">{{ currentOrder.orderRefund.description }}</el-form-item>
          <el-form-item label="申请时间">{{ currentOrder.orderRefund.applyTime }}</el-form-item>
          <el-form-item label="处理结果" prop="status">
            <el-radio-group v-model="refundForm.status">
              <el-radio :label="1">同意退款</el-radio>
              <el-radio :label="2">拒绝退款</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item 
            label="拒绝原因" 
            prop="rejectReason"
            v-if="refundForm.status === 2"
          >
            <el-input 
              v-model="refundForm.rejectReason" 
              type="textarea" 
              placeholder="请输入拒绝原因"
            />
          </el-form-item>
        </template>
        <template v-else>
          <!-- 申请退款表单 -->
          <el-form-item label="退款金额" prop="refundAmount">
            <el-input-number 
              v-model="refundForm.refundAmount" 
              :min="0" 
              :max="currentOrder.totalAmount"
              :precision="2"
              :step="0.01"
            />
          </el-form-item>
          <el-form-item label="退款原因" prop="reason">
            <el-input 
              v-model="refundForm.reason" 
              placeholder="请输入退款原因"
            />
          </el-form-item>
          <el-form-item label="退款说明" prop="description">
            <el-input 
              v-model="refundForm.description" 
              type="textarea" 
              placeholder="请输入退款说明"
            />
          </el-form-item>
          <el-form-item label="凭证图片" prop="proofImages">
            <el-upload
              action="#"
              list-type="picture-card"
              :auto-upload="false"
              :on-change="async (file: UploadFile) => {
                try {
                  const res = await uploadFile(file.raw as File)
                  if (res.data) {
                    const images = JSON.parse(refundForm.proofImages)
                    images.push(res.data)
                    refundForm.proofImages = JSON.stringify(images)
                    ElMessage.success('图片上传成功')
                  }
                } catch (error) {
                  console.error('图片上传失败:', error)
                  ElMessage.error('图片上传失败')
                }
              }"
              :on-remove="(file: UploadFile) => {
                const images = JSON.parse(refundForm.proofImages)
                const index = images.indexOf(file.url)
                if (index > -1) {
                  images.splice(index, 1)
                  refundForm.proofImages = JSON.stringify(images)
                }
              }"
              :on-preview="(file: UploadFile) => {
                previewUrl.value = file.url
                previewVisible.value = true
              }"
              :before-upload="(file: File) => {
                const isImage = file.type.startsWith('image/')
                const isLt5M = file.size / 1024 / 1024 < 5

                if (!isImage) {
                  ElMessage.error('只能上传图片文件！')
                  return false
                }
                if (!isLt5M) {
                  ElMessage.error('图片大小不能超过 5MB！')
                  return false
                }
                return true
              }"
              :limit="5"
              :on-exceed="() => {
                ElMessage.warning('最多只能上传5张图片')
              }"
            >
              <el-icon><Plus /></el-icon>
              <template #tip>
                <div class="el-upload__tip">
                  支持jpg、png、jpeg格式，单张图片不超过5MB，最多上传5张
                </div>
              </template>
            </el-upload>
            <el-dialog v-model="previewVisible" title="预览">
              <img :src="previewUrl" alt="Preview" style="width: 100%" />
            </el-dialog>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="refundDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRefund">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 创建订单对话框 -->
    <el-dialog
      v-model="createDialogVisible"
      title="创建订单"
      width="800px"
    >
      <el-form
        ref="createFormRef"
        :model="createForm"
        :rules="createRules"
        label-width="100px"
      >
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="createForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="支付方式" prop="payType">
          <el-select v-model="createForm.payType" placeholder="请选择支付方式">
            <el-option label="支付宝" :value="1" />
            <el-option label="微信" :value="2" />
            <el-option label="银联" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="createForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input v-model="createForm.receiverPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input v-model="createForm.receiverAddress" type="textarea" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="运费" prop="freightAmount">
          <el-input-number 
            v-model="createForm.freightAmount" 
            :min="0" 
            :precision="2"
            :step="0.01"
            @change="calculateTotal"
          />
        </el-form-item>
        <el-form-item label="订单备注" prop="note">
          <el-input v-model="createForm.note" type="textarea" placeholder="请输入订单备注" />
        </el-form-item>

        <el-form-item label="商品信息" prop="orderItems">
          <div class="order-items">
            <div v-for="(item, index) in createForm.orderItems" :key="index" class="order-item">
              <el-row :gutter="20" align="middle">
                <el-col :span="4">
                  <el-image 
                    :src="item.productPic" 
                    :preview-src-list="[item.productPic]"
                    fit="cover"
                    class="product-image"
                  />
                </el-col>
                <el-col :span="6">
                  <div class="product-info">
                    <div class="product-name">{{ item.productName }}</div>
                    <div class="sku-name" v-if="item.skuName">{{ item.skuName }}</div>
                  </div>
                </el-col>
                <el-col :span="4">
                  <div class="price">¥{{ item.price.toFixed(2) }}</div>
                </el-col>
                <el-col :span="4">
                  <el-input-number 
                    v-model="item.quantity" 
                    :min="1" 
                    :step="1"
                    @change="() => handleItemChange(index)"
                    class="quantity-input"
                  />
                </el-col>
                <el-col :span="4">
                  <div class="total">¥{{ item.totalAmount.toFixed(2) }}</div>
                </el-col>
                <el-col :span="2">
                  <el-button type="danger" link @click="removeOrderItem(index)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <el-button type="primary" link @click="openProductDialog" class="add-product-btn">
              <el-icon><Plus /></el-icon>添加商品
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="订单总金额">
          <span class="total-amount">¥{{ createForm.totalAmount.toFixed(2) }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitCreate">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 商品选择对话框 -->
    <el-dialog
      v-model="productDialogVisible"
      title="选择商品"
      width="800px"
    >
      <div class="product-search">
        <el-form :inline="true" :model="productSearchForm">
          <el-form-item label="商品名称">
            <el-input v-model="productSearchForm.name" placeholder="请输入商品名称" clearable />
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select v-model="productSearchForm.status" placeholder="请选择状态" clearable>
              <el-option label="上架" :value="1" />
              <el-option label="下架" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleProductSearch">搜索</el-button>
            <el-button @click="() => {
              productSearchForm.name = ''
              productSearchForm.status = ''
              handleProductSearch()
            }">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="productLoading"
        :data="productList"
        border
        style="width: 100%"
      >
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            ¥{{ row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleSelectProduct(row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="productCurrentPage"
          v-model:page-size="productPageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="productTotal"
          @size-change="handleProductSizeChange"
          @current-change="handleProductCurrentChange"
        />
      </div>
    </el-dialog>

    <!-- SKU选择对话框 -->
    <el-dialog
      v-model="skuDialogVisible"
      title="选择规格"
      width="500px"
    >
      <div v-if="currentProduct">
        <p>请选择商品规格:</p>
        <el-select
          v-model="selectedSkuId"
          style="width: 100%"
          placeholder="请选择规格"
        >
          <el-option
            v-for="sku in currentProduct.skuList"
            :key="sku.id"
            :label="`${sku.name} (¥${sku.price.toFixed(2)})`"
            :value="sku.id"
          />
        </el-select>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="skuDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSkuSelection">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 更新物流信息对话框 -->
    <el-dialog
      v-model="updateDeliveryDialogVisible"
      title="更新物流信息"
      width="500px"
    >
      <el-form
        ref="updateDeliveryFormRef"
        :model="updateDeliveryForm"
        :rules="updateDeliveryRules"
        label-width="100px"
      >
        <el-form-item label="物流公司" prop="deliveryCompany">
          <el-input v-model="updateDeliveryForm.deliveryCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="deliverySn">
          <el-input v-model="updateDeliveryForm.deliverySn" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="物流状态" prop="status">
          <el-select v-model="updateDeliveryForm.status" placeholder="请选择物流状态">
            <el-option label="待发货" :value="0" />
            <el-option label="已发货" :value="1" />
            <el-option label="已签收" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="物流信息" prop="deliveryInfo">
          <el-input
            v-model="updateDeliveryForm.deliveryInfo"
            type="textarea"
            placeholder="请输入物流信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="updateDeliveryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUpdateDelivery">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, h } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  getOrderList,
  getOrderDetail,
  updateOrderStatus,
  updateOrderDelivery,
  updateOrderDeliveryStatus,
  handleOrderRefund,
  processOrderRefund,
  deleteOrder,
  createOrder,
  getProductList,
  getProductDetail
} from '@/api/order'
import { Plus, Delete, Picture } from '@element-plus/icons-vue'
import { uploadFile } from '@/api/upload'

interface OrderRefund {
  id: number;
  orderId: number;
  refundNo: string;
  refundAmount: number;
  status: number;
  reason: string;
  description: string;
  proofImages: string;
  rejectReason: string | null;
  applyTime: string;
  handleTime: string | null;
  completeTime: string | null;
  createTime: string;
  updateTime: string;
}

interface OrderItem {
  id: number;
  orderNo: string;
  totalAmount: number;
  payType: number;
  status: number;
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
  note: string;
  createTime: string;
  orderItems: any[];
  orderDelivery: any;
  orderRefund: OrderRefund | null;
}

interface UploadResponse {
  data: string;
}

interface UploadFile {
  url: string;
  raw?: File;
}

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<OrderItem[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 订单详情对话框
const detailDialogVisible = ref(false)
const currentOrder = ref<OrderItem>({} as OrderItem)

// 发货对话框
const deliveryDialogVisible = ref(false)
const deliveryFormRef = ref<FormInstance>()
const deliveryForm = reactive({
  deliveryCompany: '',
  deliverySn: ''
})
const deliveryRules = {
  deliveryCompany: [
    { required: true, message: '请输入物流公司', trigger: 'blur' }
  ],
  deliverySn: [
    { required: true, message: '请输入物流单号', trigger: 'blur' }
  ]
}

// 退款对话框
const refundDialogVisible = ref(false)
const refundFormRef = ref<FormInstance>()
const refundForm = reactive({
  refundAmount: 0,
  reason: '',
  description: '',
  proofImages: '[]',
  status: 1,
  rejectReason: ''
})

// 图片预览相关
const previewVisible = ref(false)
const previewUrl = ref('')

const refundRules = {
  refundAmount: [
    { required: true, message: '请输入退款金额', trigger: 'blur' }
  ],
  reason: [
    { required: true, message: '请输入退款原因', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入退款说明', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择处理结果', trigger: 'change' }
  ],
  rejectReason: [
    { required: true, message: '请输入拒绝原因', trigger: 'blur' }
  ]
}

// 创建订单对话框
const createDialogVisible = ref(false)
const createFormRef = ref<FormInstance>()
const createForm = reactive({
  userId: '',
  totalAmount: 0,
  payAmount: 0,
  freightAmount: 0,
  payType: 1,
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  note: '',
  orderItems: [] as any[]
})

const createRules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' },
    { pattern: /^\d+$/, message: '用户ID必须为数字', trigger: 'blur' }
  ],
  receiverName: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入收货人电话', trigger: 'blur' }
  ],
  receiverAddress: [
    { required: true, message: '请输入收货地址', trigger: 'blur' }
  ],
  orderItems: [
    { required: true, message: '请添加商品', trigger: 'change' }
  ]
}

// 商品选择对话框
const productDialogVisible = ref(false)
const productList = ref<any[]>([])
const productLoading = ref(false)
const productTotal = ref(0)
const productCurrentPage = ref(1)
const productPageSize = ref(10)
const productSearchForm = reactive({
  name: '',
  status: ''
})

// SKU选择对话框
const skuDialogVisible = ref(false)
const currentProduct = ref<any>(null)
const selectedSkuId = ref('')

// 物流状态相关
const getDeliveryStatusType = (status: number) => {
  const statusMap: Record<number, string> = {
    0: 'warning',
    1: 'primary',
    2: 'success'
  }
  return statusMap[status] || 'info'
}

const getDeliveryStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待发货',
    1: '已发货',
    2: '已签收'
  }
  return statusMap[status] || '未知'
}

// 更新物流信息对话框
const updateDeliveryDialogVisible = ref(false)
const updateDeliveryFormRef = ref<FormInstance>()
const updateDeliveryForm = reactive({
  deliveryCompany: '',
  deliverySn: '',
  status: 1,
  deliveryInfo: ''
})

const updateDeliveryRules = {
  deliveryCompany: [
    { required: true, message: '请输入物流公司', trigger: 'blur' }
  ],
  deliverySn: [
    { required: true, message: '请输入物流单号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择物流状态', trigger: 'change' }
  ],
  deliveryInfo: [
    { required: true, message: '请输入物流信息', trigger: 'blur' }
  ]
}

// 更新物流信息
const handleUpdateDelivery = () => {
  if (currentOrder.value.orderDelivery) {
    updateDeliveryForm.deliveryCompany = currentOrder.value.orderDelivery.deliveryCompany
    updateDeliveryForm.deliverySn = currentOrder.value.orderDelivery.deliverySn
    updateDeliveryForm.status = currentOrder.value.orderDelivery.status
    updateDeliveryForm.deliveryInfo = ''
  }
  updateDeliveryDialogVisible.value = true
}

// 提交更新物流信息
const submitUpdateDelivery = async () => {
  if (!updateDeliveryFormRef.value) return
  await updateDeliveryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 获取现有物流信息
        const existingInfo = currentOrder.value.orderDelivery.deliveryInfo
          ? JSON.parse(currentOrder.value.orderDelivery.deliveryInfo)
          : []
        
        // 添加新的物流信息
        const newInfo = {
          time: new Date().toLocaleString(),
          info: updateDeliveryForm.deliveryInfo
        }
        existingInfo.unshift(newInfo)

        // 更新物流信息
        await updateOrderDelivery(String(currentOrder.value.id), {
          deliveryCompany: updateDeliveryForm.deliveryCompany,
          deliverySn: updateDeliveryForm.deliverySn,
          receiverName: currentOrder.value.receiverName,
          receiverPhone: currentOrder.value.receiverPhone,
          receiverAddress: currentOrder.value.receiverAddress,
          deliveryInfo: JSON.stringify(existingInfo)
        })

        // 更新物流状态
        await updateOrderDeliveryStatus(String(currentOrder.value.id), {
          status: updateDeliveryForm.status
        })

        // 如果物流状态为已签收，同时更新订单状态为已完成
        if (updateDeliveryForm.status === 2) {
          await updateOrderStatus(String(currentOrder.value.id), { status: 3 })
        }

        ElMessage.success('物流信息更新成功')
        updateDeliveryDialogVisible.value = false
        handleDetail(currentOrder.value) // 刷新订单详情
      } catch (error) {
        console.error('更新物流信息失败:', error)
        ElMessage.error('更新物流信息失败')
      }
    }
  })
}

// 获取订单列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      pageSize: pageSize.value,
      pageNum: currentPage.value,
      ...searchForm
    }
    const res: any = await getOrderList(params)
    if (res.success && res.data) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    } else if (res.records) {
      tableData.value = res.records
      total.value = res.total || res.records.length
    } else {
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
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
  searchForm.orderNo = ''
  searchForm.status = ''
  handleSearch()
}

// 获取订单状态类型
const getStatusType = (status: number) => {
  const statusMap: Record<number, string> = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info',
    4: 'danger',
    5: 'danger',
    6: 'warning'
  }
  return statusMap[status] || 'info'
}

// 获取订单状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消',
    5: '已退款',
    6: '待处理退款'
  }
  return statusMap[status] || '未知'
}

// 获取退款状态类型
const getRefundStatusType = (status: number) => {
  const statusMap: Record<number, string> = {
    0: 'warning',   // 待处理
    1: 'success',   // 已同意
    2: 'danger',    // 已拒绝
    3: 'info'       // 已完成
  }
  return statusMap[status] || 'info'
}

// 获取退款状态文本
const getRefundStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待处理',
    1: '已同意',
    2: '已拒绝',
    3: '已完成'
  }
  return statusMap[status] || '未知'
}

// 查看订单详情
const handleDetail = async (row: OrderItem) => {
  try {
    const res: any = await getOrderDetail(String(row.id))
    if (res.success && res.data) {
      currentOrder.value = res.data
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
  }
}

// 发货
const handleDelivery = (row: OrderItem) => {
  currentOrder.value = row
  deliveryForm.deliveryCompany = ''
  deliveryForm.deliverySn = ''
  deliveryDialogVisible.value = true
}

// 提交发货
const submitDelivery = async () => {
  if (!deliveryFormRef.value) return
  await deliveryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateOrderDelivery(String(currentOrder.value.id), {
          deliveryCompany: deliveryForm.deliveryCompany,
          deliverySn: deliveryForm.deliverySn,
          receiverName: currentOrder.value.receiverName,
          receiverPhone: currentOrder.value.receiverPhone,
          receiverAddress: currentOrder.value.receiverAddress,
          deliveryInfo: JSON.stringify([{
            time: new Date().toLocaleString(),
            info: '快件已发出'
          }]),
          status: 1  // 固定为已发货状态
        })
        await updateOrderDeliveryStatus(String(currentOrder.value.id), { status: 1 })
        await updateOrderStatus(String(currentOrder.value.id), { status: 2 })
        ElMessage.success('发货成功')
        deliveryDialogVisible.value = false
        getList()
      } catch (error: any) {
        ElMessage.error(error.response?.data?.message || '发货失败')
      }
    }
  })
}

// 确认收货
const handleReceive = async (row: OrderItem) => {
    try {
      await updateOrderDeliveryStatus(String(row.id), { status: 2 })
      await updateOrderStatus(String(row.id), { status: 3 })
      ElMessage.success('确认收货成功')
      getList()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '确认收货失败')
    }
}

// 处理退款
const handleRefundProcess = (row: OrderItem) => {
  currentOrder.value = row
  refundForm.status = 1
  refundForm.rejectReason = ''
  refundDialogVisible.value = true
}

// 申请退款
const handleRefund = (row: OrderItem) => {
  currentOrder.value = row
  refundForm.refundAmount = row.totalAmount
  refundForm.reason = ''
  refundForm.description = ''
  refundForm.proofImages = '[]'
  refundDialogVisible.value = true
}

// 提交退款处理
const submitRefund = async () => {
  if (!refundFormRef.value) return
  await refundFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (currentOrder.value.orderRefund) {
          // 处理退款
          await processOrderRefund(String(currentOrder.value.id), {
            status: refundForm.status,
            rejectReason: refundForm.rejectReason
          })
          
          // 根据退款处理结果更新订单状态
          if (refundForm.status === 1) {
            // 同意退款，订单状态改为已退款(5)
            await updateOrderStatus(String(currentOrder.value.id), { status: 5 })
          } else if (refundForm.status === 2) {
            // 拒绝退款，订单状态改为已取消(4)
            await updateOrderStatus(String(currentOrder.value.id), { status: 4 })
          }
          
          ElMessage.success('退款处理成功')
        } else {
          // 申请退款
          await handleOrderRefund(String(currentOrder.value.id), {
            refundAmount: refundForm.refundAmount,
            reason: refundForm.reason,
            description: refundForm.description,
            proofImages: refundForm.proofImages
          })
          
          // 更新订单状态为待处理退款(6)
          await updateOrderStatus(String(currentOrder.value.id), { status: 6 })
          
          ElMessage.success('退款申请提交成功')
        }
        refundDialogVisible.value = false
        getList()
      } catch (error: any) {
        ElMessage.error(error.response?.data?.message || '退款处理失败')
      }
    }
  })
}

// 删除订单
const handleDelete = async (row: OrderItem) => {
    try {
      await deleteOrder(String(row.id))
    ElMessage.success('删除订单成功')
      getList()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '删除订单失败')
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

// 打开创建订单对话框
const handleCreate = () => {
  createForm.userId = ''
  createForm.totalAmount = 0
  createForm.payAmount = 0
  createForm.freightAmount = 0
  createForm.payType = 1
  createForm.receiverName = ''
  createForm.receiverPhone = ''
  createForm.receiverAddress = ''
  createForm.note = ''
  createForm.orderItems = []
  createDialogVisible.value = true
}

// 获取商品列表
const getProductListData = async () => {
  productLoading.value = true
  try {
    const params = {
      size: productPageSize.value,
      page: productCurrentPage.value,
      ...productSearchForm
    }
    const res: any = await getProductList(params)
    if (res.success && res.data) {
      productList.value = res.data.records || []
      productTotal.value = res.data.total || 0
    } else if (res.records) {
      productList.value = res.records
      productTotal.value = res.total || res.records.length
    } else {
      productList.value = []
      productTotal.value = 0
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    productList.value = []
    productTotal.value = 0
  } finally {
    productLoading.value = false
  }
}

// 选择商品
const handleSelectProduct = async (row: any) => {
  try {
    const res: any = await getProductDetail(String(row.id))
    if (res.success && res.data) {
      const product = res.data
      // 如果商品有SKU,需要选择SKU
      if (product.skuList && product.skuList.length > 0) {
        currentProduct.value = product
        selectedSkuId.value = ''
        skuDialogVisible.value = true
      } else {
        // 没有SKU的商品显示提示
        ElMessage.warning('该商品没有规格信息，无法添加到订单')
      }
      productDialogVisible.value = false
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
  }
}

// 确认选择SKU
const confirmSkuSelection = () => {
  if (!currentProduct.value || !selectedSkuId.value) return
  
  const sku = currentProduct.value.skuList.find((s: any) => s.id === selectedSkuId.value)
  if (sku) {
    createForm.orderItems.push({
      productId: currentProduct.value.id,
      skuId: sku.id,
      productName: currentProduct.value.name,
      skuName: sku.name,
      productPic: currentProduct.value.mainImage,
      price: sku.price,
      quantity: 1,
      totalAmount: sku.price
    })
    calculateTotal()
  }
  
  skuDialogVisible.value = false
  productDialogVisible.value = false
}

// 打开商品选择对话框
const openProductDialog = () => {
  productSearchForm.name = ''
  productSearchForm.status = ''
  productCurrentPage.value = 1
  getProductListData()
  productDialogVisible.value = true
}

// 商品搜索
const handleProductSearch = () => {
  productCurrentPage.value = 1
  getProductListData()
}

// 商品分页
const handleProductSizeChange = (val: number) => {
  productPageSize.value = val
  getProductListData()
}

const handleProductCurrentChange = (val: number) => {
  productCurrentPage.value = val
  getProductListData()
}

// 计算总金额
const calculateTotal = () => {
  const itemsTotal = createForm.orderItems.reduce((sum, item) => sum + item.totalAmount, 0)
  createForm.totalAmount = itemsTotal + createForm.freightAmount
  createForm.payAmount = createForm.totalAmount
}

// 监听商品数量和单价变化
const handleItemChange = (index: number) => {
  const item = createForm.orderItems[index]
  item.totalAmount = item.price * item.quantity
  calculateTotal()
}

// 删除商品
const removeOrderItem = (index: number) => {
  createForm.orderItems.splice(index, 1)
  calculateTotal()
}

// 提交创建订单
const submitCreate = async () => {
  if (!createFormRef.value) return
  await createFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await createOrder(createForm)
        ElMessage.success('创建订单成功')
        createDialogVisible.value = false
        getList()
      } catch (error: any) {
        ElMessage.error(error.response?.data?.message || '创建订单失败')
      }
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.order-container {
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

  .order-items,
  .delivery-info,
  .refund-info {
    margin-top: 20px;

    h3 {
      margin-bottom: 15px;
      font-weight: 500;
    }
  }

  .order-items {
    .order-item {
      margin-bottom: 15px;
      padding: 15px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      background-color: #fff;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      }

      .product-image {
        width: 80px;
        height: 80px;
        border-radius: 4px;
        border: 1px solid #ebeef5;
      }

      .product-info {
        .product-name {
          font-size: 14px;
          color: #303133;
          margin-bottom: 8px;
          line-height: 1.4;
        }

        .sku-name {
          font-size: 12px;
          color: #909399;
        }
      }

      .price, .total {
        font-size: 14px;
        color: #303133;
        line-height: 32px;
      }

      .total {
        color: #f56c6c;
        font-weight: bold;
      }

      .quantity-input {
        width: 120px;
      }
    }

    .add-product-btn {
      margin-top: 10px;
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .total-amount {
    font-size: 20px;
    color: #f56c6c;
    font-weight: bold;
  }

  .delivery-timeline {
    margin-top: 20px;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 4px;

    h4 {
      margin-bottom: 15px;
      font-weight: 500;
    }
  }

  .delivery-actions {
    margin-top: 20px;
    text-align: right;
  }

  .refund-actions {
    margin-top: 20px;
    text-align: right;
  }

  .proof-images {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 10px;

    .proof-image {
      width: 100px;
      height: 100px;
      border-radius: 4px;
      border: 1px solid #ebeef5;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      }
    }

    .image-error {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f5f7fa;
      color: #909399;
      font-size: 20px;
    }
  }
}
</style> 