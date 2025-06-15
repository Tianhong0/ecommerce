import express from 'express'
import systemRoutes from './system'

const router = express.Router()

// 注册系统监控路由
router.use('/system', systemRoutes)

export default router 