package com.yr.sql.web.controller;

import com.yr.sql.mybatis.pojo.Test2;
import com.yr.sql.web.commom.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @moduleName: Test2Controller
 * @description: 测试类2的控制层
 * @author: 杨睿
 * @date: 2021-05-02 15:39
 **/
@RestController
@RequestMapping(value = "test2")
public class Test2Controller extends BaseController<Test2> {
}
