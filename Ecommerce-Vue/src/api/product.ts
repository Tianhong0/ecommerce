import request from '@/utils/system/request'

// 获取商品列表
export const getProductList = (params: any) => {
  return request({
    url: '/product',
    method: 'get',
    params
  })
}

// 获取商品详情
export const getProductDetail = (id: string) => {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

// 创建商品
export const createProduct = (data: any) => {
  return request({
    url: '/product',
    method: 'post',
    data
  })
}

// 更新商品
export const updateProduct = (id: string, data: any) => {
  return request({
    url: `/product/${id}`,
    method: 'put',
    data
  })
}

// 删除商品
export const deleteProduct = (id: string) => {
  return request({
    url: `/product/${id}`,
    method: 'delete'
  })
}

// 更新商品状态
export const updateProductStatus = (id: string, status: number) => {
  return request({
    url: `/product/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 更新商品库存
export const updateProductStock = (id: string, stock: number) => {
  return request({
    url: `/product/${id}/stock`,
    method: 'put',
    data: { stock }
  })
}

// 获取分类列表
export const getCategoryList = (params?: any) => {
  return request({
    url: '/category',
    method: 'get',
    params
  })
}

// 创建分类
export const createCategory = (data: any) => {
  return request({
    url: '/category',
    method: 'post',
    data
  })
}

// 更新分类
export const updateCategory = (id: string, data: any) => {
  return request({
    url: `/category/${id}`,
    method: 'put',
    data
  })
}

// 删除分类
export const deleteCategory = (id: string) => {
  return request({
    url: `/category/${id}`,
    method: 'delete'
  })
}

// 更新分类状态
export const updateCategoryStatus = (id: string, status: number) => {
  return request({
    url: `/category/${id}/status`,
    method: 'put',
    data: { status }
  })
} 