package com.bs.tphoto.service;

import com.bs.tphoto.entity.YAlbum;
import com.bs.tphoto.entity.YPhoto;
import com.bs.tphoto.entity.YUser;

import java.util.List;

/**
 * 相册
 */
public interface YAlbumService {

    /**
     * 根据喜欢数查询所有(由高到低)
     * @param page
     * @return
     */
     List<YAlbum> queryAllByLikeCountDesc(int page);


    /**
     * 根据相册编号查询相片
     * @param aId
     * @return
     */
     List<YPhoto> queryPhotosByAId(String aId,int page);

    /**
     * 根据日期查询我的相册（由高到低）
     * @param page
     * @return
     */
    List<YAlbum> queryMyAlbumByDateDesc(int page,YUser yUser);


    /**
     * 根据创建时间获取所有公开的相册（由高到低）
     * @param page
     * @return
     */
    List<YAlbum> queryPublicYalbumByCreateDateDesc(int page);

    /**
     * 添加相册
     * @param yAlbum
     * @return
     */
    int addYalbum(YAlbum yAlbum);

    /**
     * 添加相片
     * @param yPhotos
     * @return
     */
    int addYphoto(List<YPhoto> yPhotos);

}
