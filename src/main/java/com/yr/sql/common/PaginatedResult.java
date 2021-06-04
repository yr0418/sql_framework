package com.yr.sql.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @moduleName: PaginatedResult
 * @description: 自定义分页结果
 * @author: 杨睿
 * @date: 2021-03-20 9:45
 **/

@Accessors(chain = true)
@Data
@ToString
public class PaginatedResult implements Serializable {
    private String version;
    private Boolean success;
    private String message;
    private Object data;
    private Pagination pagination;
    private Dictionary<String, Object> dictionary; // dictionary of related result

    public PaginatedResult() {
        this.version = Constant.VERSION;
        this.success = true;
        this.message = Constant.EMPTY_STRING;
        this.dictionary = new Hashtable<>();
        this.pagination = new Pagination();
    }

    public PaginatedResult setParameters(int pageSize, int current, int total) {
        this.pagination = new Pagination(pageSize, current, total);
        return this;
    }

    @Accessors(chain = true)
    @Data
    @ToString
    @NoArgsConstructor
    public class Pagination {
        private int current = 0; // Current page number
        private int total = 0; // Number of total elements
        private int pageSize = 0;
        private int pageCount = 0;
        private Boolean previous;
        private Boolean next;

        public Pagination(int pageSize, int current, int total) {
            this.pageSize = pageSize;
            this.current = current;
            this.total = total;
            this.previous = current > 1;
            this.pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            this.next = current < pageCount;
            this.previous = current > 1;
        }
    }
}
