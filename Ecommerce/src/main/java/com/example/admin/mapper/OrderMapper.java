package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.Order;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
} 