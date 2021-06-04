package com.yr.sql.web.commom;

import com.yr.sql.common.ManualCache;
import com.yr.sql.common.PaginatedResult;
import com.yr.sql.common.Result;
import com.yr.sql.common.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yr.sql.common.Constant.*;

/**
 * @moduleName: BaseController
 * @description: 底层Controller
 * @author: 杨睿
 * @date: 2021-01-06 9:45
 **/
public class BaseController<T> {
    @Autowired
    protected BaseService<T> service;

    @Autowired
    protected ManualCache manualCache;

    /**
     * 获取真实反射类型
     *
     * @return 反射类型
     */
    Class getActualClass() {
        Type type = getClass().getGenericSuperclass();

        //判断是否为泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象数组
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();

            // 返回第一个泛型T对应的类
            return (Class) types[0];
        } else {
            // 若没有给定泛型，则返回 Object 类
            return (Class) type;
        }
    }


    /**
     * 关联查询
     *
     * @param result 查询结果
     * @return 关联查询结果
     */
    protected PaginatedResult relatedResult(PaginatedResult result) throws Exception {
        return result;
    }


    /**
     * 分页查询实例
     *
     * @param current  当前页
     * @param pageSize 页面大小
     * @param query    查询参数
     * @return 查询结果
     */
    @ApiOperation(value = "分页查询实例")
    @PostMapping(value = "search/query")
    public ResponseEntity<?> searchByQuery(
            @RequestParam(value = "current", required = false, defaultValue = CURRENT_PAGE) Integer current,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestBody QueryParameter[] query
    ) throws Exception {
        return search(current, pageSize, query, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
    }

    /**
     * 分页关联查询实例，关联查询查询table表
     * 1. 对table表，使用查询参数query查询，获取查询到的实例的id列表 ids
     * 2. 再在原始表中，查询 type字段 值为value 且 tableId 再 ids中
     *
     * @param current  当前页
     * @param pageSize 页面大小
     * @param query    查询参数
     * @param type     字段类型
     * @param value    字段值
     * @param table    表
     * @return 查询结果
     */
    @ApiOperation(value = "分页关联查询")
    @PostMapping(value = "search/related")
    public ResponseEntity<?> search(
            @RequestParam(value = "current", required = false, defaultValue =CURRENT_PAGE) Integer current,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestBody QueryParameter[] query,
            @RequestParam(value = "type", required = false, defaultValue = EMPTY_STRING) String type,
            @RequestParam(value = "value", required = false, defaultValue = EMPTY_STRING) String value,
            @RequestParam(value = "table", required = false, defaultValue = EMPTY_STRING) String table
            ) throws Exception{

        //查询限制参数
        QueryParameter restriction = null;

        /*
         * 生成 query 限制
         * 当查询的字段以 id 结尾时，默认该字段具有唯一性，使用 精准查询
         * 否则，使用模糊查询
         */
        if (!(EMPTY_STRING.equals(type))) {
            if (type.endsWith("Id")) {
                restriction = new QueryParameter(type, QueryParameterMethod.EQUAL,Long.parseLong(value));
            } else {
                restriction = new QueryParameter(type, QueryParameterMethod.LIKE,value);
            }
        }

        if (EMPTY_STRING.equals(table)) {
            ArrayList<QueryParameter> queryList = new ArrayList(Arrays.asList(query));
            if (restriction != null) {
                queryList.add(restriction);
            }
            query = queryList.toArray(new QueryParameter[0]);
            return ResponseEntity.ok(relatedResult(
                    new PaginatedResult()
                            .setData(service.selectAll(current, pageSize, query))
                            .setParameters(pageSize, current,service.getCount(query))
                    )
            );
        } else {
            return ResponseEntity.ok(relatedResult(
                    new PaginatedResult()
                    .setData(service.selectRelatedAll(table, current, pageSize, restriction, query))
                    .setParameters(pageSize, current, service.getCount(table, restriction, query))
                    )
            );
        }
    }

    /**
     * 查询最新的数个实例
     *
     * @param query  查询参数
     * @param number 实例个数
     * @return 查询结果
     */
    @ApiOperation(value = "查询最新数个实例")
    @PostMapping("/top")
    public ResponseEntity<?> searchByTop(@RequestBody QueryParameter[] query,
                                 @RequestParam(value = "number", required = false, defaultValue = "1") Integer number) throws Exception {
        return search(1, number, query, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
    }

    /**
     * 查询所有实例
     *
     * @param query 查询参数
     * @return 查询结果
     */
    @ApiOperation(value = "查询所有实例")
    @PostMapping("/all")
    public ResponseEntity<?> searchAll(@RequestBody QueryParameter[] query) throws Exception {
        return search(ALL_PAGE, Integer.parseInt(PAGE_SIZE), query, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
    }

    /**
     * 根据 id 获取实例
     *
     * @param ids 实例id列表
     * @return
     */
    @ApiOperation(value = "根据id获取实例")
    @GetMapping(value = "selectById")
    public ResponseEntity<?> selectById(@RequestParam Long[] ids) {
        List<Long> idsList = Arrays.asList(ids);
        List<T> instance = service.selectAll(idsList);
        if (!instance.isEmpty()) {
            return ResponseEntity.ok(new Result().setData(instance).setMessage("查询成功"));
        } else {
            return ResponseEntity.ok(new Result(false).setData(ids).setMessage("目标不存在"));
        }
    }


    /**
     * 新增实例
     *
     * @param instance 实例内容
     * @return
     */
    @ApiOperation(value = "新增实例")
    @PostMapping(value = "insert")
    public ResponseEntity<?> insert(@RequestBody T instance){
        if (service.add(instance)) {
            return ResponseEntity.ok(new Result().setData(instance).setMessage("新增成功"));
        } else {
            return ResponseEntity.ok(new Result().setData(instance).setMessage("新增失败"));
        }
    }

    /**
     * 批量新增实例
     *
     * @param list 实例列表
     * @return
     */
    @ApiOperation(value = "批量新增实例")
    @PostMapping(value = "insertList")
    public ResponseEntity<?> insertList(@RequestBody List<T> list) {
        int count = service.addList(list);
        return ResponseEntity.ok(new Result().setData(count).setMessage("新增成功"));
    }

    /**
     * 根据 Id 修改实例
     *
     * @param instance 实例
     * @return
     */
    @ApiOperation(value = "根据 id 修改实例")
    @PostMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody T instance) {
        boolean result = service.modifyById(instance);
        if (result) {
            return ResponseEntity.ok(new Result().setData(instance).setMessage("修改成功"));
        } else {
            return ResponseEntity.ok(new Result(false).setMessage("目标不存在"));
        }
    }

    /**
     * 根据 id 删除实例
     *
     * @param id 实例id
     * @return
     */
    @ApiOperation(value = "根据 id 删除实例")
    @DeleteMapping(value = "deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        boolean result = service.deleteById(id);
        if (result) {
            return ResponseEntity.ok(new Result().setData(id).setMessage("删除成功"));
        } else {
            return ResponseEntity.ok(new Result(false).setMessage("目标不存在"));
        }
    }

    /**
     * 删除全部实例
     *
     * @return
     */
    @ApiOperation(value = "删除全部实例")
    @DeleteMapping(value = "deleteAll")
    public ResponseEntity<?> deleteAll() {
        boolean result = service.deleteAll();
        if (result) {
            return ResponseEntity.ok(new Result().setMessage("删除成功"));
        } else {
            return ResponseEntity.ok(new Result(false).setMessage("目标不存在"));
        }
    }

    /**
     * 查询删除实例
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "查询删除实例")
    @DeleteMapping(value = "deleteByInstance")
    public ResponseEntity<?> deleteByInstance(@RequestBody QueryParameter[] query) {
        boolean result = service.delete(query);
        if (result) {
            return ResponseEntity.ok(new Result().setMessage("删除成功"));
        } else {
            return ResponseEntity.ok(new Result(false).setMessage("目标不存在"));
        }
    }
}
