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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  相册
 */

@RestController
@RequestMapping("/yalbum")
@PropertySource({"classpath:config/config.properties"})
public class YAlbumController {

    @Value("${local.upload.images.dir}")
    private String filepath;

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
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (FileModel fileModel : files) {
                YPhoto yPhoto = new YPhoto();
                yPhoto.setaId(aId);
                yPhoto.setpBig(fileModel.getName());
                yPhoto.setpSmall(fileModel.getName());
                yPhoto.setpId(UUID.randomUUID().toString());
                yPhotos.add(yPhoto);

                byte[] b = Base64Coder.decodeLines(fileModel.getFile());
                FileOutputStream fos = new FileOutputStream(filepath + fileModel.getName());
                fos.write(b);
                fos.flush();
                fos.close();
            }

            YAlbum yAlbum = new YAlbum();
            yAlbum.setaCover(files.get(0).name);
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

    private final ResourceLoader resourceLoader;

    @Autowired
    public YAlbumController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filepath + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    // 上传文件存储目录
    private static final String uploadPath = "e:/ceshi";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    @PostMapping("/createAlbumNew")
    @Authorization
    public void createAlbumNew(HttpServletRequest request,HttpServletResponse response,@CurrentUser YUser user){
        String userName = request.getParameter("userName");
        String user1 = user.getuId();
        System.out.println(userName);
        System.out.println(user1);
        response.setCharacterEncoding("utf-8");
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            printMsg(response,"Error: 表单必须包含 enctype=multipart/form-data");
            return;
        }
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
//        System.out.println(System.getProperty("java.io.tmpdir"));
        // 设置临时存储目录
        factory.setRepository(new File("e:/"));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                    }
                }
                printMsg(response,"success");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            printMsg(response,
                    "so big");
        }
    }

    /**
     * 打印消息
     * @param response
     * @throws IOException
     */
    private void printMsg(HttpServletResponse response, String msg){
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println(msg);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
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
