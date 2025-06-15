<template>
  <div class="redirect-container">
    <el-result
      icon="warning"
      title="页面重定向中"
      sub-title="正在跳转到目标页面，请稍候..."
    >
      <template #extra>
        <el-button type="primary" @click="goBack">返回上一页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { LocationQueryRaw } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  // 获取重定向目标路径
  const { path, query } = route.query
  if (path) {
    // 延迟跳转，让用户看到提示信息
    setTimeout(() => {
      const queryParams: LocationQueryRaw = {}
      if (query) {
        Object.entries(query).forEach(([key, value]) => {
          if (value) {
            queryParams[key] = value.toString()
          }
        })
      }
      router.replace({
        path: path as string,
        query: queryParams
      })
    }, 1000)
  } else {
    // 如果没有目标路径，返回首页
    router.replace('/')
  }
})
</script>

<style lang="scss" scoped>
.redirect-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}
</style> 