package com.bs.tphoto.utils;

public class PageUtil {

    /**
     * 获取分页起始位置
     * @param page
     * @param rows
     * @return
     */
    public static int getOffset(int page,int rows){
        if(page < 0){
            page = 1;
        }

        if(rows <= 0){
            rows = 1;
        }
        return  (page-1) * rows ;
    }

    public static void main(String[] args) {
        int offset = getOffset(1,5);
        System.out.println(offset);
    }
}
