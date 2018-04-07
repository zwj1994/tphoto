package com.bs.tphoto.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bs.tphoto.entity.*;
import com.bs.tphoto.po.WallpaperApiModel;
import com.bs.tphoto.service.YAlbumService;
import com.bs.tphoto.utils.Base64Coder;
import com.bs.tphoto.utils.token.annotation.Authorization;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import com.bs.tphoto.utils.token.model.ResultModel;
import com.bs.tphoto.utils.token.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @PostMapping("/ranking")
    public List<YAlbum> ranking(@RequestParam int page){
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
    @PostMapping("/photo")
    public List<YPhoto> photo(@RequestParam String aId,@RequestParam int page){
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
    @PostMapping("/myAlbum")
    @Authorization
    public List<YAlbum> myAlbum(@RequestParam int page,@CurrentUser YUser user){
        List<YAlbum> list = null;
        try {
            list = yAlbumService.queryMyAlbumByDateDesc(page,user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取所有公开的相册(分页)
     * @param page
     * @return
     */
    @PostMapping("/allPublicAlbum")
    public List<YAlbum> allPublicAlbum(@RequestParam int page){
        List<YAlbum> list = null;
        try {
            list = yAlbumService.queryPublicYalbumByCreateDateDesc(page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 创建相册
     * @param user
     * @param content
     * @param isPrivate
     * @param image_size
     * @param files
     */
    @PostMapping("/createAlbum")
    @Authorization
    public ResponseEntity createAlbum(HttpServletRequest request, @CurrentUser YUser user, @RequestParam String content, @RequestParam boolean isPrivate, @RequestParam int image_size, @RequestBody List<FileModel> files){
        int count = 0;
        try {
            String aId = UUID.randomUUID().toString();
            List<YPhoto> yPhotos = new ArrayList<>();

//            String filepath = request.getServletContext().getRealPath("./") + File.separator + "upload/";
            String filepath = "g:/ftp/youm/upload/";
            String ftppath = "ftp://192.168.31.126:21/youm/upload/";
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (FileModel fileModel : files) {
                YPhoto yPhoto = new YPhoto();
                yPhoto.setaId(aId);
                yPhoto.setpBig(ftppath + fileModel.getName());
                yPhoto.setpSmall(ftppath + fileModel.getName());
                yPhoto.setpId(UUID.randomUUID().toString());
                yPhotos.add(yPhoto);

                byte[] b = Base64Coder.decodeLines(fileModel.getFile());
                FileOutputStream fos = new FileOutputStream(file.getPath()
                        + "/" + fileModel.getName());
                fos.write(b);
                fos.flush();
                fos.close();
            }

            YAlbum yAlbum = new YAlbum();
            yAlbum.setaCover(ftppath + files.get(0).name);
            yAlbum.setaDescribe("");
            yAlbum.setaId(aId);
            yAlbum.setaName(content);
            yAlbum.setaPrivacy(isPrivate ? 1 : 0);
            yAlbum.setuId(user.getuId());
            yAlbum.setaState(0);
            count = yAlbumService.addYalbum(yAlbum);
            if(count > 0){
                count = yAlbumService.addYphoto(yPhotos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(count > 0 ? ResultModel.ok() : ResultModel.error(ResultStatus.FAIL), HttpStatus.OK);
    }


    static class FileModel{
        private String file;
        private String name;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
