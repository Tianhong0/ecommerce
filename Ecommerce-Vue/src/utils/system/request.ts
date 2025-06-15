import axios , { AxiosError, AxiosRequestConfig, AxiosResponse, AxiosInstance } from 'axios'
import store from '@/store'
import { ElMessage } from 'element-plus'

const service: AxiosInstance = axios.create({
  baseURL: '', // 使用相对路径，让 Vite 代理处理
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求前的统一处理
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 打印请求信息
    console.log('发送请求:', {
      url: config.url,
      method: config.method,
      data: config.data,
      headers: config.headers
    })

    // JWT鉴权处理
    const token = store.state.user.token
    if (token) {
      config.headers = config.headers || {}
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 打印响应信息
    console.log('收到响应:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    })

    const res = response.data
    // 根据success字段判断请求是否成功
    if (res.success) {
      return res
    } else {
      ElMessage.error(res.message || res.msg || '请求失败')
      return Promise.reject(res)
    }
  },
  (error: AxiosError)=> {
    // 详细打印错误信息
    console.error('请求错误详情：', {
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      config: error.config
    })
    
    // 处理401错误
    if (error.response?.status === 401) {
      store.dispatch('user/loginOut')
    }

    // 全局异常弹窗
    const msg = error.response?.data?.message || error.response?.data?.msg || error.message || '请求失败'
    ElMessage.error(msg)

    return Promise.reject(error)
  }
)

export default service