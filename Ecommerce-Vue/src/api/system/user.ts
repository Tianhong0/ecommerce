import request from '@/utils/request'

// 用户角色类型
export interface UserRole {
  id: number;
  name: string;
}

// 用户列表项类型
export interface UserItem {
  id: string | number;
  username: string;
  nickname: string;
  email: string;
  phone: string;
  status: number;
  roleIds: number[];
  createTime: string;
}

// 获取用户列表
export function getUserList(params: {
  pageNum?: number;
  pageSize?: number;
  username?: string;
  nickname?: string;
  email?: string;
  phone?: string;
  status?: number;
}) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

// 创建用户
export function createUser(data: any) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id: string, data: any) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id: string) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

// 更新用户状态
export function updateUserStatus(id: string, status: number) {
  return request({
    url: `/users/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 获取用户角色
export function getUserRoles(id: string) {
  return request({
    url: `/users/${id}/roles`,
    method: 'get'
  })
}

// 分配用户角色
export function assignUserRoles(id: string, data: any) {
  return request({
    url: `/users/${id}/roles`,
    method: 'post',
    data
  })
}