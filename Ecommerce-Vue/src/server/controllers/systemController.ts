import { Request, Response } from 'express'
import os from 'os'
import osUtils from 'node-os-utils'
import { exec } from 'child_process'
import { promisify } from 'util'

const execAsync = promisify(exec)
const cpu = osUtils.cpu
const mem = osUtils.mem
const drive = osUtils.drive

// 获取系统信息
export const getSystemInfo = async (req: Request, res: Response) => {
  try {
    // 获取Java信息
    let javaVersion = ''
    let javaVendor = ''
    let javaHome = ''
    try {
      const { stdout } = await execAsync('java -version')
      const versionMatch = stdout.match(/version "([^"]+)"/)
      if (versionMatch) {
        javaVersion = versionMatch[1]
      }
      javaHome = process.env.JAVA_HOME || ''
      javaVendor = 'Oracle Corporation' // 默认值，实际应该从java -version输出中解析
    } catch (error) {
      console.warn('获取Java信息失败:', error)
    }

    res.json({
      success: true,
      message: '查询成功',
      data: {
        osName: os.type(),
        osVersion: os.release(),
        osArch: os.arch(),
        javaVersion,
        javaVendor,
        javaHome,
        userDir: process.cwd(),
        userHome: os.homedir(),
        userName: os.userInfo().username
      }
    })
  } catch (error) {
    console.error('获取系统信息失败:', error)
    res.status(500).json({
      success: false,
      message: '获取系统信息失败'
    })
  }
}

// 获取CPU使用率
export const getCpuUsage = async (req: Request, res: Response) => {
  try {
    const [cpuInfo, cpuUsage, memInfo, driveInfo] = await Promise.all([
      cpu.info(),
      cpu.usage(),
      mem.info(),
      drive.info()
    ])

    // 获取CPU温度（如果可用）
    let cpuTemperature = null
    try {
      if (process.platform === 'win32') {
        const { stdout } = await execAsync('wmic /namespace:\\\\root\\wmi PATH MSAcpi_ThermalZoneTemperature get CurrentTemperature')
        const tempMatch = stdout.match(/CurrentTemperature\s+(\d+)/)
        if (tempMatch) {
          // Windows返回的是开尔文温度，需要转换为摄氏度
          cpuTemperature = ((parseInt(tempMatch[1]) / 10) - 273.15).toFixed(2)
        }
      } else if (process.platform === 'linux') {
        const { stdout } = await execAsync('cat /sys/class/thermal/thermal_zone0/temp')
        cpuTemperature = (parseInt(stdout) / 1000).toFixed(2)
      }
    } catch (error) {
      console.warn('获取CPU温度失败:', error)
    }

    res.json({
      success: true,
      message: '查询成功',
      data: {
        cpuModel: cpuInfo.model,
        cpuCores: cpuInfo.cores,
        cpuThreads: cpuInfo.cores * 2, // 估算线程数
        cpuTemperature,
        cpuUsage: cpuUsage.toFixed(2),
        memoryTotal: memInfo.totalMemMb.toFixed(2),
        memoryUsed: memInfo.usedMemMb.toFixed(2),
        memoryFree: memInfo.freeMemMb.toFixed(2),
        memoryUsage: ((memInfo.usedMemMb / memInfo.totalMemMb) * 100).toFixed(2),
        diskTotal: (driveInfo.totalGb).toFixed(2),
        diskUsed: (driveInfo.usedGb).toFixed(2),
        diskFree: (driveInfo.freeGb).toFixed(2),
        diskUsage: (driveInfo.usedPercentage).toFixed(2)
      }
    })
  } catch (error) {
    console.error('获取CPU使用率失败:', error)
    res.status(500).json({
      success: false,
      message: '获取CPU使用率失败'
    })
  }
}

// 获取内存使用情况
export const getMemoryUsage = async (req: Request, res: Response) => {
  try {
    const [cpuInfo, cpuUsage, memInfo, driveInfo] = await Promise.all([
      cpu.info(),
      cpu.usage(),
      mem.info(),
      drive.info()
    ])

    res.json({
      success: true,
      message: '查询成功',
      data: {
        cpuModel: cpuInfo.model,
        cpuCores: cpuInfo.cores,
        cpuThreads: cpuInfo.cores * 2,
        cpuTemperature: null, // 内存接口不返回温度
        cpuUsage: cpuUsage.toFixed(2),
        memoryTotal: memInfo.totalMemMb.toFixed(2),
        memoryUsed: memInfo.usedMemMb.toFixed(2),
        memoryFree: memInfo.freeMemMb.toFixed(2),
        memoryUsage: ((memInfo.usedMemMb / memInfo.totalMemMb) * 100).toFixed(2),
        diskTotal: (driveInfo.totalGb).toFixed(2),
        diskUsed: (driveInfo.usedGb).toFixed(2),
        diskFree: (driveInfo.freeGb).toFixed(2),
        diskUsage: (driveInfo.usedPercentage).toFixed(2)
      }
    })
  } catch (error) {
    console.error('获取内存使用情况失败:', error)
    res.status(500).json({
      success: false,
      message: '获取内存使用情况失败'
    })
  }
} 