package com.bs.tphoto.entity;/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:24 2018/3/21
 */

import java.io.Serializable;

/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:24 2018/3/21
 */
public class PersonDO implements Serializable {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
