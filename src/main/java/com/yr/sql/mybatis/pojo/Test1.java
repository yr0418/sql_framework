package com.yr.sql.mybatis.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "test_1")
public class Test1 implements Serializable {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * int类型
     */
    @Column(name = "type_int")
    private Integer typeInt;

    /**
     * 字符串类型
     */
    @Column(name = "type_string")
    private String typeString;

    /**
     * double类型
     */
    @Column(name = "type_double")
    private Double typeDouble;

    /**
     * date类型
     */
    @Column(name = "type_date")
    private Date typeDate;

    /**
     * Long类型
     */
    @Column(name = "type_long")
    private Long typeLong;

    /**
     * 关联值id
     */
    @Column(name = "same_id")
    private Integer sameId;

    /**
     * 关联值val
     */
    @Column(name = "same_val")
    private String sameVal;

    @Column(name = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取int类型
     *
     * @return type_int - int类型
     */
    public Integer getTypeInt() {
        return typeInt;
    }

    /**
     * 设置int类型
     *
     * @param typeInt int类型
     */
    public void setTypeInt(Integer typeInt) {
        this.typeInt = typeInt;
    }

    /**
     * 获取字符串类型
     *
     * @return type_string - 字符串类型
     */
    public String getTypeString() {
        return typeString;
    }

    /**
     * 设置字符串类型
     *
     * @param typeString 字符串类型
     */
    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    /**
     * 获取double类型
     *
     * @return type_double - double类型
     */
    public Double getTypeDouble() {
        return typeDouble;
    }

    /**
     * 设置double类型
     *
     * @param typeDouble double类型
     */
    public void setTypeDouble(Double typeDouble) {
        this.typeDouble = typeDouble;
    }

    /**
     * 获取date类型
     *
     * @return type_date - date类型
     */
    public Date getTypeDate() {
        return typeDate;
    }

    /**
     * 设置date类型
     *
     * @param typeDate date类型
     */
    public void setTypeDate(Date typeDate) {
        this.typeDate = typeDate;
    }

    /**
     * 获取Long类型
     *
     * @return type_long - Long类型
     */
    public Long getTypeLong() {
        return typeLong;
    }

    /**
     * 设置Long类型
     *
     * @param typeLong Long类型
     */
    public void setTypeLong(Long typeLong) {
        this.typeLong = typeLong;
    }

    /**
     * 获取关联值id
     *
     * @return same_id - 关联值id
     */
    public Integer getSameId() {
        return sameId;
    }

    /**
     * 设置关联值id
     *
     * @param sameId 关联值id
     */
    public void setSameId(Integer sameId) {
        this.sameId = sameId;
    }

    /**
     * 获取关联值val
     *
     * @return same_val - 关联值val
     */
    public String getSameVal() {
        return sameVal;
    }

    /**
     * 设置关联值val
     *
     * @param sameVal 关联值val
     */
    public void setSameVal(String sameVal) {
        this.sameVal = sameVal;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}