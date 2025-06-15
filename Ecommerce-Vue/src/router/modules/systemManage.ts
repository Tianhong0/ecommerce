import { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'
const SystemManageroute: RouteRecordRaw = {
  
    path: '/systemManage',
    component: Layout,
    meta: { title: 'message.menu.systemManage.name', 
      icon: '@/assets/icon/system.png'
    },
    children: [
    
      {
        path: 'role',
        component: createNameComponent(() => import('@/views/main/systemManage/role/index.vue')),
        meta: { title: 'message.menu.systemManage.role' }
      },
      {
        path: 'user',
        component: createNameComponent(() => import('@/views/main/systemManage/users/index.vue')),
        meta: { title: 'message.menu.systemManage.user' }
      }
    ]
  }


export default SystemManageroute