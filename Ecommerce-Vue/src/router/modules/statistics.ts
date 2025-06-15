import { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'
const Statisticsroute: RouteRecordRaw = {
  
    path: '/statistics',
    component: Layout,
    meta: { title: 'message.menu.statistics.name', 
      icon: 'sfont system-order'
    },
    children: [
    
      {
        path: 'statistics',
        component: createNameComponent(() => import('@/views/main/statistics/index.vue')),
        meta: { title: 'message.menu.statistics.name' }
      },
     
    ]
  }


export default Statisticsroute