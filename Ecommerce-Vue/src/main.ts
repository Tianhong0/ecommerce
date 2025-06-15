/*
 * @Date: 2022-05-22 20:44:25
 * @Description: 
 */
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import { baidu } from './utils/system/statistics'
import 'element-plus/theme-chalk/display.css' // 引入基于断点的隐藏类
import 'element-plus/dist/index.css'
import 'normalize.css' // css初始化
import './assets/style/common.scss' // 公共css
import './theme/modules/chinese/index.scss'
import App from './App.vue'
import store from './store'
import router from './router'
import { getAuthRoutes } from './router/permission'
import i18n from './locale'

if (import.meta.env.PROD) { // 非开发环境调用百度统计
  baidu()
}

/** 权限路由处理主方法 */
const app = createApp(App)

// 使用 Element Plus
app.use(ElementPlus)
app.use(store)
app.use(router)
app.use(i18n)

// 先挂载应用
app.mount('#app')

// 然后确保路由加载完成
getAuthRoutes().then(() => {
  console.log('应用初始化完成，路由已加载')
}).catch(err => {
  console.error('路由加载失败:', err)
})

