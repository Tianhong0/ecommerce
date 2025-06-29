<template>
  <view class="cart-container">
    <!-- 购物车内容 -->
    <view class="cart-content" v-if="cartList.length > 0">
      <view class="cart-list">
        <view 
          class="cart-item" 
          v-for="(item, index) in cartList" 
          :key="index"
        >
          <view class="check-box" @click="toggleItemChecked(item.id)">
            <text class="iconfont" :class="item.checked ? 'icon-checked' : 'icon-unchecked'"></text>
          </view>
          <image class="item-image" :src="item.mainImage" mode="aspectFill" @click="navToDetail(item.id)"></image>
          <view class="item-info">
            <text class="item-name text-ellipsis-2" @click="navToDetail(item.id)">{{ item.name }}</text>
            <text class="item-sku" v-if="item.skuName">规格：{{ item.skuName }}</text>
            <view class="item-bottom">
              <text class="item-price">¥{{ item.price }}</text>
              <view class="quantity-box">
                <view class="quantity-btn" @click="decreaseQuantity(item.id)">-</view>
                <input class="quantity-input" type="number" v-model="item.quantity" @input="updateQuantity(item.id, item.quantity)" />
                <view class="quantity-btn" @click="increaseQuantity(item.id)">+</view>
              </view>
            </view>
          </view>
          <view class="delete-btn" @click="removeFromCart(item.id)">
            <text class="iconfont icon-delete"></text>
          </view>
        </view>
      </view>
      
      <!-- 底部结算栏 -->
      <view class="settlement-bar">
        <view class="check-all" @click="toggleAllChecked">
          <text class="iconfont" :class="isAllChecked ? 'icon-checked' : 'icon-unchecked'"></text>
          <text class="check-text">全选</text>
        </view>
        <view class="total-box">
          <text class="total-label">合计：</text>
          <text class="total-price">¥{{ checkedCartTotal }}</text>
        </view>
        <view class="settle-btn" @click="settlement">
          <text>结算({{ checkedCartList.length }})</text>
        </view>
      </view>
    </view>
    
    <!-- 空购物车 -->
    <view class="empty-cart" v-else>
      <image class="empty-image" src="/static/empty-cart.png" mode="aspectFit"></image>
      <text class="empty-text">购物车还是空的</text>
      <view class="go-shopping-btn" @click="goShopping">去购物</view>
    </view>
  </view>
</template>

<script>
import { mapState, mapGetters } from 'vuex';

export default {
  computed: {
    ...mapState(['cartList']),
    ...mapGetters(['checkedCartList', 'checkedCartTotal', 'isAllChecked'])
  },
  methods: {
    // 切换商品选中状态
    toggleItemChecked(id) {
      const item = this.cartList.find(item => item.id === id);
      if (item) {
        this.$store.commit('updateCartChecked', { id, checked: !item.checked });
      }
    },
    // 切换全选状态
    toggleAllChecked() {
      this.$store.commit('toggleAllChecked', !this.isAllChecked);
    },
    // 减少商品数量
    decreaseQuantity(id) {
      const item = this.cartList.find(item => item.id === id);
      if (item && item.quantity > 1) {
        this.$store.commit('updateCartQuantity', { id, quantity: item.quantity - 1 });
      }
    },
    // 增加商品数量
    increaseQuantity(id) {
      const item = this.cartList.find(item => item.id === id);
      if (item) {
        this.$store.commit('updateCartQuantity', { id, quantity: item.quantity + 1 });
      }
    },
    // 更新商品数量
    updateQuantity(id, quantity) {
      const num = parseInt(quantity);
      if (!isNaN(num) && num > 0) {
        this.$store.commit('updateCartQuantity', { id, quantity: num });
      }
    },
    // 从购物车移除商品
    removeFromCart(id) {
      uni.showModal({
        title: '提示',
        content: '确定要移除该商品吗？',
        success: res => {
          if (res.confirm) {
            this.$store.commit('removeFromCart', id);
          }
        }
      });
    },
    // 结算
    settlement() {
      if (this.checkedCartList.length === 0) {
        uni.showToast({
          title: '请选择要结算的商品',
          icon: 'none'
        });
        return;
      }
      
      uni.navigateTo({
        url: '/pages/order-confirm/index'
      });
    },
    // 去购物
    goShopping() {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },
    // 跳转到商品详情
    navToDetail(id) {
      uni.navigateTo({
        url: `/pages/product-detail/index?id=${id}`
      });
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.cart-container {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 120rpx;
  
  .cart-content {
    .cart-list {
      background-color: #fff;
      
      .cart-item {
        display: flex;
        align-items: center;
        padding: 30rpx;
        position: relative;
        border-bottom: 1rpx solid $border-lighter;
        
        .check-box {
          padding: 20rpx;
          margin-right: 10rpx;
          
          .iconfont {
            font-size: 40rpx;
            
            &.icon-checked {
              color: $primary-color;
            }
            
            &.icon-unchecked {
              color: $text-placeholder;
            }
          }
        }
        
        .item-image {
          width: 120rpx;
          height: 120rpx;
          border-radius: $border-radius-small;
          margin-right: 20rpx;
        }
        
        .item-info {
          flex: 1;
          height: 120rpx;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          overflow: hidden;
          
          .item-name {
            font-size: $font-size-base;
            color: $text-main;
            line-height: 1.4;
            height: 2em;
          }
          
          .item-sku {
            font-size: $font-size-small;
            color: $text-secondary;
            margin-top: 10rpx;
            margin-bottom: 10rpx;
          }
          
          .item-bottom {
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            .item-price {
              font-size: $font-size-medium;
              color: $primary-color;
              font-weight: bold;
            }
            
            .quantity-box {
              display: flex;
              align-items: center;
              
              .quantity-btn {
                width: 60rpx;
                height: 60rpx;
                display: flex;
                align-items: center;
                justify-content: center;
                border: 1rpx solid $border-color;
                font-size: $font-size-medium;
              }
              
              .quantity-input {
                width: 80rpx;
                height: 60rpx;
                text-align: center;
                border-top: 1rpx solid $border-color;
                border-bottom: 1rpx solid $border-color;
              }
            }
          }
        }
        
        .delete-btn {
          position: absolute;
          right: 30rpx;
          top: 30rpx;
          padding: 10rpx;
          
          .iconfont {
            font-size: 36rpx;
            color: $text-secondary;
          }
        }
      }
    }
    
    .settlement-bar {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      height: 100rpx;
      background-color: #fff;
      display: flex;
      align-items: center;
      padding: 0 30rpx;
      box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
      
      .check-all {
        display: flex;
        align-items: center;
        
        .iconfont {
          font-size: 40rpx;
          margin-right: 10rpx;
          
          &.icon-checked {
            color: $primary-color;
          }
          
          &.icon-unchecked {
            color: $text-placeholder;
          }
        }
        
        .check-text {
          font-size: $font-size-base;
          color: $text-regular;
        }
      }
      
      .total-box {
        flex: 1;
        text-align: right;
        margin-right: 30rpx;
        
        .total-label {
          font-size: $font-size-base;
          color: $text-regular;
        }
        
        .total-price {
          font-size: $font-size-large;
          color: $primary-color;
          font-weight: bold;
        }
      }
      
      .settle-btn {
        width: 200rpx;
        height: 70rpx;
        background-color: $primary-color;
        color: #fff;
        font-size: $font-size-medium;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 35rpx;
      }
    }
  }
  
  .empty-cart {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding-top: 200rpx;
    
    .empty-image {
      width: 240rpx;
      height: 240rpx;
      margin-bottom: 30rpx;
    }
    
    .empty-text {
      font-size: $font-size-medium;
      color: $text-secondary;
      margin-bottom: 50rpx;
    }
    
    .go-shopping-btn {
      width: 240rpx;
      height: 80rpx;
      background-color: $primary-color;
      color: #fff;
      font-size: $font-size-medium;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 40rpx;
    }
  }
}
</style> 