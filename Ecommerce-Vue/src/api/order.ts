import request from '@/utils/request'

// 获取订单列表
export function getOrderList(params: any) {
  return request({
    url: '/orders/page',
    method: 'get',
    params: {
      pageNum: params.pageNum || 1,
      pageSize: params.pageSize || 10,
      orderNo: params.orderNo || '',
      status: params.status || ''
    }
  })
}

// 获取订单详情
export function getOrderDetail(id: string) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

// 创建订单
export function createOrder(data: {
  userId: string;
  totalAmount: number;
  payAmount: number;
  freightAmount: number;
  payType: number;
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
  note?: string;
  orderItems: Array<{
    productId: string;
    skuId: string;
    productName: string;
    skuName: string;
    productPic: string;
    price: number;
    quantity: number;
    totalAmount: number;
  }>;
}) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

// 更新订单状态
export function updateOrderStatus(id: string, data: { status: number }) {
  return request({
    url: `/orders/${id}/status`,
    method: 'put',
    params: {
      status: data.status
    }
  })
}

// 更新订单物流信息
export function updateOrderDelivery(id: string, data: any) {
  return request({
    url: `/orders/${id}/delivery`,
    method: 'put',
    data: {
      deliveryCompany: data.deliveryCompany,
      deliverySn: data.deliverySn,
      receiverName: data.receiverName,
      receiverPhone: data.receiverPhone,
      receiverAddress: data.receiverAddress,
      deliveryInfo: data.deliveryInfo,
      status: data.status || 0
    }
  })
}

// 更新订单物流状态
export function updateOrderDeliveryStatus(id: string, data: { status: number }) {
  return request({
    url: `/orders/${id}/delivery/status`,
    method: 'put',
    params: {
      status: data.status
    }
  })
}

// 处理订单退款
export function handleOrderRefund(id: string, data: {
  refundAmount: number;
  reason: string;
  description: string;
  proofImages: string;
}) {
  return request({
    url: `/orders/${id}/refund`,
    method: 'post',
    data: {
      refundAmount: data.refundAmount,
      reason: data.reason,
      description: data.description,
      proofImages: data.proofImages
    }
  })
}

// 处理退款
export function processOrderRefund(id: string, data: {
  status: number;
  rejectReason?: string;
}) {
  return request({
    url: `/orders/${id}/refund`,
    method: 'put',
    params: {
      status: data.status,
      rejectReason: data.rejectReason
    }
  })
}

// 删除订单
export function deleteOrder(id: string) {
  return request({
    url: `/orders/${id}`,
    method: 'delete'
  })
}

// 获取商品列表
export function getProductList(params: any) {
  return request({
    url: '/product',
    method: 'get',
    params: {
      pageNum: params.pageNum || 1,
      pageSize: params.pageSize || 10,
      name: params.name || '',
      status: params.status || ''
    }
  })
}

// 获取商品详情
export function getProductDetail(id: string) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

// 获取订单统计数据
export function getOrderStatistics() {
  return request({
    url: '/orders/statistics',
    method: 'get'
  })
}

