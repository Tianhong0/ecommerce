<template>
  <view class="order-container">
    <!-- 订单状态切换 -->
    <view class="tab-box">
      <view 
        class="tab-item" 
        v-for="(item, index) in tabList" 
        :key="index"
        :class="{ active: currentTab === index }"
        @click="switchTab(index)"
      >
        {{ item.name }}
      </view>
    </view>
    
    <!-- 订单列表 -->
    <scroll-view class="order-list" scroll-y @scrolltolower="loadMore">
      <view 
        class="order-item" 
        v-for="(item, index) in orderList" 
        :key="index"
      >
        <view class="order-header">
          <text class="order-no">订单号：{{ item.orderNo }}</text>
          <text class="order-status">{{ getStatusText(item.status) }}</text>
        </view>
        
        <view 
          class="goods-item" 
          v-for="(goods, goodsIndex) in item.orderItems" 
          :key="goodsIndex"
          @click="navToOrderDetail(item.id)"
        >
          <image class="goods-image" :src="goods.productPic" mode="aspectFill"></image>
          <view class="goods-info">
            <text class="goods-name text-ellipsis-2">{{ goods.productName }}</text>
            <text class="goods-sku" v-if="goods.skuName">规格：{{ goods.skuName }}</text>
            <view class="goods-price-box">
              <text class="goods-price">¥{{ goods.price }}</text>
              <text class="goods-quantity">x{{ goods.quantity }}</text>
            </view>
          </view>
        </view>
        
        <view class="order-footer">
          <view class="order-total">
            <text>共{{ getTotalQuantity(item.orderItems) }}件商品 合计：</text>
            <text class="total-price">¥{{ item.payAmount }}</text>
          </view>
          
          <view class="order-actions">
            <view class="action-btn cancel-btn" v-if="item.status === 0" @click="cancelOrder(item.id)">取消订单</view>
            <view class="action-btn pay-btn" v-if="item.status === 0" @click="payOrder(item.id)">去支付</view>
            <view class="action-btn confirm-btn" v-if="item.status === 2" @click="confirmReceive(item.id)">确认收货</view>
            <view class="action-btn detail-btn" @click="navToOrderDetail(item.id)">订单详情</view>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="orderList.length === 0 && !loading">
        <image class="empty-image" src="/static/empty-order.png" mode="aspectFit"></image>
        <text class="empty-text">暂无相关订单</text>
      </view>
      
      <!-- 加载更多 -->
      <view class="loading" v-if="loading">
        <text>加载中...</text>
      </view>
      
      <!-- 没有更多数据 -->
      <view class="no-more" v-if="!hasMore && orderList.length > 0 && !loading">
        <text>没有更多数据了</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getOrderList, updateOrderStatus } from '@/api/order';

export default {
  data() {
    return {
      // 标签列表
      tabList: [
        { name: '全部', status: -1 },
        { name: '待付款', status: 0 },
        { name: '待发货', status: 1 },
        { name: '待收货', status: 2 },
        { name: '已完成', status: 3 }
      ],
      // 当前选中的标签
      currentTab: 0,
      // 订单列表
      orderList: [],
      // 分页参数
      pageNum: 1,
      pageSize: 10,
      // 是否有更多数据
      hasMore: true,
      // 是否正在加载
      loading: false
    };
  },
  onLoad() {
    this.loadOrderList();
  },
  methods: {
    // 切换标签
    switchTab(index) {
      if (this.currentTab === index) return;
      
      this.currentTab = index;
      this.orderList = [];
      this.pageNum = 1;
      this.hasMore = true;
      this.loadOrderList();
    },
    // 加载订单列表
    async loadOrderList() {
      if (!this.hasMore || this.loading) return;
      
      this.loading = true;
      
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        };
        
        // 添加状态筛选
        const status = this.tabList[this.currentTab].status;
        if (status !== -1) {
          params.status = status;
        }
        
        const res = await getOrderList(params);
        
        this.loading = false;
        
        if (res && res.data && res.data.records) {
          if (this.pageNum === 1) {
            // 第一页，直接赋值
            this.orderList = res.data.records;
          } else {
            // 非第一页，追加数据
            this.orderList = [...this.orderList, ...res.data.records];
          }
          
          // 判断是否还有更多数据
          this.hasMore = this.orderList.length < res.data.total;
          
          // 页码加1
          this.pageNum++;
        }
      } catch (error) {
        this.loading = false;
        console.error('加载订单列表失败', error);
        uni.showToast({
          title: '加载订单列表失败',
          icon: 'none'
        });
      }
    },
    // 加载更多
    loadMore() {
      this.loadOrderList();
    },
    // 获取订单状态文本
    getStatusText(status) {
      switch (status) {
        case 0:
          return '待付款';
        case 1:
          return '待发货';
        case 2:
          return '待收货';
        case 3:
          return '已完成';
        case 4:
          return '已取消';
        case 5:
          return '已退款';
        default:
          return '未知状态';
      }
    },
    // 获取订单商品总数量
    getTotalQuantity(orderItems) {
      return orderItems.reduce((total, item) => total + item.quantity, 0);
    },
    // 取消订单
    cancelOrder(id) {
      uni.showModal({
        title: '提示',
        content: '确定要取消该订单吗？',
        success: async res => {
          if (res.confirm) {
            try {
              uni.showLoading({
                title: '处理中...'
              });
              
              await updateOrderStatus(id, 4);
              
              uni.hideLoading();
              
              uni.showToast({
                title: '订单已取消',
                icon: 'success'
              });
              
              // 重新加载订单列表
              this.orderList = [];
              this.pageNum = 1;
              this.hasMore = true;
              this.loadOrderList();
            } catch (error) {
              uni.hideLoading();
              console.error('取消订单失败', error);
              uni.showToast({
                title: '取消订单失败',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    // 去支付
    payOrder(id) {
      // 模拟支付成功
      uni.showModal({
        title: '提示',
        content: '是否模拟支付成功？',
        success: async res => {
          if (res.confirm) {
            try {
              uni.showLoading({
                title: '支付中...'
              });
              
              await updateOrderStatus(id, 1);
              
              uni.hideLoading();
              
              uni.showToast({
                title: '支付成功',
                icon: 'success'
              });
              
              // 重新加载订单列表
              this.orderList = [];
              this.pageNum = 1;
              this.hasMore = true;
              this.loadOrderList();
            } catch (error) {
              uni.hideLoading();
              console.error('支付失败', error);
              uni.showToast({
                title: '支付失败',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    // 确认收货
    confirmReceive(id) {
      uni.showModal({
        title: '提示',
        content: '确定已收到商品？',
        success: async res => {
          if (res.confirm) {
            try {
              uni.showLoading({
                title: '处理中...'
              });
              
              await updateOrderStatus(id, 3);
              
              uni.hideLoading();
              
              uni.showToast({
                title: '确认收货成功',
                icon: 'success'
              });
              
              // 重新加载订单列表
              this.orderList = [];
              this.pageNum = 1;
              this.hasMore = true;
              this.loadOrderList();
            } catch (error) {
              uni.hideLoading();
              console.error('确认收货失败', error);
              uni.showToast({
                title: '确认收货失败',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    // 跳转到订单详情
    navToOrderDetail(id) {
      uni.navigateTo({
        url: `/pages/order-detail/index?id=${id}`
      });
    }
  }
};
</script>

<style lang="scss">
@import '@/static/styles/variables.scss';

.order-container {
  min-height: 100vh;
  background-color: $bg-color;
  display: flex;
  flex-direction: column;
  
  .tab-box {
    display: flex;
    background-color: #fff;
    height: 80rpx;
    border-bottom: 1rpx solid $border-lighter;
    
    .tab-item {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: $font-size-base;
      color: $text-regular;
      position: relative;
      
      &.active {
        color: $primary-color;
        font-weight: bold;
        
        &::after {
          content: '';
          position: absolute;
          left: 50%;
          bottom: 0;
          transform: translateX(-50%);
          width: 60rpx;
          height: 4rpx;
          background-color: $primary-color;
        }
      }
    }
  }
  
  .order-list {
    flex: 1;
    
    .order-item {
      margin: 20rpx;
      background-color: #fff;
      border-radius: $border-radius-medium;
      overflow: hidden;
      
      .order-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20rpx 30rpx;
        border-bottom: 1rpx solid $border-lighter;
        
        .order-no {
          font-size: $font-size-small;
          color: $text-secondary;
        }
        
        .order-status {
          font-size: $font-size-small;
          color: $primary-color;
        }
      }
      
      .goods-item {
        display: flex;
        padding: 20rpx 30rpx;
        border-bottom: 1rpx solid $border-lighter;
        
        .goods-image {
          width: 140rpx;
          height: 140rpx;
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
            }
            
            .goods-quantity {
              font-size: $font-size-base;
              color: $text-secondary;
            }
          }
        }
      }
      
      .order-footer {
        padding: 20rpx 30rpx;
        
        .order-total {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          margin-bottom: 20rpx;
          font-size: $font-size-base;
          color: $text-regular;
          
          .total-price {
            color: $primary-color;
            font-weight: bold;
          }
        }
        
        .order-actions {
          display: flex;
          justify-content: flex-end;
          
          .action-btn {
            padding: 10rpx 30rpx;
            border-radius: $border-radius-small;
            font-size: $font-size-small;
            margin-left: 20rpx;
          }
          
          .cancel-btn {
            color: $text-regular;
            border: 1rpx solid $border-color;
          }
          
          .pay-btn {
            color: #fff;
            background-color: $primary-color;
          }
          
          .confirm-btn {
            color: #fff;
            background-color: $success-color;
          }
          
          .detail-btn {
            color: $text-regular;
            border: 1rpx solid $border-color;
          }
        }
      }
    }
    
    .empty-state {
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
      }
    }
    
    .loading, .no-more {
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