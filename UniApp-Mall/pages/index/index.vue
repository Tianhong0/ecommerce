<template>
  <view class="index-container">
    <!-- 搜索框 -->
    <view class="search-box">
      <view class="search-input" @click="goToSearch">
        <text class="iconfont icon-search"></text>
        <text class="placeholder">搜索商品</text>
      </view>
    </view>
    
    <!-- 轮播图 -->
    <swiper class="banner" indicator-dots autoplay circular :interval="3000" :duration="500">
      <swiper-item v-for="(item, index) in bannerList" :key="index">
        <image :src="item.imageUrl" mode="aspectFill" class="banner-image"></image>
      </swiper-item>
    </swiper>
    
    <!-- 分类导航 -->
    <view class="category-nav">
      <view 
        class="category-item" 
        v-for="(item, index) in categoryNavList" 
        :key="index"
        @click="navToCategory(item.id)"
      >
        <image :src="item.imageUrl" mode="aspectFit" class="category-icon"></image>
        <text class="category-name">{{ item.name }}</text>
      </view>
    </view>
    
    <!-- 热门商品 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">热门商品</text>
        <text class="section-more" @click="navToMore('hot')">查看更多</text>
      </view>
      <view class="product-list">
        <view 
          class="product-item" 
          v-for="(item, index) in hotProductList" 
          :key="index"
          @click="navToDetail(item.id)"
        >
          <image :src="item.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name text-ellipsis">{{ item.name }}</text>
            <text class="product-price">¥{{ item.price }}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 新品上市 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">新品上市</text>
        <text class="section-more" @click="navToMore('new')">查看更多</text>
      </view>
      <view class="product-list">
        <view 
          class="product-item" 
          v-for="(item, index) in newProductList" 
          :key="index"
          @click="navToDetail(item.id)"
        >
          <image :src="item.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name text-ellipsis">{{ item.name }}</text>
            <text class="product-price">¥{{ item.price }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getProductList } from '@/api/product';

export default {
  data() {
    return {
      // 轮播图数据
      bannerList: [
        { imageUrl: '/static/banner/banner1.png' },
        { imageUrl: '/static/banner/banner2.png' },
        { imageUrl: '/static/banner/banner3.png' }
      ],
      // 分类导航数据
      categoryNavList: [
        { id: 1, name: '手机数码', imageUrl: '/static/category/digital.png' },
        { id: 2, name: '服装鞋包', imageUrl: '/static/category/clothing.png' },
        { id: 3, name: '家用电器', imageUrl: '/static/category/appliance.png' },
        { id: 4, name: '美妆护肤', imageUrl: '/static/category/beauty.png' },
        { id: 5, name: '生鲜果蔬', imageUrl: '/static/category/food.png' }
      ],
      // 热门商品数据
      hotProductList: [],
      // 新品上市数据
      newProductList: []
    };
  },
  onLoad() {
    this.loadData();
  },
  onPullDownRefresh() {
    this.loadData();
    uni.stopPullDownRefresh();
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        // 加载热门商品
        const hotRes = await getProductList({ page: 1, size: 4 });
        if (hotRes && hotRes.data && hotRes.data.records) {
          this.hotProductList = hotRes.data.records;
        }
        
        // 加载新品上市
        const newRes = await getProductList({ page: 1, size: 4 });
        if (newRes && newRes.data && newRes.data.records) {
          this.newProductList = newRes.data.records;
        }
      } catch (error) {
        console.error('加载数据失败', error);
        uni.showToast({
          title: '加载数据失败',
          icon: 'none'
        });
      }
    },
    // 跳转到搜索页
    goToSearch() {
      uni.navigateTo({
        url: '/pages/search/index'
      });
    },
    // 跳转到分类页
    navToCategory(id) {
      uni.switchTab({
        url: '/pages/category/index'
      });
    },
    // 查看更多
    navToMore(type) {
      uni.switchTab({
        url: '/pages/category/index'
      });
    },
    // 跳转到商品详情页
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

.index-container {
  min-height: 100vh;
  background-color: $bg-color;
  
  .search-box {
    padding: 20rpx 30rpx;
    background-color: $primary-color;
    
    .search-input {
      height: 70rpx;
      background-color: #fff;
      border-radius: 35rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .icon-search {
        font-size: $font-size-medium;
        color: $text-secondary;
        margin-right: 10rpx;
      }
      
      .placeholder {
        font-size: $font-size-base;
        color: $text-placeholder;
      }
    }
  }
  
  .banner {
    height: 250rpx;
    
    .banner-image {
      width: 100%;
      height: 100%;
    }
  }
  
  .category-nav {
    display: flex;
    justify-content: space-between;
    padding: 30rpx;
    background-color: #fff;
    
    .category-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 20%;
      
      .category-icon {
        width: 80rpx;
        height: 80rpx;
        margin-bottom: 10rpx;
      }
      
      .category-name {
        font-size: $font-size-small;
        color: $text-regular;
      }
    }
  }
  
  .section {
    margin-top: 20rpx;
    background-color: #fff;
    padding: 20rpx 30rpx;
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      
      .section-title {
        font-size: $font-size-medium;
        font-weight: bold;
        color: $text-main;
      }
      
      .section-more {
        font-size: $font-size-small;
        color: $text-secondary;
      }
    }
    
    .product-list {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      
      .product-item {
        width: 48%;
        background-color: #fff;
        margin-bottom: 20rpx;
        border-radius: $border-radius-small;
        overflow: hidden;
        box-shadow: $box-shadow;
        
        .product-image {
          width: 100%;
          height: 200rpx;
	
        }
        
        .product-info {
          padding: 16rpx;
          
          .product-name {
            font-size: $font-size-base;
            color: $text-main;
            margin-bottom: 10rpx;
          }
          
          .product-price {
            font-size: $font-size-medium;
            color: $primary-color;
            font-weight: bold;
          }
        }
      }
    }
  }
}
</style> 