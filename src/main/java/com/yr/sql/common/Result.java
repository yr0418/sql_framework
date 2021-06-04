package com.yr.sql.common;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @moduleName: Result
 * @description: 自定义返回结果值
 * @author: 杨睿
 * @date: 2021-03-20 10:10
 **/

@Accessors(chain = true)
@Data
@ToString
public class Result implements Serializable {
    private Object data;
    private Boolean success;
    private String message;
    private String version;
    private Dictionary<String, Object> dictionary; // dictionary of related result

    public Result(Boolean success) {
        this.version = Constant.VERSION;
        this.success = success;
        this.message = Constant.EMPTY_STRING;
        this.dictionary = new Hashtable<>();
    }

    public Result() {
        this.version = Constant.VERSION;
        this.success = true;
        this.message = Constant.EMPTY_STRING;
        this.dictionary = new Hashtable<>();
    }
}
