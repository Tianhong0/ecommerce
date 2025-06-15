<template>
  <div class="statistics-container">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总销售额</span>
            </div>
          </template>
          <div class="card-value">¥{{ formatNumber(statistics.totalSales) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总订单数</span>
            </div>
          </template>
          <div class="card-value">{{ formatNumber(statistics.totalOrderCount) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总商品数量</span>
            </div>
          </template>
          <div class="card-value">{{ formatNumber(statistics.totalProductCount) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总退款金额</span>
            </div>
          </template>
          <div class="card-value">¥{{ formatNumber(statistics.totalRefundAmount) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-container">
      <!-- 订单状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>订单状态分布</span>
            </div>
          </template>
          <div ref="orderStatusChart" class="chart"></div>
        </el-card>
      </el-col>
      <!-- 支付方式分布 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>支付方式分布</span>
            </div>
          </template>
          <div ref="payTypeChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-container">
      <!-- 支付方式金额分布 -->
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>支付方式金额分布</span>
            </div>
          </template>
          <div ref="payTypeAmountChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, reactive } from 'vue'
import * as echarts from 'echarts'
import { getOrderStatistics } from '@/api/order'

// 统计数据
const statistics = reactive({
  totalSales: 0,
  totalOrderCount: 0,
  totalProductCount: 0,
  totalRefundAmount: 0,
  totalRefundCount: 0,
  orderStatusCount: {},
  payTypeCount: {},
  payTypeAmount: {}
})

// 图表引用
const orderStatusChart = ref<HTMLElement>()
const payTypeChart = ref<HTMLElement>()
const payTypeAmountChart = ref<HTMLElement>()

// 图表实例
let orderStatusChartInstance: echarts.ECharts
let payTypeChartInstance: echarts.ECharts
let payTypeAmountChartInstance: echarts.ECharts

// 格式化数字
const formatNumber = (num: number) => {
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 获取统计数据
const getStatistics = async () => {
  try {
    const res = await getOrderStatistics()
    if (res.success) {
      Object.assign(statistics, res.data)
      updateCharts()
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 更新图表
const updateCharts = () => {
  // 更新订单状态分布图表
  const orderStatusData = Object.entries(statistics.orderStatusCount).map(([status, count]) => ({
    name: getOrderStatusText(Number(status)),
    value: count
  }))
  orderStatusChartInstance.setOption({
    title: {
      text: '订单状态分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: orderStatusData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 更新支付方式分布图表
  const payTypeData = Object.entries(statistics.payTypeCount).map(([type, count]) => ({
    name: getPayTypeText(Number(type)),
    value: count
  }))
  payTypeChartInstance.setOption({
    title: {
      text: '支付方式分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: payTypeData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 更新支付方式金额分布图表
  const payTypeAmountData = Object.entries(statistics.payTypeAmount).map(([type, amount]) => ({
    name: getPayTypeText(Number(type)),
    value: amount
  }))
  payTypeAmountChartInstance.setOption({
    title: {
      text: '支付方式金额分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: '{b}: ¥{c}'
    },
    xAxis: {
      type: 'category',
      data: payTypeAmountData.map(item => item.name)
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        type: 'bar',
        data: payTypeAmountData.map(item => item.value),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  })
}

// 获取订单状态文本
const getOrderStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消',
    5: '已退款'
  }
  return statusMap[status] || '未知'
}

// 获取支付方式文本
const getPayTypeText = (type: number) => {
  const typeMap: Record<number, string> = {
    1: '支付宝',
    2: '微信',
    3: '银联'
  }
  return typeMap[type] || '未知'
}

// 初始化图表
const initCharts = () => {
  if (orderStatusChart.value) {
    orderStatusChartInstance = echarts.init(orderStatusChart.value)
  }
  if (payTypeChart.value) {
    payTypeChartInstance = echarts.init(payTypeChart.value)
  }
  if (payTypeAmountChart.value) {
    payTypeAmountChartInstance = echarts.init(payTypeAmountChart.value)
  }
}

// 监听窗口大小变化
const handleResize = () => {
  orderStatusChartInstance?.resize()
  payTypeChartInstance?.resize()
  payTypeAmountChartInstance?.resize()
}

onMounted(() => {
  initCharts()
  getStatistics()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  orderStatusChartInstance?.dispose()
  payTypeChartInstance?.dispose()
  payTypeAmountChartInstance?.dispose()
})
</script>

<style lang="scss" scoped>
.statistics-container {
  padding: 20px;

  .data-overview {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-value {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
      text-align: center;
      padding: 10px 0;
    }
  }

  .charts-container {
    margin-bottom: 20px;

    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .chart {
        height: 400px;
      }
    }
  }
}
</style>