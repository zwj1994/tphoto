package com.bs.tphoto.service.impl;

import com.bs.tphoto.PageBean;
import com.bs.tphoto.dao.SysMapper;
import com.bs.tphoto.dao.YAlbumMapper;
import com.bs.tphoto.entity.YAlbum;
import com.bs.tphoto.entity.YPhoto;
import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.service.YAlbumService;
import com.bs.tphoto.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YAlbumServiceImpl implements YAlbumService {

    @Autowired
    private YAlbumMapper yAlbumMapper;

    @Autowired
    private SysMapper sysMapper;

    @Override
    public List<YAlbum> queryAllByLikeCountDesc(int page) {
        return yAlbumMapper.selectAllByLikeCountDesc(new PageBean(PageUtil.getOffset(page,5),5));
    }

    @Override
    public List<YPhoto> queryPhotosByAId(String aId,int page) {
        return yAlbumMapper.selectPhotosByAId(aId,PageUtil.getOffset(page,10),10);
    }

    @Override
    public List<YAlbum> queryMyAlbumByDateDesc(int page, YUser yUser) {
        return yAlbumMapper.selectMyAlbumByDateDesc(PageUtil.getOffset(page,5),5,yUser.getuId());
    }
}
