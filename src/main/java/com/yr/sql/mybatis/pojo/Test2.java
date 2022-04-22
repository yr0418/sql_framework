package com.yr.sql.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;

@Data
@Table(name = "test_2")
public class Test2 implements Serializable {
    @Id
    @Column(name = "id")
    private Integer Id;

    @Column(name = "t_name")
    private String tName;

    private static final long serialVersionUID = 1L;
}