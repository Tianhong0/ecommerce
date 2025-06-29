/**
 * 封装uni-app请求
 */

// 基础URL - 使用相对路径，通过代理转发请求
const baseURL = '/api';
// 直接请求的URL
const directURL = 'http://localhost:8080';

// 请求拦截器
const httpInterceptor = {
  invoke(options) {
    // 1. 拼接URL
    if (!options.url.startsWith('http')) {
      // 商品和分类相关接口直接请求后端
      if (options.url.includes('/product') || options.url.includes('/category')) {
        options.url = directURL + options.url;
      } else {
        options.url = baseURL + options.url;
      }
    }
    
    // 2. 请求超时
    options.timeout = 10000;
    
    // 3. 添加小程序端请求头标识
    options.header = {
      ...options.header,
      'source-client': 'miniapp'
    };
    
    // 4. 添加token请求头
    const token = uni.getStorageSync('token');
    if (token) {
      options.header.Authorization = `Bearer ${token}`;
    }
    
    // 5. 添加内容类型
    options.header['Content-Type'] = options.header['Content-Type'] || 'application/json';
  }
};

// 注册请求拦截器
uni.addInterceptor('request', httpInterceptor);
uni.addInterceptor('uploadFile', httpInterceptor);

/**
 * 请求函数
 * @param {Object} options - 请求配置
 * @returns {Promise} 请求结果
 */
export const http = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      ...options,
      success: (res) => {
        // 状态码2xx，请求成功
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          // 401错误, 清理用户信息，跳转登录页
          uni.removeStorageSync('token');
          uni.removeStorageSync('userInfo');
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          });
          setTimeout(() => {
            uni.navigateTo({ url: '/pages/login/index' });
          }, 1500);
          reject(res);
        } else {
          // 其他错误
          uni.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          });
          reject(res);
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络错误',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

/**
 * GET请求
 * @param {String} url - 请求地址
 * @param {Object} data - 请求参数
 * @returns {Promise} 请求结果
 */
export const get = (url, data) => {
  return http({
    method: 'GET',
    url,
    data
  });
};

/**
 * POST请求
 * @param {String} url - 请求地址
 * @param {Object} data - 请求参数
 * @returns {Promise} 请求结果
 */
export const post = (url, data) => {
  return http({
    method: 'POST',
    url,
    data
  });
};

/**
 * PUT请求
 * @param {String} url - 请求地址
 * @param {Object} data - 请求参数
 * @returns {Promise} 请求结果
 */
export const put = (url, data) => {
  return http({
    method: 'PUT',
    url,
    data
  });
};

/**
 * DELETE请求
 * @param {String} url - 请求地址
 * @param {Object} data - 请求参数
 * @returns {Promise} 请求结果
 */
export const del = (url, data) => {
  return http({
    method: 'DELETE',
    url,
    data
  });
}; 