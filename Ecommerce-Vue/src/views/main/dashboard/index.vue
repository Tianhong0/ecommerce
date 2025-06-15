<template>
  <div class="dashboard-container">
    <!-- 欢迎信息 -->
    <el-card class="welcome-card">
      <div class="welcome-info">
        <div class="welcome-text">
          <h2>欢迎使用电商管理系统</h2>
          <p>今天是 {{ currentDate }}，祝您工作愉快！</p>
        </div>
        <div class="system-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="操作系统">{{ systemInfo.osName }} {{ systemInfo.osVersion }}</el-descriptions-item>
            <el-descriptions-item label="系统架构">{{ systemInfo.osArch }}</el-descriptions-item>
            <el-descriptions-item label="Java版本">{{ systemInfo.javaVersion }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ systemInfo.userName }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>

    <!-- 设备信息监控 -->
    <el-row :gutter="20" class="monitor-section">
      <el-col :span="8">
        <el-card class="monitor-card">
          <template #header>
            <div class="card-header">
              <span>CPU使用率</span>
              <el-tag :type="getStatusType(cpuInfo.cpuUsage)">{{ getStatusText(cpuInfo.cpuUsage) }}</el-tag>
            </div>
          </template>
          <div class="monitor-content">
            <el-progress 
              type="dashboard"
              :percentage="Number(cpuInfo.cpuUsage)"
              :color="getProgressColor(Number(cpuInfo.cpuUsage))"
            >
              <template #default="{ percentage }">
                <div class="progress-value">
                  <span class="value">{{ percentage }}%</span>
                  <span class="label">CPU使用率</span>
                </div>
              </template>
            </el-progress>
            <div class="monitor-details">
              <div class="detail-item">
                <span class="label">型号：</span>
                <span class="value">{{ cpuInfo.cpuModel }}</span>
              </div>
              <div class="detail-item">
                <span class="label">核心数：</span>
                <span class="value">{{ cpuInfo.cpuCores }}</span>
              </div>
              <div class="detail-item">
                <span class="label">线程数：</span>
                <span class="value">{{ cpuInfo.cpuThreads }}</span>
              </div>
              <div class="detail-item" v-if="cpuInfo.cpuTemperature">
                <span class="label">温度：</span>
                <span class="value">{{ cpuInfo.cpuTemperature }}°C</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="monitor-card">
          <template #header>
            <div class="card-header">
              <span>内存使用率</span>
              <el-tag :type="getStatusType(cpuInfo.memoryUsage)">{{ getStatusText(cpuInfo.memoryUsage) }}</el-tag>
            </div>
          </template>
          <div class="monitor-content">
            <el-progress 
              type="dashboard"
              :percentage="Number(cpuInfo.memoryUsage)"
              :color="getProgressColor(Number(cpuInfo.memoryUsage))"
            >
              <template #default="{ percentage }">
                <div class="progress-value">
                  <span class="value">{{ percentage }}%</span>
                  <span class="label">内存使用率</span>
                </div>
              </template>
            </el-progress>
            <div class="monitor-details">
              <div class="detail-item">
                <span class="label">总内存：</span>
                <span class="value">{{ cpuInfo.memoryTotal }} MB</span>
              </div>
              <div class="detail-item">
                <span class="label">已使用：</span>
                <span class="value">{{ cpuInfo.memoryUsed }} MB</span>
              </div>
              <div class="detail-item">
                <span class="label">可用：</span>
                <span class="value">{{ cpuInfo.memoryFree }} MB</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="monitor-card">
          <template #header>
            <div class="card-header">
              <span>磁盘使用率</span>
              <el-tag :type="getStatusType(cpuInfo.diskUsage)">{{ getStatusText(cpuInfo.diskUsage) }}</el-tag>
            </div>
          </template>
          <div class="monitor-content">
            <el-progress 
              type="dashboard"
              :percentage="Number(cpuInfo.diskUsage)"
              :color="getProgressColor(Number(cpuInfo.diskUsage))"
            >
              <template #default="{ percentage }">
                <div class="progress-value">
                  <span class="value">{{ percentage }}%</span>
                  <span class="label">磁盘使用率</span>
                </div>
              </template>
            </el-progress>
            <div class="monitor-details">
              <div class="detail-item">
                <span class="label">总容量：</span>
                <span class="value">{{ cpuInfo.diskTotal }} GB</span>
              </div>
              <div class="detail-item">
                <span class="label">已使用：</span>
                <span class="value">{{ cpuInfo.diskUsed }} GB</span>
              </div>
              <div class="detail-item">
                <span class="label">可用：</span>
                <span class="value">{{ cpuInfo.diskFree }} GB</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统公告 -->
    <el-card class="notice-card">
      <template #header>
        <div class="card-header">
          <span>系统公告</span>
          <el-button type="primary" link>更多</el-button>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(notice, index) in notices"
          :key="index"
          :timestamp="notice.time"
          :type="notice.type"
        >
          {{ notice.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 待办事项 -->
    <el-card class="todo-card">
      <template #header>
        <div class="card-header">
          <span>待办事项</span>
          <el-button type="primary" link>添加</el-button>
        </div>
      </template>
      <el-checkbox-group v-model="checkedTodos">
        <div v-for="(todo, index) in todoList" :key="index" class="todo-item">
          <el-checkbox :label="todo.id">
            <span :class="{ 'todo-done': todo.done }">{{ todo.content }}</span>
          </el-checkbox>
          <span class="todo-time">{{ todo.time }}</span>
        </div>
      </el-checkbox-group>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getSystemInfo, getCpuUsage } from '@/api/system'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

// 当前路由
const route = useRoute()

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})

// 系统信息
const systemInfo = ref({
  osName: '',
  osVersion: '',
  osArch: '',
  javaVersion: '',
  javaVendor: '',
  javaHome: '',
  userDir: '',
  userHome: '',
  userName: ''
})

// CPU和系统资源信息
const cpuInfo = ref({
  cpuModel: '',
  cpuCores: 0,
  cpuThreads: 0,
  cpuTemperature: null,
  cpuUsage: '0',
  memoryTotal: '0',
  memoryUsed: '0',
  memoryFree: '0',
  memoryUsage: '0',
  diskTotal: '0',
  diskUsed: '0',
  diskFree: '0',
  diskUsage: '0'
})

// 获取状态类型
const getStatusType = (value: string) => {
  const num = Number(value)
  if (num < 60) return 'success'
  if (num < 80) return 'warning'
  return 'danger'
}

// 获取状态文本
const getStatusText = (value: string) => {
  const num = Number(value)
  if (num < 60) return '正常'
  if (num < 80) return '警告'
  return '危险'
}

// 获取进度条颜色
const getProgressColor = (percentage: number) => {
  if (percentage < 60) return '#67C23A'
  if (percentage < 80) return '#E6A23C'
  return '#F56C6C'
}

// 获取系统信息
const fetchSystemInfo = async () => {
  try {
    const res = await getSystemInfo()
    if (res.data) {
      systemInfo.value = res.data
    }
  } catch (error) {
    console.error('获取系统信息失败:', error)
    ElMessage.error('获取系统信息失败')
  }
}

// 获取系统资源使用情况
const fetchSystemResources = async () => {
  try {
    const res = await getCpuUsage()
    if (res.data) {
      cpuInfo.value = res.data
    }
  } catch (error) {
    console.error('获取系统资源使用情况失败:', error)
    ElMessage.error('获取系统资源使用情况失败')
  }
}

// 系统公告
const notices = ref([
  {
    content: '系统将于本周六凌晨2点进行例行维护',
    time: '2024-03-20 10:00:00',
    type: 'warning'
  },
  {
    content: '新版本功能更新：新增订单导出功能',
    time: '2024-03-19 15:30:00',
    type: 'primary'
  },
  {
    content: '系统性能优化完成，运行更流畅',
    time: '2024-03-18 09:00:00',
    type: 'success'
  }
])

// 待办事项
const todoList = ref([
  {
    id: 1,
    content: '审核新上架商品',
    time: '今天 14:00',
    done: false
  },
  {
    id: 2,
    content: '处理退款申请',
    time: '今天 16:00',
    done: false
  },
  {
    id: 3,
    content: '更新商品库存',
    time: '明天 10:00',
    done: false
  }
])

const checkedTodos = ref<number[]>([])

// 定时器
let timer: number

onMounted(async () => {
  // 判断当前路由是否为首页
  const isDashboard = route.path === '/' || route.path === '/dashboard'
  
  if (isDashboard) {
    await fetchSystemInfo()
    await fetchSystemResources()
    timer = window.setInterval(fetchSystemResources, 3000)
  }
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .welcome-card {
    margin-bottom: 20px;

    .welcome-info {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .welcome-text {
        h2 {
          margin: 0 0 10px 0;
          font-size: 24px;
          color: #303133;
        }

        p {
          margin: 0;
          color: #606266;
        }
      }

      .system-info {
        width: 50%;
      }
    }
  }

  .monitor-section {
    margin-bottom: 20px;

    .monitor-card {
      .monitor-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;

        .progress-value {
          display: flex;
          flex-direction: column;
          align-items: center;

          .value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
          }

          .label {
            font-size: 14px;
            color: #909399;
            margin-top: 5px;
          }
        }

        .monitor-details {
          margin-top: 20px;
          width: 100%;

          .detail-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;

            .label {
              color: #606266;
            }

            .value {
              color: #303133;
              font-weight: 500;
            }
          }
        }
      }
    }
  }

  .notice-card {
    margin-bottom: 20px;
  }

  .todo-card {
    .todo-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #EBEEF5;

      &:last-child {
        border-bottom: none;
      }

      .todo-done {
        text-decoration: line-through;
        color: #909399;
      }

      .todo-time {
        color: #909399;
        font-size: 12px;
      }
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>