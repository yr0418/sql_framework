package com.yr.sql.mybatis.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class User implements Serializable {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 年级
     */
    private Integer age;

    /**
     * 分数
     */
    private Double score;

    /**
     * 注册时间
     */
    @Column(name = "sign_time")
    private Date signTime;

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
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取年级
     *
     * @return age - 年级
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置年级
     *
     * @param age 年级
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取分数
     *
     * @return score - 分数
     */
    public Double getScore() {
        return score;
    }

    /**
     * 设置分数
     *
     * @param score 分数
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * 获取注册时间
     *
     * @return sign_time - 注册时间
     */
    public Date getSignTime() {
        return signTime;
    }

    /**
     * 设置注册时间
     *
     * @param signTime 注册时间
     */
    public void setSignTime(Date signTime) {
        this.signTime = signTime;
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
}