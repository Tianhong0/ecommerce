// API响应类型定义

/**
 * 通用API响应接口
 */
export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data?: T;
}

/**
 * 分页数据接口
 */
export interface PageResult<T = any> {
  records: T[];
  total: number;
  size: number;
  current: number;
} 