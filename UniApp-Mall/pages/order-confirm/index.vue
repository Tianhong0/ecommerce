<template>
  <view class="confirm-container">
    <!-- 收货地址 -->
    <view class="address-box" @click="chooseAddress">
      <view class="address-info" v-if="address">
       
      </view>
      <view class="no-address" v-else>
		  <input v-model="info.address" placeholder="请填写收货地址"  />
		  <hr />
		  <br />
		   <input v-model="info.name" placeholder="请填写收货人"  />
		   <hr />
		   <br />
		    <input v-model="info.phone" placeholder="请填写电话"  />
			<hr />
			<br />
      </view>
      <text class="iconfont icon-right"></text>
    </view>
    
    <!-- 商品列表 -->
    <view class="goods-box">
      <view class="goods-title">
        <text>商品清单</text>
      </view>
      <view class="goods-list">
        <view 
          class="goods-item" 
          v-for="(item, index) in goodsList" 
          :key="index"
        >
          <image class="goods-image" :src="item.mainImage" mode="aspectFill"></image>
          <view class="goods-info">
            <text class="goods-name text-ellipsis-2">{{ item.name }}</text>
            <text class="goods-sku" v-if="item.skuName">规格：{{ item.skuName }}</text>
            <view class="goods-price-box">
              <text class="goods-price">¥{{ item.price }}</text>
              <text class="goods-quantity">x{{ item.quantity }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 配送方式 -->
    <view class="delivery-box">
      <view class="delivery-title">
        <text>配送方式</text>
      </view>
      <view class="delivery-content">
        <text>快递配送</text>
        <text class="delivery-fee">¥{{ deliveryFee }}</text>
      </view>
    </view>
    
    <!-- 订单备注 -->
    <view class="remark-box">
      <view class="remark-title">
        <text>订单备注</text>
      </view>
      <view class="remark-content">
        <input class="remark-input" type="text" v-model="remark" placeholder="选填，请先和商家协商一致" />
      </view>
    </view>
    
    <!-- 订单金额 -->
    <view class="amount-box">
      <view class="amount-item">
        <text>商品金额</text>
        <text>¥{{ goodsAmount }}</text>
      </view>
      <view class="amount-item">
        <text>运费</text>
        <text>¥{{ deliveryFee }}</text>
      </view>
      <view class="amount-total">
        <text>实付金额</text>
        <text class="total-price">¥{{ totalAmount }}</text>
      </view>
    </view>
    
    <!-- 底部结算栏 -->
    <view class="settlement-bar">
      <view class="total-box">
        <text>合计：</text>
        <text class="total-price">¥{{ totalAmount }}</text>
      </view>
      <view class="submit-btn" @click="submitOrder">提交订单</view>
    </view>
  </view>
</template>

<script>
import { createOrder } from '@/api/order';

export default {
  data() {
    return {
		info:{
			name: null,
			phone: null,
			// 收货地址
			address: null,
		},
	
      // 是否直接购买
      isDirect: false,
      // 商品列表
      goodsList: [],

      // 订单备注
      remark: '',
      // 运费
      deliveryFee: 0
    };
  },
  computed: {
    // 商品总金额
    goodsAmount() {
      return this.goodsList.reduce((total, item) => {
        return total + item.price * item.quantity;
      }, 0).toFixed(2);
    },
    // 订单总金额
    totalAmount() {
      return (parseFloat(this.goodsAmount) + parseFloat(this.deliveryFee)).toFixed(2);
    }
  },
  onLoad(options) {
    // 判断是否直接购买
    this.isDirect = options.direct === '1';
    
    // 获取商品数据
    this.getGoodsList();
    
    // // 获取收货地址
    // this.getAddress();
  },
  methods: {
    // 获取商品数据
    getGoodsList() {
      if (this.isDirect) {
        // 直接购买
        const buyNowGoods = uni.getStorageSync('buyNowGoods');
        if (buyNowGoods) {
          this.goodsList = [buyNowGoods];
        }
      } else {
        // 购物车结算
        const cartList = this.$store.getters.checkedCartList;
        this.goodsList = JSON.parse(JSON.stringify(cartList));
      }
    },
    // 获取收货地址
    // getAddress() {
    //   const addressList = uni.getStorageSync('addressList');
    //   if (addressList && addressList.length > 0) {
    //     // 获取默认地址或第一个地址
    //     const defaultAddress = addressList.find(item => item.isDefault);
    //     this.address = defaultAddress || addressList[0];
    //   }
    // },
    // 选择收货地址
    // chooseAddress() {
    //   uni.navigateTo({
    //     url: '/pages/address/list'
    //   });
    // },
    // 提交订单
    async submitOrder() {
      // 验证收货地址
      // if (!this.address) {
      //   uni.showToast({
      //     title: '请选择收货地址',
      //     icon: 'none'
      //   });
      //   return;
      // }
      
      try {
        uni.showLoading({
          title: '提交中...'
        });
        
        // 构建订单数据
        const orderData = {
          userId: this.$store.state.userInfo.id,
          totalAmount: parseFloat(this.goodsAmount),
          payAmount: parseFloat(this.totalAmount),
          freightAmount: parseFloat(this.deliveryFee),
          payType: 1, // 1-支付宝 2-微信 3-银联
          receiverName: this.info.name,
          receiverPhone: this.info.phone,
          receiverAddress: this.info.address,
          note: this.remark,
          orderItems: this.goodsList.map(item => ({
            productId: item.id,
            skuId: item.skuId || null,
            productName: item.name,
            skuName: item.skuName || '',
            productPic: item.mainImage,
            price: item.price,
            quantity: item.quantity,
            totalAmount: item.price * item.quantity
          }))
        };
        
        // 调用创建订单接口
        const res = await createOrder(orderData);
        
        uni.hideLoading();
        
        if (res && res.data) {
          // 清空购物车中已结算的商品
          if (!this.isDirect) {
            this.goodsList.forEach(item => {
              this.$store.commit('removeFromCart', item.id);
            });
          }
          
          // 清除直接购买的临时数据
          uni.removeStorageSync('buyNowGoods');
          
          // 显示成功提示
          uni.showToast({
            title: '订单提交成功',
            icon: 'success',
            duration: 2000
          });
          
          // 等待提示显示完成后跳转到订单列表
          setTimeout(() => {
            uni.navigateTo({
              url: '/pages/order-list/index'
            });
          }, 1500);
        }
      } catch (error) {
        uni.hideLoading();
        console.error('提交订单失败', error);
        uni.showToast({
          title: '提交订单失败',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.confirm-container {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 120rpx;
  
  .address-box {
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;
    position: relative;
    
    .address-info {
      flex: 1;
      
      .user-info {
        margin-bottom: 10rpx;
        
        .name {
          font-size: $font-size-medium;
          color: $text-main;
          font-weight: bold;
          margin-right: 20rpx;
        }
        
        .phone {
          font-size: $font-size-base;
          color: $text-regular;
        }
      }
      
      .address-detail {
        font-size: $font-size-base;
        color: $text-regular;
        line-height: 1.4;
      }
    }
    
    .no-address {
      flex: 1;
      font-size: $font-size-medium;
      color: $text-regular;
    }
    
    .iconfont {
      font-size: 40rpx;
      color: $text-secondary;
      margin-left: 20rpx;
    }
    
    &::after {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      height: 6rpx;
      background-image: linear-gradient(45deg, #ff6700 25%, transparent 25%, transparent 50%, #ff6700 50%, #ff6700 75%, transparent 75%, transparent);
      background-size: 20rpx 20rpx;
    }
  }
  
  .goods-box {
    background-color: #fff;
    margin-bottom: 20rpx;
    
    .goods-title {
      padding: 30rpx;
      border-bottom: 1rpx solid $border-lighter;
      font-size: $font-size-medium;
      color: $text-main;
      font-weight: bold;
    }
    
    .goods-list {
      .goods-item {
        display: flex;
        padding: 30rpx;
        border-bottom: 1rpx solid $border-lighter;
        
        .goods-image {
          width: 160rpx;
          height: 160rpx;
          border-radius: $border-radius-small;
          margin-right: 20rpx;
        }
        
        .goods-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          
          .goods-name {
            font-size: $font-size-base;
            color: $text-main;
            line-height: 1.4;
            height: 2.8em;
          }
          
          .goods-sku {
            font-size: $font-size-small;
            color: $text-secondary;
            margin-top: 10rpx;
          }
          
          .goods-price-box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            .goods-price {
              font-size: $font-size-medium;
              color: $primary-color;
              font-weight: bold;
            }
            
            .goods-quantity {
              font-size: $font-size-base;
              color: $text-secondary;
            }
          }
        }
      }
    }
  }
  
  .delivery-box {
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .delivery-title {
      font-size: $font-size-medium;
      color: $text-main;
      font-weight: bold;
      margin-bottom: 20rpx;
    }
    
    .delivery-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: $font-size-base;
      color: $text-regular;
      
      .delivery-fee {
        color: $primary-color;
      }
    }
  }
  
  .remark-box {
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .remark-title {
      font-size: $font-size-medium;
      color: $text-main;
      font-weight: bold;
      margin-bottom: 20rpx;
    }
    
    .remark-content {
      .remark-input {
        width: 100%;
        height: 80rpx;
        border: 1rpx solid $border-color;
        border-radius: $border-radius-small;
        padding: 0 20rpx;
        font-size: $font-size-base;
      }
    }
  }
  
  .amount-box {
    background-color: #fff;
    padding: 30rpx;
    
    .amount-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      font-size: $font-size-base;
      color: $text-regular;
    }
    
    .amount-total {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: $font-size-medium;
      color: $text-main;
      padding-top: 20rpx;
      border-top: 1rpx solid $border-lighter;
      
      .total-price {
        color: $primary-color;
        font-weight: bold;
        font-size: $font-size-large;
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
    padding-left: 30rpx;
    box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
    
    .total-box {
      flex: 1;
      font-size: $font-size-medium;
      color: $text-main;
      
      .total-price {
        color: $primary-color;
        font-weight: bold;
        font-size: $font-size-large;
      }
    }
    
    .submit-btn {
      width: 240rpx;
      height: 100rpx;
      background-color: $primary-color;
      color: #fff;
      font-size: $font-size-medium;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style> 