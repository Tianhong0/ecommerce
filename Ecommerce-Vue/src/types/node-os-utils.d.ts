declare module 'node-os-utils' {
  export const cpu: {
    usage(): Promise<number>
    info(): Promise<{
      model: string
      cores: number
    }>
  }
  
  export const mem: {
    info(): Promise<{
      totalMemMb: number
      usedMemMb: number
      freeMemMb: number
    }>
  }

  export const drive: {
    info(): Promise<{
      totalGb: number
      usedGb: number
      freeGb: number
      usedPercentage: number
    }>
  }
} 