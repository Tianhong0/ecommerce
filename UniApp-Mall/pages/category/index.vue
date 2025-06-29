<template>
  <view class="category-container">
    <!-- 左侧分类菜单 -->
    <scroll-view class="category-menu" scroll-y>
      <view 
        class="menu-item" 
        v-for="(item, index) in categoryList" 
        :key="index"
        :class="{ active: currentCategoryIndex === index }"
        @click="switchCategory(index)"
      >
        {{ item.name }}
      </view>
    </scroll-view>
    
    <!-- 右侧商品列表 -->
    <scroll-view class="category-content" scroll-y @scrolltolower="loadMore">
      <view class="content-header">
        <text class="content-title">{{ currentCategory.name }}</text>
      </view>
      
      <!-- 商品列表 -->
      <view class="product-list" v-if="productList.length > 0">
        <view 
          class="product-item" 
          v-for="(item, index) in productList" 
          :key="index"
          @click="navToDetail(item.id)"
        >
          <image :src="item.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name text-ellipsis-2">{{ item.name }}</text>
            <text class="product-price">¥{{ item.price }}</text>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-else>
        <text>暂无商品</text>
      </view>
      
      <!-- 加载更多 -->
      <view class="loading" v-if="loading">
        <text>加载中...</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getCategoryList } from '@/api/category';
import { getProductList } from '@/api/product';

export default {
  data() {
    return {
      // 分类列表
      categoryList: [],
      // 当前选中的分类索引
      currentCategoryIndex: 0,
      // 商品列表
      productList: [],
      // 分页参数
      pageNum: 1,
      pageSize: 10,
      // 是否有更多数据
      hasMore: true,
      // 是否正在加载
      loading: false
    };
  },
  computed: {
    // 当前选中的分类
    currentCategory() {
      return this.categoryList[this.currentCategoryIndex] || {};
    }
  },
  onLoad() {
    this.loadCategories();
  },
  methods: {
    // 加载分类数据
    async loadCategories() {
      try {
        const res = await getCategoryList();
        if (res && res.data && res.data.records) {
          this.categoryList = res.data.records;
          // 加载第一个分类的商品
          if (this.categoryList.length > 0) {
            this.loadProducts();
          }
        }
      } catch (error) {
        console.error('加载分类失败', error);
        uni.showToast({
          title: '加载分类失败',
          icon: 'none'
        });
      }
    },
    // 加载商品数据
    async loadProducts() {
      if (!this.hasMore || this.loading) return;
      
      this.loading = true;
      
      try {
        const params = {
          page: this.pageNum,
          size: this.pageSize,
          categoryId: this.currentCategory.id
        };
        
        const res = await getProductList(params);
        
        if (res && res.data && res.data.records) {
          if (this.pageNum === 1) {
            // 第一页，直接赋值
            this.productList = res.data.records;
          } else {
            // 非第一页，追加数据
            this.productList = [...this.productList, ...res.data.records];
          }
          
          // 判断是否还有更多数据
          this.hasMore = this.productList.length < res.data.total;
          
          // 页码加1
          this.pageNum++;
        }
      } catch (error) {
        console.error('加载商品失败', error);
        uni.showToast({
          title: '加载商品失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    // 切换分类
    switchCategory(index) {
      if (this.currentCategoryIndex === index) return;
      
      this.currentCategoryIndex = index;
      this.productList = [];
      this.pageNum = 1;
      this.hasMore = true;
      this.loadProducts();
    },
    // 加载更多
    loadMore() {
      this.loadProducts();
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

.category-container {
  display: flex;
  height: 100vh;
  background-color: $bg-color;
  
  .category-menu {
    width: 200rpx;
    height: 100%;
    background-color: #f7f7f7;
    
    .menu-item {
      height: 100rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: $font-size-base;
      color: $text-regular;
      position: relative;
      
      &.active {
        color: $primary-color;
        background-color: #fff;
        font-weight: bold;
        
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 6rpx;
          height: 36rpx;
          background-color: $primary-color;
        }
      }
    }
  }
  
  .category-content {
    flex: 1;
    height: 100%;
    background-color: #fff;
    
    .content-header {
      padding: 30rpx;
      border-bottom: 1rpx solid $border-lighter;
      
      .content-title {
        font-size: $font-size-medium;
        font-weight: bold;
        color: $text-main;
      }
    }
    
    .product-list {
      display: flex;
      flex-wrap: wrap;
      padding: 20rpx;
      
      .product-item {
        width: 48%;
        margin-bottom: 20rpx;
        margin-right: 4%;
        background-color: #fff;
        border-radius: $border-radius-small;
        overflow: hidden;
        box-shadow: $box-shadow;
        
        &:nth-child(2n) {
          margin-right: 0;
        }
        
        .product-image {
          width: 100%;
          height: 180rpx;
        }
        
        .product-info {
          padding: 16rpx;
          
          .product-name {
            font-size: $font-size-base;
            color: $text-main;
            margin-bottom: 10rpx;
            height: 80rpx;
          }
          
          .product-price {
            font-size: $font-size-medium;
            color: $primary-color;
            font-weight: bold;
          }
        }
      }
    }
    
    .empty-state {
      padding: 100rpx 0;
      display: flex;
      align-items: center;
      justify-content: center;
      color: $text-secondary;
      font-size: $font-size-base;
    }
    
    .loading {
      padding: 30rpx 0;
      display: flex;
      align-items: center;
      justify-content: center;
      color: $text-secondary;
      font-size: $font-size-base;
    }
  }
}
</style> 