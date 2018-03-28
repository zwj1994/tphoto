package com.bs.tphoto.controller;

import com.bs.tphoto.po.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类
 */
@RestController
@RequestMapping("/category")
public class CategoryController {


    /***
     * 获取分类接口
     * @return
     */
    @GetMapping("/getCategoryApi")
    public Map<?,?> getCategoryApi(HttpServletResponse response){

        Map<String,Object> map = new HashMap<>();

        List<CategoryBean> list = new ArrayList<>();

        for(int i = 0;i< 100;i++){
            int num = (int)(Math.random() * 7 + 1);
            CategoryBean bean = new CategoryBean();
            bean.setCover("http://zuvips.com:8080/youm/images/"+num+".jpg");
            bean.setName("远方夕阳"+i);
            bean.setUrl("http://zuvips.com:8080/youm/category/getCategoryChilList");
            list.add(bean);
        }
        map.put("category",list);
        return map;
    }

    /***
     * 获取分类子级集合
     * @return
     */
    @GetMapping("/getCategoryChilList")
    public CategoryDataModel getCategoryChilList(HttpServletResponse response){

        CategoryDataModel model = new CategoryDataModel();
        CategoryDataModel.LinkBean linkBean = new CategoryDataModel.LinkBean();
        linkBean.setNext("http://zuvips.com:8080/youm/category/getCategoryChilList");
        linkBean.setPrev("http://zuvips.com:8080/youm/category/getCategoryChilList");
        model.setLink(linkBean);

        List<CategoryDataModel.DataBean> list = new ArrayList<>();
        for(int i = 0;i< 20;i++){
            int num = (int)(Math.random() * 7 + 1);
            CategoryDataModel.DataBean dataBean = new CategoryDataModel.DataBean();
            dataBean.setBig("http://zuvips.com:8080/youm/images/"+num+".jpg");
            dataBean.setDown(1234);
            dataBean.setDown_stat("1234");
            dataBean.setKey("1234");
            dataBean.setSmall("http://zuvips.com:8080/youm/images/"+num+".jpg");
            list.add(dataBean);
        }
        model.setData(list);
        return model;
    }

    /***
     * 首页
     * @return
     */
    @GetMapping("/homePage")
    public WallpaperApiModel homePage(HttpServletResponse response){
        int num = (int)(Math.random() * 7 + 1);
        WallpaperApiModel model = new WallpaperApiModel();
        model.setRanking("http://zuvips.com:8080/youm/category/ranking");
        model.setBanner("http://zuvips.com:8080/youm/images/"+num+".jpg");
        model.setWallpaper("http://zuvips.com:8080/youm/images/"+num+".jpg");
        model.setRecommend("http://zuvips.com:8080/youm/images/"+num+".jpg");

        List<WallpaperApiModel.EverydayBean> everydays = new ArrayList<>();
        List<WallpaperApiModel.SpecialBean> specials = new ArrayList<>();
        for(int i = 0;i< 20;i++){
            int num1 = (int)(Math.random() * 7 + 1);
            WallpaperApiModel.EverydayBean everyday = new WallpaperApiModel.EverydayBean();
            everyday.setDate("2018-03-22");
            everyday.setImage("http://zuvips.com:8080/youm/images/"+num1+".jpg");
            everyday.setName("测试"+i);
            everyday.setUrl("http://zuvips.com:8080/youm/category/getCategoryChilList");
            everydays.add(everyday);

            int num2 = (int)(Math.random() * 7 + 1);
            WallpaperApiModel.SpecialBean specialBean = new WallpaperApiModel.SpecialBean();
            specialBean.setDesc("未来一定是美好的"+i);
            specialBean.setIcon("http://zuvips.com:8080/youm/images/"+num2+".jpg");
            specialBean.setName("测试出结果"+i);
            specialBean.setUrl("http://zuvips.com:8080/youm/category/getCategoryChilList");
            specials.add(specialBean);
        }
        model.setEveryday(everydays);
        model.setSpecial(specials);
        return model;
    }

    /***
     * 排名
     * @return
     */
    @GetMapping("/ranking")
    public SpecialApiModel ranking(){

        SpecialApiModel model = new SpecialApiModel();
        List<SpecialApiModel.DataBean> beans = new ArrayList<>();
        for(int i = 0;i < 20;i++){
            int num = (int)(Math.random() * 7 + 1);
            SpecialApiModel.DataBean bean = new SpecialApiModel.DataBean();
            bean.setBig("http://zuvips.com:8080/youm/images/"+num+".jpg");
            bean.setDown(12);
            bean.setDown_stat("123");
            bean.setKey("1234");
            bean.setSmall("http://zuvips.com:8080/youm/images/"+num+".jpg");
            beans.add(bean);
        }
        model.setData(beans);
        return model;
    }




}
