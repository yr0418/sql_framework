package com.yr.sql.web.commom;

import lombok.Data;

import java.sql.Date;
import java.util.List;

/**
 * @moduleName: QueryParameter
 * @description: 定义查询参数
 * @author: 杨睿
 * @date: 2021-03-21 10:38
 **/

@Data
public class QueryParameter {
    private String type;
    private QueryParameterMethod method;
    private Object value;
    private QueryParameterType valueType;

    public QueryParameter(String type, QueryParameterMethod method, Long value) {
        this.type = type;
        this.method = method;
        this.valueType = QueryParameterType.LONG;
        this.value = value;
    }

    public QueryParameter(String type, QueryParameterMethod method, Integer value) {
        this.type = type;
        this.method = method;
        this.valueType = QueryParameterType.INT;
        this.value = value;
    }

    public QueryParameter(String type, QueryParameterMethod method, String value) {
        this.type = type;
        this.method = method;
        this.valueType = QueryParameterType.STRING;
        this.value = value;
    }

    public QueryParameter(String type, QueryParameterMethod method, Date value) {
        this.type = type;
        this.method = method;
        this.valueType = QueryParameterType.DATE;
        this.value = value;
    }

    public QueryParameter(String type, QueryParameterMethod method, List value) {
        this.type = type;
        this.method = method;
        this.valueType = QueryParameterType.LIST;
        this.value = value;
    }

    protected QueryParameter() {
    }

    /**
     * 重写toString方法
     * @return
     */
    @Override
    public String toString() {
        String valueString = value.toString();
        switch (valueType) {
            case DATE:
                Date date = (Date) value;
                valueString = Long.valueOf(date.getTime()).toString();
                break;
            case LIST:
                List list = (List) value;
                for (int i = 0; i < list.size(); i++) {
                    valueString += list.get(i).toString();
                    if (i != list.size() - 1) {
                        valueString += ",";
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + valueType);
        }
        return String.format("%s,%s,%s,%s", type, method.toString(), valueString, valueType.toString());
    }
}
