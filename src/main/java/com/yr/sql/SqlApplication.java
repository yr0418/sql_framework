package com.yr.sql;

import com.yr.sql.common.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @moduleName: WorkApplication
 * @description: Springboot 启动类
 *
 * @author: 杨睿
 */
@SpringBootApplication
@MapperScan(value = "com.yr.sql.mybatis.mapper")
public class SqlApplication {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(SqlApplication.class, args);
        SpringUtil.setApplicationContext(app);
    }

}
