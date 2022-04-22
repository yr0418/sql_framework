package com.yr.sql.web.commom;

import com.github.pagehelper.PageHelper;
import com.yr.sql.common.SpringUtil;
import com.yr.sql.mybatis.CustomMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.yr.sql.common.Constant.ALL_PAGE;
import static com.yr.sql.common.Constant.PAGE_SIZE;

/**
 * @moduleName: BaseService
 * @description: 底层Service
 * @author: 杨睿
 * @date: 2021-01-06 9:42
 **/

public class BaseService<T> {
    @Autowired
    protected CustomMapper<T> mapper;

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
     * 空值预处理，判断 Id 是否存在
     *
     * @param id 主键
     * @return
     */
    public Optional<T> selectByID(Long id) {
        return Optional.ofNullable(mapper.selectByPrimaryKey(id));
    }

    /**
     * 根据 Id列表 获取 实例列表
     *
     * @param idList id列表
     * @return 实例列表
     */
    @Cacheable(value = "selectAll(idList)", keyGenerator = "cacheKeyGenerator")
    public List<T> selectAll(List<Long> idList) {
        String ids = "";
        for (int index = 0; index < idList.size(); index++) {
            ids += Long.toString(idList.get(index));
            if (index != idList.size()-1) {
                ids += ",";
            }
        }
        if ("".equals(ids)) {
            return new ArrayList<>();
        } else {
            return mapper.selectByIds(ids);
        }
    }

    /**
     * 根据页数获取实例列表
     *
     * @param page     页数
     * @param pageSize 页面大小
     * @return 该页数据
     */
    @Cacheable(value = "selectAll()page", keyGenerator = "cacheKeyGenerator")
    public List<T> selectAll(int page, int pageSize) {
        Example example = new Example(getActualClass());
        example.setOrderByClause("id desc");
        PageHelper.startPage(page, pageSize);
        return mapper.selectByExample(example);
    }

    /**
     * 根据查询参数和页数获取实例列表
     *
     * @param parameters 查询参数
     * @param page       页数
     * @return 实例列表
     */
    @Cacheable(value = "selectAll(parameters)page", keyGenerator = "cacheKeyGenerator")
    public List<T> selectAll(int page, int pageSize, QueryParameter...parameters) {
        if (page != ALL_PAGE) {
            PageHelper.startPage(page, pageSize);
        }
        return mapper.selectByExample(createExample(parameters));
    }

    /**
     * 根据查询参数获取实例列表
     *
     * @param parameters 查询参数
     * @return 实例列表
     */
    @Cacheable(value = "selectAll(parameters)", keyGenerator = "cacheKeyGenerator")
    public List<T> selectAll(QueryParameter...parameters) {
        return selectAll(ALL_PAGE, Integer.parseInt(PAGE_SIZE), parameters);
    }

    /**
     * 根据字段(字符型)获取实例列表
     *
     * @param type  字段
     * @param value 查询参数(字符型)
     * @return 实例列表
     */
    public List<T> selectAll(String type, String value) {
        if (type.toLowerCase().endsWith("id")) {
            return selectAll(new QueryParameter(type, QueryParameterMethod.EQUAL, Long.parseLong(value)));
        } else {
            return selectAll(new QueryParameter(type, QueryParameterMethod.LIKE, value));
        }
    }

    /**
     * 根据字段(id列表)查询实例
     *
     * @param type 字段
     * @param ids  id列表
     * @return 实例列表
     */
    public List<T> selectAll(String type, List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        } else {
            Example example = new Example(getActualClass());
            example.setOrderByClause("id desc");
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(type, ids);
            return mapper.selectByExample(example);
        }
    }

    /**
     * 根据 id列表 和 页数 获取实例
     *
     * @param type        字段
     * @param ids         id列表
     * @param restriction 限制参数
     * @param page        页数
     * @param pageSize    页面大小
     * @return 实例列表
     */
    public List<T> selectAll(String type, List<Long> ids, QueryParameter restriction, int page, int pageSize) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        } else {
            Example example = new Example(getActualClass());
            example.setOrderByClause("id desc");
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(type, ids);
            if (restriction != null) {
                createCriteria(criteria, restriction);
            }
//            PageHelper.startPage(page, pageSize, PAGE_SIZE);
            PageHelper.startPage(page, pageSize);
            return mapper.selectByExample(example);
        }
    }

    /**
     * 根据查询参数和页数获取位置最前的多个实例
     *
     * @param count      数量
     * @param parameters 查询参数
     * @return 实例列表
     */
    public List<T> selectTop(int count, QueryParameter...parameters) {
        return mapper.selectByExampleAndRowBounds(createExample(parameters), new RowBounds(0, count));
    }

    /**
     * 根据查询参数和页数获取实例列表（关联查询）
     *
     * @param table       关联表
     * @param parameters  查询参数
     * @param restriction 查询参数
     * @param page        页数
     * @return 实例列表
     */
    public List<T> selectRelatedAll(String table, int page, int pageSize, QueryParameter restriction, QueryParameter...parameters) throws Exception {
        Object relatedService = SpringUtil.getBean(table + "Service");
        Method selectMethod = relatedService.getClass().getDeclaredMethod("selectAll", QueryParameter[].class);
        Object selectList = selectMethod.invoke(relatedService, new Object[] {parameters});
        Method selectIdsMethod = relatedService.getClass().getDeclaredMethod("getIds", List.class);
        List<Long> ids = (List<Long>) selectIdsMethod.invoke(relatedService, (List) selectList);
        return selectAll(table+"Id", ids, restriction, page, pageSize);
    }


    /**
     * 获取实例总数
     *
     * @return 实例总数
     * @throws Exception 反射异常
     */
    @Cacheable(value = "getCount", keyGenerator = "cacheKeyGenerator")
    public int getCount() throws Exception {
        Object object = getActualClass().newInstance();
        int count = mapper.selectCount((T) object);
        return count;
    }

    /**
     * 根据查询参数获取实例总数
     *
     * @param parameters 查询参数
     * @return 实例总数
     */
    @Cacheable(value = "getCount(parameters)", keyGenerator = "cacheKeyGenerator")
    public int getCount(QueryParameter...parameters) {
        int count = mapper.selectCountByExample(createExample(parameters));
        return count;
    }

    /**
     * 根据字段（字符型）获取实例总数
     *
     * @param type  字段
     * @param value 查询参数（字符型）
     * @return 实例总数
     */
    public int getCount(String type, String value) {
        if (type.endsWith("Id")) {
            return getCount(new QueryParameter(type, QueryParameterMethod.EQUAL, Long.parseLong(value)));
        } else {
            return getCount(new QueryParameter(type, QueryParameterMethod.LIKE, value));
        }
    }

    /**
     * 根据查询参数获取实例总数（关联查询）
     *
     * @param table      关联表
     * @param parameters 查询参数
     * @return 实例总数
     */
    public int getCount(String table, QueryParameter restriction, QueryParameter...parameters) throws Exception {
        Object relatedService = SpringUtil.getBean(table + "Service");
        Method selectMethod = relatedService.getClass().getDeclaredMethod("selectAll", QueryParameter[].class);
        Object selectList = selectMethod.invoke(relatedService, new Object[] {parameters});
        Method selectIdsMethod = relatedService.getClass().getDeclaredMethod("getIds", List.class);
        List<Long> ids = (List<Long>) selectIdsMethod.invoke(relatedService, selectList);
        return getCount(table + "Id", ids, restriction);
    }

    /**
     * 根据 字段（id列表）获取实例总数
     * @param type        字段
     * @param ids         id列表
     * @param restriction 限制参数
     * @return 实例总数
     */
    public int getCount(String type, List<Long> ids, QueryParameter restriction) {
        if (ids.isEmpty()) {
            return 0;
        } else {
            Example example = new Example(getActualClass());
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(type, ids);
            if (restriction != null) {
                createCriteria(criteria, restriction);
            }
            int count = mapper.selectCountByExample(example);
            return count;
        }
    }


    /**
     * 从实例列表中提取ID列表
     *
     * @param list 实例列表
     * @return ID列表
     */
    public List<Long> getIds(List<T> list) throws Exception {
        return getRelatedIds(list, "id");
    }

    /**
     * 提取对象列表中所有的特定ID字段列表
     *
     * @param list  对象列表
     * @param filed 私有字段名
     * @return 所有特定ID字段列表
     * @throws Exception 反射异常
     */
    public List<Long> getRelatedIds(List<T> list, String filed) throws Exception {
        Class type = getActualClass();
        Field field = type.getDeclaredField(filed);
        field.setAccessible(true);
        ArrayList<Long> result = new ArrayList<>();
        for (T instance : list) {
            Long e = Long.parseLong(field.get(instance).toString());
            if (!result.contains(e)) {
                result.add(e);
            }
        }
        return result;
    }


    /**
     * 插入实例
     *
     * @param t 实例
     * @return 插入与否
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page", "getCount", "getCount(parameters)" }, allEntries = true)
    public boolean add(T t) {
        return mapper.insertUseGeneratedKeys(t) > 0;
    }

    /**
     * 批量插入实例
     *
     * @param list 实例列表
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page", "getCount", "getCount(parameters)" }, allEntries = true)
    public int addList(List<T> list) {
        return mapper.insertList(list);
    }


    /**
     * 修改实例
     *
     * @param t 实例
     * @return 修改与否
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page" }, allEntries = true)
    public boolean modifyById(T t) {
        return mapper.updateByPrimaryKeySelective(t) > 0;
    }


    /**
     * 通过 id 删除实例
     *
     * @param id 实例id
     * @return 删除与否
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page", "getCount", "getCount(parameters)" }, allEntries = true)
    public boolean deleteById(Long id) {
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * 删除全部实例
     *
     * @return 删除与否
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page",
            "getCount", "getCount(parameters)" }, allEntries = true)
    public boolean deleteAll() {
        Example example = new Example(getActualClass());
        return mapper.deleteByExample(example) > 0;
    }

    /**
     * 根据参数删除实例
     *
     * @param parameters 参数
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { "selectAll(idList)", "selectAll()page", "selectAll(parameters)", "selectAll(parameters)page", "getCount", "getCount(parameters)" }, allEntries = true)
    public boolean delete(QueryParameter... parameters) {
        return mapper.deleteByExample(createExample(parameters)) > 0;
    }


    /**
     * 创建 example
     *
     * @param parameters 参数
     * @return example实例
     */
    public Example createExample(QueryParameter...parameters) {
        Example example = new Example(getActualClass());
        example.setOrderByClause("id desc");
        Example.Criteria criteria = example.createCriteria();
        for (QueryParameter parameter: parameters) {
            createCriteria(criteria, parameter);
        }
        return example;
    }

    public void createCriteria(Example.Criteria criteria, QueryParameter parameter) {
        Object value = parameter.getValue();
        switch (parameter.getMethod()) {
            case LIKE:
                criteria.andLike(parameter.getType(), "%" + value.toString() + "%");
                break;
            case EQUAL:
                criteria.andEqualTo(parameter.getType(), value);
                break;
            case IN:
                criteria.andIn(parameter.getType(), (List) value);
                break;
            case IS_NULL:
                criteria.andIsNull(parameter.getType());
                break;
            case IS_NOT_NULL:
                criteria.andIsNotNull(parameter.getType());
                break;
            case GREATER:
                criteria.andGreaterThan(parameter.getType(), value);
                break;
            case GREATER_OR_EQUAL:
                criteria.andGreaterThanOrEqualTo(parameter.getType(), value);
                break;
            case LESS:
                criteria.andLessThan(parameter.getType(), value);
                break;
            case LESS_OR_EQUAL:
                criteria.andLessThanOrEqualTo(parameter.getType(), value);
            default:
                break;
        }
    }
}
