package com.yr.sql.web.controller;

import com.yr.sql.mybatis.pojo.User;
import com.yr.sql.web.commom.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @moduleName: UserController
 * @description: 用户controller层
 * @author: 杨睿
 * @date: 2021-01-06 9:44
 **/

@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController<User> {
}
