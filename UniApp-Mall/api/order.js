/**
 * 订单相关API
 */
import { get, post, put } from '../utils/request';

/**
 * 创建订单
 * @param {Object} data - 订单数据
 * @returns {Promise} 创建结果
 */
export const createOrder = (data) => {
  return post('/orders', data);
};

/**
 * 获取订单列表
 * @param {Object} params - 查询参数 {pageNum, pageSize, orderNo, status}
 * @returns {Promise} 订单列表
 */
export const getOrderList = (params) => {
  return get('/orders/page', params);
};

/**
 * 获取订单详情
 * @param {Number} id - 订单ID
 * @returns {Promise} 订单详情
 */
export const getOrderDetail = (id) => {
  return get(`/orders/${id}`);
};

/**
 * 更新订单状态
 * @param {Number} id - 订单ID
 * @param {Number} status - 订单状态
 * @returns {Promise} 更新结果
 */
export const updateOrderStatus = (id, status) => {
  return put(`/orders/${id}/status?status=${status}`);
}; 