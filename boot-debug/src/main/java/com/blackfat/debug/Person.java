package com.blackfat.debug;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-03-08 09:26
 * @since 1.0-SNAPSHOT
 */

public class Person {

    private Integer id;

    private String name;

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
