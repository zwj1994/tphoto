package com.bs.tphoto.controller;

import com.bs.tphoto.entity.YAlbum;
import com.bs.tphoto.entity.YPhoto;
import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.service.YAlbumService;
import com.bs.tphoto.utils.token.annotation.Authorization;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *  相册
 */

@RestController
@RequestMapping("/yalbum")
public class YAlbumController {

    @Autowired
    private YAlbumService yAlbumService;

    /**
     * 获取相册喜欢排名
     * @param page
     * @return
     */
    @GetMapping("/ranking")
    public List<YAlbum> ranking(@RequestParam int page){
        System.out.println("a==========page="+page);
        List<YAlbum> list = null;
        try {
            list = yAlbumService.queryAllByLikeCountDesc(page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取相册内相片
     * @param aId
     * @return
     */
    @GetMapping("/photo")
    public List<YPhoto> photo(@RequestParam String aId,@RequestParam int page){
        System.out.println("aId="+aId+"==page="+page);
        List<YPhoto> list = null;
        try {
            list = yAlbumService.queryPhotosByAId(aId,page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取我的相册
     * @param page
     * @return
     */
    @GetMapping("/myAlbum")
    @Authorization
    public List<YAlbum> myAlbum(@RequestParam int page,@CurrentUser YUser user){
        System.out.println("a==========page="+user);
        List<YAlbum> list = null;
        try {
            list = yAlbumService.queryMyAlbumByDateDesc(page,user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
