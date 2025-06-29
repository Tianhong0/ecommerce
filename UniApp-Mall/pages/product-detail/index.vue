<template>
  <view class="detail-container">
    <!-- 商品图片 -->
    <swiper class="product-swiper" indicator-dots autoplay circular :interval="3000" :duration="500">
      <swiper-item>
        <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
      </swiper-item>
    </swiper>
    
    <!-- 商品信息 -->
    <view class="product-info">
      <view class="price-box">
        <text class="price">¥{{ product.price }}</text>
        <text class="stock">库存: {{ product.stock }}</text>
      </view>
      <view class="name-box">
        <text class="name">{{ product.name }}</text>
      </view>
      <view class="desc-box">
        <text class="desc">{{ product.description || '暂无描述' }}</text>
      </view>
    </view>
    
    <!-- 商品规格 -->
    <view class="specs-box">
      <view class="specs-title">
        <text>商品规格</text>
      </view>
      <view class="specs-content">
        <view class="specs-item" v-if="product.skuList && product.skuList.length > 0">
          <text class="specs-label">选择规格</text>
          <view class="specs-values">
            <view 
              class="specs-value" 
              v-for="(item, index) in product.skuList" 
              :key="index"
              :class="{ active: selectedSkuIndex === index }"
              @click="selectSku(index)"
            >
              {{ item.name }}
            </view>
          </view>
        </view>
        <view class="specs-item">
          <text class="specs-label">数量</text>
          <view class="quantity-box">
            <view class="quantity-btn" @click="decreaseQuantity">-</view>
            <input class="quantity-input" type="number" v-model="quantity" />
            <view class="quantity-btn" @click="increaseQuantity">+</view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 商品详情 -->
    <view class="detail-box">
      <view class="detail-title">
        <text>商品详情</text>
      </view>
      <view class="detail-content">
        <rich-text :nodes="product.description || '暂无详情'"></rich-text>
      </view>
    </view>
    
    <!-- 底部操作栏 -->
    <view class="action-bar">
      <view class="action-btn-group">
        <view class="action-icon" @click="navToHome">
          <text class="iconfont icon-home"></text>
          <text class="icon-text">首页</text>
        </view>
        <view class="action-icon" @click="navToCart">
          <text class="iconfont icon-cart"></text>
          <text class="icon-text">购物车</text>
          <view class="badge" v-if="cartCount > 0">{{ cartCount }}</view>
        </view>
      </view>
      <view class="action-btn add-cart" @click="addToCart">加入购物车</view>
      <view class="action-btn buy-now" @click="buyNow">立即购买</view>
    </view>
  </view>
</template>

<script>
import { getProductDetail } from '@/api/product';
import { mapGetters } from 'vuex';

export default {
  data() {
    return {
      // 商品ID
      productId: null,
      // 商品详情
      product: {},
      // 选中的SKU索引
      selectedSkuIndex: 0,
      // 购买数量
      quantity: 1
    };
  },
  computed: {
    ...mapGetters(['cartCount']),
    // 当前选中的SKU
    selectedSku() {
      if (this.product.skuList && this.product.skuList.length > 0) {
        return this.product.skuList[this.selectedSkuIndex];
      }
      return null;
    }
  },
  onLoad(options) {
    this.productId = options.id;
    this.loadProductDetail();
  },
  methods: {
    // 加载商品详情
    async loadProductDetail() {
      try {
        uni.showLoading({
          title: '加载中...'
        });
        
        const res = await getProductDetail(this.productId);
        
        uni.hideLoading();
        
        if (res && res.data) {
          this.product = res.data;
        }
      } catch (error) {
        uni.hideLoading();
        console.error('加载商品详情失败', error);
        uni.showToast({
          title: '加载商品详情失败',
          icon: 'none'
        });
      }
    },
    // 选择SKU
    selectSku(index) {
      this.selectedSkuIndex = index;
    },
    // 减少数量
    decreaseQuantity() {
      if (this.quantity > 1) {
        this.quantity--;
      }
    },
    // 增加数量
    increaseQuantity() {
      const maxStock = this.selectedSku ? this.selectedSku.stock : this.product.stock;
      if (this.quantity < maxStock) {
        this.quantity++;
      } else {
        uni.showToast({
          title: '已达最大库存',
          icon: 'none'
        });
      }
    },
    // 添加到购物车
    addToCart() {
      // 检查是否登录
      const token = uni.getStorageSync('token');
      if (!token) {
        uni.navigateTo({
          url: '/pages/login/index'
        });
        return;
      }
      
      // 构建商品数据
      const goodsData = {
        id: this.product.id,
        name: this.product.name,
        mainImage: this.product.mainImage,
        price: this.selectedSku ? this.selectedSku.price : this.product.price,
        quantity: this.quantity,
        skuId: this.selectedSku ? this.selectedSku.id : null,
        skuName: this.selectedSku ? this.selectedSku.name : null
      };
      
      // 添加到购物车
      this.$store.commit('addToCart', goodsData);
      
      uni.showToast({
        title: '添加成功',
        icon: 'success'
      });
    },
    // 立即购买
    buyNow() {
      // 检查是否登录
      const token = uni.getStorageSync('token');
      if (!token) {
        uni.navigateTo({
          url: '/pages/login/index'
        });
        return;
      }
      
      // 构建商品数据
      const goodsData = {
        id: this.product.id,
        name: this.product.name,
        mainImage: this.product.mainImage,
        price: this.selectedSku ? this.selectedSku.price : this.product.price,
        quantity: this.quantity,
        skuId: this.selectedSku ? this.selectedSku.id : null,
        skuName: this.selectedSku ? this.selectedSku.name : null
      };
      
      // 跳转到确认订单页面
      uni.navigateTo({
        url: '/pages/order-confirm/index?direct=1'
      });
      
      // 将当前商品临时存储到本地，用于订单确认页面获取
      uni.setStorageSync('buyNowGoods', goodsData);
    },
    // 跳转到首页
    navToHome() {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },
    // 跳转到购物车
    navToCart() {
      uni.switchTab({
        url: '/pages/cart/index'
      });
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.detail-container {
  padding-bottom: 100rpx;
  background-color: $bg-color;
  
  .product-swiper {
    width: 100%;
    height: 500rpx;
    
    .product-image {
      width: 100%;
      height: 100%;
    }
  }
  
  .product-info {
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .price-box {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      
      .price {
        font-size: 48rpx;
        color: $primary-color;
        font-weight: bold;
      }
      
      .stock {
        font-size: $font-size-base;
        color: $text-secondary;
      }
    }
    
    .name-box {
      margin-bottom: 20rpx;
      
      .name {
        font-size: $font-size-large;
        color: $text-main;
        font-weight: bold;
        line-height: 1.4;
      }
    }
    
    .desc-box {
      .desc {
        font-size: $font-size-base;
        color: $text-regular;
        line-height: 1.5;
      }
    }
  }
  
  .specs-box {
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .specs-title {
      font-size: $font-size-medium;
      color: $text-main;
      font-weight: bold;
      margin-bottom: 30rpx;
    }
    
    .specs-content {
      .specs-item {
        margin-bottom: 30rpx;
        
        .specs-label {
          font-size: $font-size-base;
          color: $text-regular;
          margin-bottom: 20rpx;
          display: block;
        }
        
        .specs-values {
          display: flex;
          flex-wrap: wrap;
          
          .specs-value {
            padding: 10rpx 30rpx;
            border: 1rpx solid $border-color;
            border-radius: $border-radius-small;
            margin-right: 20rpx;
            margin-bottom: 20rpx;
            font-size: $font-size-base;
            color: $text-regular;
            
            &.active {
              border-color: $primary-color;
              color: $primary-color;
              background-color: rgba($primary-color, 0.05);
            }
          }
        }
        
        .quantity-box {
          display: flex;
          align-items: center;
          
          .quantity-btn {
            width: 70rpx;
            height: 70rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1rpx solid $border-color;
            font-size: $font-size-large;
          }
          
          .quantity-input {
            width: 100rpx;
            height: 70rpx;
            text-align: center;
            border-top: 1rpx solid $border-color;
            border-bottom: 1rpx solid $border-color;
          }
        }
      }
    }
  }
  
  .detail-box {
    background-color: #fff;
    padding: 30rpx;
    
    .detail-title {
      font-size: $font-size-medium;
      color: $text-main;
      font-weight: bold;
      margin-bottom: 30rpx;
    }
    
    .detail-content {
      font-size: $font-size-base;
      color: $text-regular;
      line-height: 1.6;
    }
  }
  
  .action-bar {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    height: 100rpx;
    background-color: #fff;
    display: flex;
    align-items: center;
    box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
    
    .action-btn-group {
      display: flex;
      width: 200rpx;
      height: 100%;
      
      .action-icon {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        position: relative;
        
        .iconfont {
          font-size: 40rpx;
          color: $text-regular;
        }
        
        .icon-text {
          font-size: $font-size-mini;
          color: $text-secondary;
          margin-top: 6rpx;
        }
        
        .badge {
          position: absolute;
          top: 10rpx;
          right: 20rpx;
          min-width: 32rpx;
          height: 32rpx;
          border-radius: 16rpx;
          background-color: $primary-color;
          color: #fff;
          font-size: $font-size-mini;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 0 6rpx;
        }
      }
    }
    
    .action-btn {
      flex: 1;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: $font-size-medium;
      color: #fff;
    }
    
    .add-cart {
      background-color: #ff9500;
    }
    
    .buy-now {
      background-color: $primary-color;
    }
  }
}
</style> 