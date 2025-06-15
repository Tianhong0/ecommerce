import { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'
const Orderoute: RouteRecordRaw = {
  
    path: '/order',
    component: Layout,
    meta: { title: 'message.menu.order.name', 
      icon: 'sfont system-order'
    },
    children: [
    
      {
        path: 'order',
        component: createNameComponent(() => import('@/views/main/order/list/index.vue')),
        meta: { title: 'message.menu.order.order' }
      },
     
    ]
  }


export default Orderoute