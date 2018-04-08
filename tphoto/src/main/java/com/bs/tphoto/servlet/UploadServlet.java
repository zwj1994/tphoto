package com.bs.tphoto.servlet;

import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.utils.token.annotation.Authorization;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Servlet implementation class UploadServlet
 */
@WebServlet(name = "up", urlPatterns={"/up"},loadOnStartup=1)
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String uploadPath = "e:/ceshi";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB


    /**
     * 上传数据及保存文件
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response){
        String uId = request.getParameter("uId");
        String content = request.getParameter("content");
        String isPrivate = request.getParameter("isPrivate");
        System.out.println(uId);
        System.out.println(content);
        System.out.println(isPrivate);
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

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
//        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
//            String uploadPath = "e:/ceshi";

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
                printMsg(response,"上传成功");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            printMsg(response,
                    "错误信息: 值太大");
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}