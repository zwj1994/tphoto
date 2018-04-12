package com.bs.tphoto.controller;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bs.tphoto.entity.*;
import com.bs.tphoto.po.WallpaperApiModel;
import com.bs.tphoto.service.YAlbumService;
import com.bs.tphoto.utils.Base64Coder;
import com.bs.tphoto.utils.ReduceImgUtil;
import com.bs.tphoto.utils.UUIDUtil;
import com.bs.tphoto.utils.token.annotation.Authorization;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import com.bs.tphoto.utils.token.model.ResultModel;
import com.bs.tphoto.utils.token.model.ResultStatus;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 *  相册
 */

@RestController
@RequestMapping("/yalbum")
@PropertySource({"classpath:config/config.properties"})
public class YAlbumController {

    @Value("${local.upload.img.original.dir}")
    private String original_filepath;

    @Value("${local.upload.img.compress.dir}")
    private String compress_filepath;

    @Value("${local.upload.img.compress.threshold}")
    private int compress_threshold;

    @Value("${local.upload.img.compress.quality}")
    private int compress_quality;

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


    private final ResourceLoader resourceLoader;

    @Autowired
    public YAlbumController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/original/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> original(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + original_filepath + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/compress/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> compress(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + compress_filepath + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 创建相册
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value="/createAlbum")
    @Authorization
    public ResponseEntity createAlbum(@RequestParam String content,@RequestParam boolean isPrivate, HttpServletRequest request,@CurrentUser YUser user) {
        int count = 0;
        try {
            List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
            List<String> fileNames = new ArrayList<>();
            if(uploadFile(files,fileNames)){
                String aId = UUIDUtil.get32LenUUId();
                YAlbum yAlbum = new YAlbum();
                yAlbum.setaCover(fileNames.get(0));
                yAlbum.setaDescribe("");
                yAlbum.setaId(aId);
                yAlbum.setaName(content);
                yAlbum.setaPrivacy(isPrivate ? 1 : 0);
                yAlbum.setuId(user.getuId());
                yAlbum.setaState(0);
                count = yAlbumService.addYalbum(yAlbum);
                if(count > 0){
                    List<YPhoto> yPhotos = new ArrayList<>();
                    for(String fileName : fileNames){
                        YPhoto yPhoto = new YPhoto();
                        yPhoto.setaId(aId);
                        yPhoto.setpBig(fileName);
                        yPhoto.setpSmall(fileName);
                        yPhoto.setpId(UUIDUtil.get32LenUUId());
                        yPhotos.add(yPhoto);
                    }
                    count = yAlbumService.addYphoto(yPhotos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回json
        return new ResponseEntity<>(count > 0 ? ResultModel.ok() : ResultModel.error(ResultStatus.FAIL), HttpStatus.OK);
    }

    /**
     * 批量上传文件
     * @param files
     * @param fileNames
     * @return
     */
    private boolean uploadFile(List<MultipartFile> files,List<String> fileNames){
        if(null == files || files.isEmpty() || null == fileNames){
            return false;
        }
        try{
            File targetFile = new File(original_filepath);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            if(null != files && files.size() > 0){
                FileOutputStream out = null;
                for(MultipartFile f : files){
                    String fileName = f.getOriginalFilename();
                    fileNames.add(fileName);
                    byte[] file = f.getBytes();
                    out = new FileOutputStream(original_filepath + fileName);
                    out.write(file);
                    out.flush();
                    out.close();
                    try {
                        if(file.length >= 1024 * compress_threshold){
                            ReduceImgUtil.saveMinPhoto(original_filepath + fileName, compress_filepath , fileName, compress_quality, 1d);
                        }else{
                            ReduceImgUtil.copyFile(original_filepath + fileName,compress_filepath,fileName);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }

}
