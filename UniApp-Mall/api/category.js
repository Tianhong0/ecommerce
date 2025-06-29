/**
 * 商品分类相关API
 */
import { get } from '../utils/request';

/**
 * 获取分类列表
 * @param {Object} params - 查询参数 {page, size, name}
 * @returns {Promise} 分类列表
 */
export const getCategoryList = (params) => {
  return get('/category', params);
};

/**
 * 获取分类详情
 * @param {Number} id - 分类ID
 * @returns {Promise} 分类详情
 */
export const getCategoryDetail = (id) => {
  return get(`/category/${id}`);
}; 