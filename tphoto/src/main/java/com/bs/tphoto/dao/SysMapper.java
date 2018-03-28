package com.bs.tphoto.dao;


import com.bs.tphoto.entity.YUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:zhuwj
 * @Description: 系统
 * @Date:Created in 10:22 2018/3/21
 */

@Mapper
public interface SysMapper {

    /**
     * 查询用户
     * @param model
     * @return
     */
    @Select("select u_name,u_account,u_sign,u_headImg,u_state from y_user where u_account = #{uAccount} and u_pwd = #{uPwd} and u_state != -1")
    YUser select(YUser model);

    /**
     * 查询单个用户
     * @param account
     * @return
     */
    @Select("select u_name,u_account,u_sign,u_headImg,u_state from y_user where u_account = #{account} and u_state != -1")
    YUser findOne(@Param("account") String account);



}
