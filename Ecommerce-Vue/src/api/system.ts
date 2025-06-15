import request from '@/utils/request'

// 获取系统信息
export function getSystemInfo() {
  return request({
    url: '/system/info',
    method: 'get'
  })
}

// 获取系统资源使用情况
export function getCpuUsage() {
  return request({
    url: '/system/cpu',
    method: 'get'
  })
}

// 获取内存使用情况
export function getMemoryUsage() {
  return request({
    url: '/system/memory',
    method: 'get'
  })
} 