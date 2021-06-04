package com.yr.sql.mybatis;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

/**
 * @moduleName: CustomMapper
 * @description: 顶级Mapper层
 * @author: 杨睿
 * @date: 2021-03-20 11:00
 **/
public interface CustomMapper<T> extends Mapper<T>, MySqlMapper<T>, ConditionMapper<T>, SelectByIdsMapper<T> {
}
