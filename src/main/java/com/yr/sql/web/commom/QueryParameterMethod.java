package com.yr.sql.web.commom;

/**
 * @moduleName: QueryParameterMethod
 * @description: 行为参数
 * @author: 杨睿
 * @date: 2021-03-21 10:37
 **/
public enum QueryParameterMethod {
    /**
     * 等于，=
     */
    EQUAL,

    /**
     * 模糊查询，like
     */
    LIKE,

    /**
     * in
     */
    IN,

    /**
     * 判断空值
     */
    IS_NULL,

    /**
     * 判断不为空
     */
    IS_NOT_NULL,

    /**
     * 二者之间，between
     */
    BETWEEN,

    /**
     * 大于，<
     */
    GREATER,

    /**
     * 大于等于，<=
     */
    GREATER_OR_EQUAL,

    /**
     * 小于，<
     */
    LESS,

    /**
     * 小于等于，<=
     */
    LESS_OR_EQUAL
}
