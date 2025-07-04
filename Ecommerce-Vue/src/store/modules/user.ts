import { loginApi, getInfoApi, loginOutApi, getCurrentUserInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/system/auth'
import { ElMessage } from 'element-plus'

export interface userState {
  token: string;
  name: string;
  avatar: string;
  roles: string[];
  nickname: string;
  email: string;
  phone: string;
  id: number | null;
}

const state: userState = {
  token: getToken(),
  name: '',
  avatar: '',
  roles: [],
  nickname: '',
  email: '',
  phone: '',
  id: null
}

const mutations = {
  SET_TOKEN: (state: userState, token: string) => {
    state.token = token
  },
  SET_NAME: (state: userState, name: string) => {
    state.name = name
  },
  SET_AVATAR: (state: userState, avatar: string) => {
    state.avatar = avatar
  },
  SET_ROLES: (state: userState, roles: string[]) => {
    state.roles = roles
  },
  SET_NICKNAME: (state: userState, nickname: string) => {
    state.nickname = nickname
  },
  SET_EMAIL: (state: userState, email: string) => {
    state.email = email
  },
  SET_PHONE: (state: userState, phone: string) => {
    state.phone = phone
  },
  SET_ID: (state: userState, id: number) => {
    state.id = id
  }
}

const actions = {
  // 用户登录
  login({ commit, dispatch }: any, userInfo: any) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      loginApi({
        username: username.trim(),
        password: password
      })
        .then((response: any) => {
          const { data, success, message } = response
          if (success) {
            commit('SET_TOKEN', data)
            setToken(data)
            // 登录成功后立即获取用户信息
            dispatch('getCurrentUserInfo')
            ElMessage.success({
              message: message || '登录成功',
              type: 'success',
              duration: 2000
            })
            resolve(response)
          } else {
            reject(new Error(message || '登录失败'))
          }
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取当前用户信息
  getCurrentUserInfo({ commit }: any) {
    return new Promise((resolve, reject) => {
      getCurrentUserInfo()
        .then((response: any) => {
          const { data, success } = response
          if (!success) {
            reject(new Error('获取用户信息失败'))
            return
          }
          
          const { username, nickname, email, phone, id, permissions, avatar } = data
          
          commit('SET_NAME', username)
          commit('SET_NICKNAME', nickname || username)
          commit('SET_EMAIL', email || '')
          commit('SET_PHONE', phone || '')
          commit('SET_ID', id)
          commit('SET_ROLES', permissions || [])
          commit('SET_AVATAR', avatar || '')
          
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取用户信息
  getInfo({ commit, state }: any) {
    return new Promise((resolve, reject) => {
      getInfoApi({ token: state.token })
        .then((response: any) => {
          const { data, success } = response
          if (!success) {
            reject(new Error('验证失败，请重新登录'))
            return
          }
          const { roles, name, avatar } = data
          if (!roles || roles.length <= 0) {
            reject(new Error('getInfo: 角色必须是非空数组!'))
            return
          }
          commit('SET_ROLES', roles)
          commit('SET_NAME', name)
          commit('SET_AVATAR', avatar)
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 用户登出
  loginOut({ commit }: any) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_NAME', '')
      commit('SET_NICKNAME', '')
      commit('SET_EMAIL', '')
      commit('SET_PHONE', '')
      commit('SET_ID', null)
      commit('SET_AVATAR', '')
      removeToken()
      ElMessage.success({
        message: '退出登录成功',
        type: 'success',
        duration: 2000
      })
      // 重定向到登录页
      window.location.href = '/#/login'
      resolve(null)
    })
  },

  // 移除 token
  resetToken({ commit }: any) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve(null)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
