const Layout = () => import('@/layout/index.vue')

export default {
  path: '/user',
  component: Layout,
  redirect: '/user/profile',
  name: 'User',
  meta: {
    title: '用户中心',
    icon: 'user'
  },
  children: [
    {
      path: 'profile',
      name: 'UserProfile',
      component: () => import('@/views/main/user/profile/index.vue'),
      meta: {
        title: '个人信息',
        icon: 'profile'
      }
    }
  ]
} 