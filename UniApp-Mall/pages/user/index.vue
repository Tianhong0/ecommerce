<template>
  <view class="user-container">
    <!-- 用户信息 -->
    <view class="user-info-box">
      <view class="user-info" v-if="userInfo">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
        <view class="info">
          <text class="nickname">{{ userInfo.nickname || userInfo.username }}</text>
          <text class="account">账号：{{ userInfo.username }}</text>
        </view>
      </view>
      <view class="not-login" v-else @click="goLogin">
        <image class="avatar" src="/static/default-avatar.png" mode="aspectFill"></image>
        <text class="login-text">点击登录</text>
      </view>
    </view>
    
    <!-- 我的订单 -->
    <view class="order-box">
      <view class="box-header" @click="navToOrderList(-1)">
        <text class="title">我的订单</text>
        <view class="more">
          <text>查看全部</text>
          <text class="iconfont icon-right"></text>
        </view>
      </view>
      <view class="order-nav">
        <view class="nav-item" @click="navToOrderList(0)">
          <view class="icon-box">
            <text class="iconfont icon-pay"></text>
            <text class="badge" v-if="orderCount.waitPay > 0">{{ orderCount.waitPay }}</text>
          </view>
          <text class="text">待付款</text>
        </view>
        <view class="nav-item" @click="navToOrderList(1)">
          <view class="icon-box">
            <text class="iconfont icon-send"></text>
            <text class="badge" v-if="orderCount.waitSend > 0">{{ orderCount.waitSend }}</text>
          </view>
          <text class="text">待发货</text>
        </view>
        <view class="nav-item" @click="navToOrderList(2)">
          <view class="icon-box">
            <text class="iconfont icon-receive"></text>
            <text class="badge" v-if="orderCount.waitReceive > 0">{{ orderCount.waitReceive }}</text>
          </view>
          <text class="text">待收货</text>
        </view>
        <view class="nav-item" @click="navToOrderList(3)">
          <view class="icon-box">
            <text class="iconfont icon-comment"></text>
          </view>
          <text class="text">已完成</text>
        </view>
      </view>
    </view>
    
    <!-- 我的服务 -->
    <view class="service-box">
      <view class="box-header">
        <text class="title">我的服务</text>
      </view>
      <view class="service-list">
        <view class="service-item" @click="navToAddress">
          <text class="iconfont icon-address"></text>
          <text class="text">收货地址</text>
        </view>
        <view class="service-item" @click="navToFavorite">
          <text class="iconfont icon-favorite"></text>
          <text class="text">我的收藏</text>
        </view>
        <view class="service-item" @click="navToHelp">
          <text class="iconfont icon-help"></text>
          <text class="text">帮助中心</text>
        </view>
        <view class="service-item" @click="navToFeedback">
          <text class="iconfont icon-feedback"></text>
          <text class="text">意见反馈</text>
        </view>
      </view>
    </view>
    
    <!-- 退出登录 -->
    <view class="logout-btn" v-if="userInfo" @click="logout">退出登录</view>
  </view>
</template>

<script>
import { mapState } from 'vuex';

export default {
  data() {
    return {
      // 订单数量
      orderCount: {
        waitPay: 0,
        waitSend: 0,
        waitReceive: 0
      }
    };
  },
  computed: {
    ...mapState(['userInfo'])
  },
  onShow() {
    // 如果已登录，获取订单数量
    if (this.userInfo) {
      this.getOrderCount();
    }
  },
  methods: {
    // 跳转到登录页
    goLogin() {
      uni.navigateTo({
        url: '/pages/login/index'
      });
    },
    // 获取订单数量
    getOrderCount() {
      // 模拟数据
      this.orderCount = {
        waitPay: 2,
        waitSend: 1,
        waitReceive: 3
      };
    },
    // 跳转到订单列表
    navToOrderList(status) {
      if (!this.userInfo) {
        this.goLogin();
        return;
      }
      
      uni.navigateTo({
        url: `/pages/order-list/index?status=${status}`
      });
    },
    // 跳转到收货地址
    navToAddress() {
      if (!this.userInfo) {
        this.goLogin();
        return;
      }
      
      uni.navigateTo({
        url: '/pages/address/list'
      });
    },
    // 跳转到我的收藏
    navToFavorite() {
      if (!this.userInfo) {
        this.goLogin();
        return;
      }
      
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      });
    },
    // 跳转到帮助中心
    navToHelp() {
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      });
    },
    // 跳转到意见反馈
    navToFeedback() {
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      });
    },
    // 退出登录
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: res => {
          if (res.confirm) {
            this.$store.commit('clearUserInfo');
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            });
          }
        }
      });
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.user-container {
  min-height: 100vh;
  background-color: $bg-color;
  
  .user-info-box {
    height: 300rpx;
    background-color: $primary-color;
    padding: 0 30rpx;
    display: flex;
    align-items: center;
    
    .user-info, .not-login {
      display: flex;
      align-items: center;
      
      .avatar {
        width: 120rpx;
        height: 120rpx;
        border-radius: 60rpx;
        margin-right: 30rpx;
        border: 4rpx solid rgba(255, 255, 255, 0.3);
      }
    }
    
    .user-info {
      .info {
        color: #fff;
        
        .nickname {
          font-size: $font-size-large;
          font-weight: bold;
          margin-bottom: 10rpx;
          display: block;
        }
        
        .account {
          font-size: $font-size-small;
          opacity: 0.8;
        }
      }
    }
    
    .not-login {
      .login-text {
        font-size: $font-size-medium;
        color: #fff;
        font-weight: bold;
      }
    }
  }
  
  .order-box, .service-box {
    margin: 20rpx;
    background-color: #fff;
    border-radius: $border-radius-medium;
    overflow: hidden;
    
    .box-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid $border-lighter;
      
      .title {
        font-size: $font-size-medium;
        color: $text-main;
        font-weight: bold;
      }
      
      .more {
        font-size: $font-size-small;
        color: $text-secondary;
        display: flex;
        align-items: center;
        
        .iconfont {
          margin-left: 10rpx;
          font-size: $font-size-small;
        }
      }
    }
  }
  
  .order-box {
    .order-nav {
      display: flex;
      padding: 30rpx 0;
      
      .nav-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .icon-box {
          position: relative;
          margin-bottom: 10rpx;
          
          .iconfont {
            font-size: 50rpx;
            color: $primary-color;
          }
          
          .badge {
            position: absolute;
            top: 10rpx;
            right: -80rpx;
            min-width: 32rpx;
            height: 32rpx;
            border-radius: 16rpx;
            background-color: $danger-color;
            color: #fff;
            font-size: $font-size-mini;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0 6rpx;
          }
        }
        
        .text {
          font-size: $font-size-small;
          color: $text-regular;
		  
        }
      }
    }
  }
  
  .service-box {
    .service-list {
      display: flex;
      flex-wrap: wrap;
      padding: 30rpx 0;
      
      .service-item {
        width: 25%;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 30rpx;
        
        .iconfont {
          font-size: 50rpx;
          color: $primary-color;
          margin-bottom: 10rpx;
        }
        
        .text {
          font-size: $font-size-small;
          color: $text-regular;
        }
      }
    }
  }
  
  .logout-btn {
    margin: 60rpx 30rpx;
    height: 90rpx;
    background-color: #fff;
    color: $danger-color;
    font-size: $font-size-medium;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: $border-radius-medium;
  }
}
</style> 