import request from '@/utils/request'

// 获取角色列表
export function getRoleList(params: any) {
  return request({
    url: '/roles',
    method: 'get',
    params
  })
}

// 创建角色
export function createRole(data: any) {
  return request({
    url: '/roles',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(id: string, data: any) {
  return request({
    url: `/roles/${id}`,
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id: string) {
  return request({
    url: `/roles/${id}`,
    method: 'delete'
  })
}

// 获取角色权限
export function getRolePermissions(id: string) {
  return request({
    url: `/roles/${id}/permissions`,
    method: 'get'
  })
}

// 分配角色权限
export function assignRolePermissions(id: string, data: string[]) {
  return request({
    url: `/roles/${id}/permissions`,
    method: 'post',
    data
  })
}

// 获取权限树
export function getPermissionTree() {
  return request({
    url: '/permissions/tree',
    method: 'get'
  })
} 