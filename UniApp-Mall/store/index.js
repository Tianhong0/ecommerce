import Vue from 'vue';
import Vuex from 'vuex';
import { getUserInfo } from '../api/user';

Vue.use(Vuex);

// 创建 store 实例
const store = new Vuex.Store({
  state: {
    // 用户信息
    userInfo: uni.getStorageSync('userInfo') || null,
    // 购物车
    cartList: uni.getStorageSync('cartList') || [],
    // 收货地址
    addressList: uni.getStorageSync('addressList') || []
  },
  mutations: {
    // 设置用户信息
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
      uni.setStorageSync('userInfo', userInfo);
    },
    // 清除用户信息
    clearUserInfo(state) {
      state.userInfo = null;
      uni.removeStorageSync('userInfo');
      uni.removeStorageSync('token');
    },
    // 添加商品到购物车
    addToCart(state, goods) {
      const index = state.cartList.findIndex(item => item.id === goods.id);
      if (index !== -1) {
        // 商品已存在，数量+1
        state.cartList[index].quantity += goods.quantity || 1;
      } else {
        // 商品不存在，添加到购物车
        state.cartList.push({
          ...goods,
          quantity: goods.quantity || 1,
          checked: true
        });
      }
      // 保存到本地存储
      uni.setStorageSync('cartList', state.cartList);
    },
    // 更新购物车商品数量
    updateCartQuantity(state, { id, quantity }) {
      const index = state.cartList.findIndex(item => item.id === id);
      if (index !== -1) {
        state.cartList[index].quantity = quantity;
        uni.setStorageSync('cartList', state.cartList);
      }
    },
    // 删除购物车商品
    removeFromCart(state, id) {
      state.cartList = state.cartList.filter(item => item.id !== id);
      uni.setStorageSync('cartList', state.cartList);
    },
    // 清空购物车
    clearCart(state) {
      state.cartList = [];
      uni.setStorageSync('cartList', []);
    },
    // 更新购物车商品选中状态
    updateCartChecked(state, { id, checked }) {
      const index = state.cartList.findIndex(item => item.id === id);
      if (index !== -1) {
        state.cartList[index].checked = checked;
        uni.setStorageSync('cartList', state.cartList);
      }
    },
    // 全选/取消全选
    toggleAllChecked(state, checked) {
      state.cartList.forEach(item => {
        item.checked = checked;
      });
      uni.setStorageSync('cartList', state.cartList);
    },
    // 添加收货地址
    addAddress(state, address) {
      state.addressList.push(address);
      uni.setStorageSync('addressList', state.addressList);
    },
    // 更新收货地址
    updateAddress(state, { index, address }) {
      state.addressList[index] = address;
      uni.setStorageSync('addressList', state.addressList);
    },
    // 删除收货地址
    removeAddress(state, index) {
      state.addressList.splice(index, 1);
      uni.setStorageSync('addressList', state.addressList);
    }
  },
  actions: {
    // 获取用户信息
    async getUserInfo({ commit }) {
      try {
        const res = await getUserInfo();
        if (res && res.data) {
          commit('setUserInfo', res.data);
          return res.data;
        }
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    }
  },
  getters: {
    // 购物车商品总数
    cartCount(state) {
      return state.cartList.reduce((total, item) => total + item.quantity, 0);
    },
    // 购物车选中商品
    checkedCartList(state) {
      return state.cartList.filter(item => item.checked);
    },
    // 购物车选中商品总价
    checkedCartTotal(state, getters) {
      return getters.checkedCartList.reduce((total, item) => {
        return total + item.price * item.quantity;
      }, 0).toFixed(2);
    },
    // 是否全选
    isAllChecked(state) {
      return state.cartList.length > 0 && state.cartList.every(item => item.checked);
    }
  }
});

export default store; 