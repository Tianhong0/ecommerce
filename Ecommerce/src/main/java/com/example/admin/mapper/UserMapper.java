package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问映射器
 * <p>
 * 该接口继承自MyBatis-Plus的BaseMapper，提供对用户表(sys_user)的数据访问能力。
 * 通过继承BaseMapper，自动获得基本的CRUD操作方法，如：
 * - insert: 插入一条记录
 * - deleteById: 根据ID删除
 * - updateById: 根据ID更新
 * - selectById: 根据ID查询
 * - selectList: 查询列表
 * - selectPage: 分页查询
 * 等基础数据操作功能。
 * </p>
 * <p>
 * 该接口使用@Mapper注解标记，会被MyBatis自动扫描并创建实现类。
 * 无需编写SQL语句即可完成基本的数据库操作。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 * @see User 用户实体类
 * @see BaseMapper MyBatis-Plus基础映射器接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 