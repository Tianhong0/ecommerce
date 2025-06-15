<template>
  <el-scrollbar>
    <el-menu
      class="layout-menu system-scrollbar"
      background-color="var(--system-menu-background)"
      text-color="var(--system-menu-text-color)"
      active-text-color="var(--system-primary-color)"
      :mode="mode"
      :default-active="activeMenu"
      :class="isCollapse? 'collapse': ''"
      :collapse="isCollapse"
      :collapse-transition="false"
      :unique-opened="expandOneMenu"
    >
      <menu-item v-for="(menu, key) in filteredMenuRoutes" :key="key" :menu="menu" />
    </el-menu>
  </el-scrollbar>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted, ref, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import MenuItem from './MenuItem.vue'
export default defineComponent({
  props: {
    mode: {
      type: String,
      default: "vertical"
    }
  },
  components: {
    MenuItem
  },
  setup(props) {
    const { mode } = props
    const store = useStore()
    const router = useRouter()
    const isCollapse = computed(() => store.state.app.isCollapse)
    const expandOneMenu = computed(() => store.state.app.expandOneMenu)
    const route = useRoute()
    
    // 获取所有路由并过滤掉重复的路由和不需要显示的路由
    const getFilteredRoutes = () => {
      // 获取前端定义的路由
      const frontRoutes = router.getRoutes().filter(route => {
        // 过滤条件：
        // 1. 不是hideMenu为true的路由
        // 2. 不是通配符路由
        // 3. 不是/system路径下的路由（404、401等）
        // 4. 不是登录路由
        // 5. 不是特定的二级菜单路径
        if (route.meta?.hideMenu) return false
        if (route.path === '/:pathMatch(.*)') return false
        if (route.path === '/login') return false
        if (route.path === '/system') return false
        if (route.path.startsWith('/system/')) return false
        if (route.path === '/404') return false
        if (route.path === '/401') return false
        
        // 过滤掉特定的二级菜单和三级菜单路径
        if (route.path.includes('/menu-1')) return false
        if (route.path.includes('/menu-1-1')) return false
        
        // 过滤掉重定向页面菜单
        if (route.path.includes('/redirect')) return false
        
        // 过滤掉tab页面菜单
        if (route.path === '/tab' || route.path.startsWith('/tab/')) return false
        
        // 过滤掉没有名称的路由
        if (!route.name && !route.meta?.title) return false
        
        return true
      })
      
      // 专门处理首页路由，确保只有一个首页菜单
      const dashboardRoutes = frontRoutes.filter(route => 
        route.path === '/' || route.path === '/dashboard'
      )
      
      // 如果有多个首页路由，只保留一个（优先保留根路径'/'）
      let finalDashboardRoute = null
      if (dashboardRoutes.length > 0) {
        finalDashboardRoute = dashboardRoutes.find(route => route.path === '/') || dashboardRoutes[0]
      }
      
      // 过滤掉所有首页路由，然后添加回唯一的一个
      const nonDashboardRoutes = frontRoutes.filter(route => 
        route.path !== '/' && route.path !== '/dashboard'
      )
      
      const uniqueRoutes = []
      
      // 添加回唯一的首页路由（如果存在）
      if (finalDashboardRoute) {
        uniqueRoutes.push(finalDashboardRoute)
      }
      
      // 处理其他路由
      const processedPaths = new Set()
      nonDashboardRoutes.forEach(route => {
        // 提取主路径部分（例如从/pages/card提取/pages）
        const mainPath = '/' + route.path.split('/')[1]
        
        // 如果是一级路由且还没处理过，则添加到结果中
        if (!processedPaths.has(mainPath) && mainPath !== '/') {
          // 找到对应的一级路由
          const mainRoute = nonDashboardRoutes.find(r => r.path === mainPath) || route
          processedPaths.add(mainPath)
          uniqueRoutes.push(mainRoute)
        }
      })
      
      return uniqueRoutes
    }
    
    const menuRoutes = ref(getFilteredRoutes())
    
    // 将扁平的路由转换为树形结构
    const filteredMenuRoutes = computed(() => {
      return menuRoutes.value
    })
    
    // 监听路由变化，更新菜单
    watch(() => router.getRoutes().length, () => {
      console.log('路由数量变化，更新菜单:', router.getRoutes().length)
      menuRoutes.value = getFilteredRoutes()
    })
    
    const activeMenu: any = computed(() => {
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    });
    
    onMounted(() => {
      console.log('菜单组件挂载，当前路由数量:', router.getRoutes().length)
      // 确保菜单显示所有路由
      nextTick(() => {
        menuRoutes.value = getFilteredRoutes()
        console.log('菜单路由更新完成')
        // 等待下一个渲染周期，确保DOM已更新
        nextTick(() => {
          const menuElement = document.querySelector('.layout-menu')
          if (menuElement) {
            // 这里可以安全地访问DOM元素
            console.log('菜单元素已渲染')
          }
        })
      })
    })
    
    return {
      isCollapse,
      expandOneMenu,
      menuRoutes,
      filteredMenuRoutes,
      activeMenu,
      mode,
    }
  }
})
</script>

<style lang="scss" scoped>
  .el-scrollbar {
    background-color: var(--system-menu-background);
    flex: 1;
  }
  .layout-menu {
    width: 100%;
    border: none;
    &.collapse {
      margin-left: 0px;
    }
    :deep() {
      .el-menu-item, .el-sub-menu {
        background-color: var(--system-menu-background) !important;
      }
      .el-menu-item i, .el-menu-item-group__title, .el-sub-menu__title i {
        color: var(--system-menu-text-color);
      }
      .el-menu-item, .el-sub-menu__title{
        &.is-active {
          background-color: var(--system-primary-color) !important;
          color: var(--system-primary-text-color) !important;
          i {
            color: var(--system-primary-text-color) !important;
          }
          &:hover {
            background-color: var(--system-primary-color) !important;
            color: var(--system-primary-text-color) !important;
          }
        }
        &:hover {
          background-color: var(--system-menu-hover-background) !important;
        }
      }
      .el-sub-menu {
        &.is-active {
          >.el-sub-menu__title, >.el-sub-menu__title i {
            color: var(--system-menu-submenu-active-color) !important;
          }
        }
        .el-menu-item {
          background-color: var(--system-menu-children-background) !important;
          &.is-active {
            background-color: var(--system-primary-color) !important;
            color: var(--system-primary-text-color) !important;
            &:hover {
              background-color: var(--system-primary-color) !important;
              color: var(--system-primary-text-color) !important;
            }
          }
          &:hover {
            background-color: var(--system-menu-hover-background) !important;
          }
        }
        .el-sub-menu {
          .el-sub-menu__title {
            background-color: var(--system-menu-children-background) !important;
            &:hover {
              background-color: var(--system-menu-hover-background) !important;
            }
          }
        }
      }
    }
  }
</style>