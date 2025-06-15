import type { Route } from '../index.type'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'
const route: Route[] = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { title: 'message.menu.dashboard.index' },
    children: [
      {
        path: '/dashboard',
        component: createNameComponent(() => import('@/views/main/dashboard/index.vue')),
        meta: { title: 'message.menu.dashboard.index' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/404',
    hideMenu: true,
    meta: { title: 'message.menu.system.name' },
    children: [
      {
        path: '/system/404',
        component: createNameComponent(() => import('@/views/system/404.vue')),
        meta: { title: 'message.menu.system.404', hideTabs: true }
      },
      {
        path: '/system/401',
        component: createNameComponent(() => import('@/views/system/401.vue')),
        meta: { title: 'message.menu.system.401', hideTabs: true }
      },
      {
        path: '/system/redirect/:path(.*)',
        component: createNameComponent(() => import('@/views/main/system/redirect.vue')),
        meta: { title: 'message.menu.system.redirect', hideTabs: true }
      }
    ]
  },
 
  {
    path: '/login',
    component: createNameComponent(() => import('@/views/system/login.vue')),
    hideMenu: true,
    meta: { title: 'message.system.login', hideTabs: true }
  },
  {
    // 找不到路由重定向到404页面
    path: "/:pathMatch(.*)",
    component: Layout,
    redirect: "/system/404",
    hideMenu: true,
    meta: { title: '' },
    children: []
  },
]

export default route