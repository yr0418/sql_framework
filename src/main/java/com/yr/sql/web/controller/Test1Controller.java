package com.yr.sql.web.controller;

import com.yr.sql.mybatis.pojo.Test1;
import com.yr.sql.web.commom.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @moduleName: TestController
 * @description: controller测试类
 * @author: 杨睿
 * @date: 2021-04-12 17:27
 **/
@RestController
@RequestMapping(value = "test")
public class Test1Controller extends BaseController<Test1> {

}
