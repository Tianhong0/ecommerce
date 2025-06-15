import express from 'express'
import { getSystemInfo, getCpuUsage, getMemoryUsage } from '../controllers/systemController'

const router = express.Router()

// 获取系统信息
router.get('/info', getSystemInfo)

// 获取CPU使用率
router.get('/cpu', getCpuUsage)

// 获取内存使用情况
router.get('/memory', getMemoryUsage)

export default router 