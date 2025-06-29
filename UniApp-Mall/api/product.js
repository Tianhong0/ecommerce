/**
 * 商品相关API
 */
import { get } from '../utils/request';

/**
 * 分页查询商品列表
 * @param {Object} params - 查询参数 {page, size, name, categoryId}
 * @returns {Promise} 商品列表
 */
export const getProductList = (params) => {
  return get('/product', params);
};

/**
 * 获取商品详情
 * @param {Number} id - 商品ID
 * @returns {Promise} 商品详情
 */
export const getProductDetail = (id) => {
  return get(`/product/${id}`);
}; 