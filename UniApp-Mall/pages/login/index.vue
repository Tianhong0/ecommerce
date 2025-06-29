<template>
  <view class="login-container">
    <view class="login-header">
      <image class="logo" src="/static/logo.png" mode="aspectFit"></image>
      <text class="title">电商商城</text>
    </view>
    
    <view class="login-form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" type="text" v-model="loginForm.username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" type="password" v-model="loginForm.password" placeholder="请输入密码" password />
      </view>
      
      <button class="login-btn" @click="handleLogin">登录</button>
    </view>
  </view>
</template>

<script>
import { login } from '@/api/user';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    // 处理登录
    async handleLogin() {
      // 表单验证
      if (!this.loginForm.username || !this.loginForm.password) {
        uni.showToast({
          title: '用户名和密码不能为空',
          icon: 'none'
        });
        return;
      }
      
      try {
        uni.showLoading({
          title: '登录中...'
        });
        
        // 调用登录接口
        const res = await login(this.loginForm);
        
        uni.hideLoading();
        
        if (res && res.data) {
          // 保存token
          uni.setStorageSync('token', res.data);
          
          // 获取用户信息
          await this.$store.dispatch('getUserInfo');
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          });
          
          // 跳转到首页
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/index/index'
            });
          }, 1500);
        }
      } catch (error) {
        uni.hideLoading();
        uni.showToast({
          title: '登录失败，请检查用户名和密码',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.login-container {
  padding: 60rpx;
  height: 100vh;
  display: flex;
  flex-direction: column;
  
  .login-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 80rpx;
    margin-top: 60rpx;
    
    .logo {
      width: 160rpx;
      height: 160rpx;
      margin-bottom: 20rpx;
    }
    
    .title {
      font-size: 40rpx;
      font-weight: bold;
      color: $text-main;
    }
  }
  
  .login-form {
    .form-item {
      margin-bottom: 40rpx;
      
      .label {
        display: block;
        font-size: $font-size-medium;
        margin-bottom: 20rpx;
        color: $text-regular;
      }
      
      .input {
        width: 100%;
        height: 90rpx;
        border: 1rpx solid $border-color;
        border-radius: $border-radius-medium;
        padding: 0 30rpx;
        font-size: $font-size-medium;
        background-color: $bg-white;
      }
    }
    
    .login-btn {
      width: 100%;
      height: 90rpx;
      background-color: $primary-color;
      color: #fff;
      border-radius: $border-radius-medium;
      font-size: $font-size-medium;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 60rpx;
    }
  }
}
</style> 