package com.yr.sql.mybatis.pojo;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "test_2")
public class Test2 implements Serializable {
    @Id
    @Column(name = "t_num")
    private Integer tNum;

    @Column(name = "t_name")
    private String tName;

    private static final long serialVersionUID = 1L;

    /**
     * @return t_num
     */
    public Integer gettNum() {
        return tNum;
    }

    /**
     * @param tNum
     */
    public void settNum(Integer tNum) {
        this.tNum = tNum;
    }

    /**
     * @return t_name
     */
    public String gettName() {
        return tName;
    }

    /**
     * @param tName
     */
    public void settName(String tName) {
        this.tName = tName;
    }
}