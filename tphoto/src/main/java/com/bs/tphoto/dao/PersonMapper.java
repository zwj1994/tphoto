package com.bs.tphoto.dao;

import com.bs.tphoto.entity.PersonDO;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:22 2018/3/21
 */

@Mapper
@CacheConfig(cacheNames = "persons")
public interface PersonMapper {

    /**
     * 查询所有
     *
     * @return
     */
    @Select("select id,name from person")
    @Cacheable(key ="1")
    List<PersonDO> selectAll();

    /**
     * 添加操作，返回新增元素的 ID
     *
     * @param personDO
     */
    @Insert("insert into person(id,name) values(#{id},#{name})")
    @CacheEvict(allEntries=false,key="1")
    int insert(PersonDO personDO);



}
