/*
 * @Date: 2023-03-10 19:34:30
 * @Description: 
 */
import { RouteRecordRaw } from 'vue-router'
import { createNameComponent } from '../createRouter'

const routes: RouteRecordRaw[] = [

  {
    path: '/system',
    name: 'system',
    component: createNameComponent(() => import('@/views/main/system/index.vue')),
    meta: {
      title: 'message.menu.system.name',
      icon: 'icon-xitongshezhi',
    },
    children: [
   
     
    ],
  },
]

export default routes