package com.bs.tphoto.service.impl;

import com.bs.tphoto.dao.SysMapper;
import com.bs.tphoto.dao.YBannerMapper;
import com.bs.tphoto.entity.YBanner;
import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

    @Autowired
    private YBannerMapper yBannerMapper;


    @Override
    public YUser login(YUser model) {
        return sysMapper.select(model);
    }

    @Override
    public YUser findOne(String account) {
        return sysMapper.findOne(account);
    }

    @Override
    public List<YBanner> queryBanner() {
        return yBannerMapper.selectAll();
    }
}
