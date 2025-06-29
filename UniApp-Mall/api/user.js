/**
 * 用户相关API
 */
import { get, post } from '../utils/request';

/**
 * 用户登录
 * @param {Object} data - 登录参数 {username, password}
 * @returns {Promise} 登录结果
 */
export const login = (data) => {
  return post('/auth/login', data);
};

/**
 * 获取用户信息
 * @returns {Promise} 用户信息
 */
export const getUserInfo = () => {
  return get('/auth/info');
};

/**
 * 获取用户权限
 * @returns {Promise} 用户权限列表
 */
export const getUserPermissions = () => {
  return get('/auth/permissions');
}; 