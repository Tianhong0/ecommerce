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
      showError(res)
      return Promise.reject(res)
    }
  },
  (error: AxiosError)=> {
    console.error('请求错误详情：', {
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      config: error.config
    })
    
    if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.response?.status === 401) {
      // token过期或未授权
      store.dispatch('user/loginOut')
      ElMessage.error('登录已过期，请重新登录')
    } else if (error.message === 'Network Error') {
      ElMessage.error('网络错误，请检查后端服务是否正常运行')
    } else {
      const badMessage: any = error.message || error
      const code = parseInt(badMessage.toString().replace('Error: Request failed with status code ', ''))
      showError({ 
        code, 
        message: error.response?.data?.message || badMessage 
      })
    }
    return Promise.reject(error)
  }
)

// 错误处理
function showError(error: any) {
  // token过期，清除本地数据，并跳转至登录页面
  if (error.code === 401) {
    store.dispatch('user/loginOut')
    ElMessage.error('登录已过期，请重新登录')
  } else {
    ElMessage({
      message: error.msg || error.message || '服务异常',
      type: 'error',
      duration: 3 * 1000
    })
  }
}

export default service