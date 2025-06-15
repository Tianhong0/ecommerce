import { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'

const productRoutes: RouteRecordRaw = {
  path: '/product',
  component: Layout,
  meta: {
    title: 'message.menu.product.name',
    icon: 'sfont system-product'
  },
  children: [
    {
      path: 'category',
      name: 'ProductCategory',
      component: createNameComponent(() => import('@/views/main/product/category/index.vue')),
      meta: {
        title: 'message.menu.product.category'
      }
    },
    {
      path: 'product',
      name: 'ProductList',
      component: createNameComponent(() => import('@/views/main/product/product/index.vue')),
      meta: {
        title: 'message.menu.product.product'
      }
    }
  ]
}

export default productRoutes 