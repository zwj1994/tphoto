package com.bs.tphoto.service;

import com.bs.tphoto.entity.YBanner;
import com.bs.tphoto.entity.YUser;

import java.util.List;

/**
 * 系统服务
 */
public interface SysService {

    /***
     * 登录
     * @param model
     * @return
     */
    YUser login(YUser model);

    /***
     * 根据账号查找用户信息
     * @param account
     * @return
     */
    YUser findOne(String account);

    /**
     * 获取轮播图
     * @return
     */
    List<YBanner> queryBanner();
}
