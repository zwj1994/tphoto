package com.bs.tphoto.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * uuid 随机生成id
 * @author  sanghaiqin
 * @date  2016年7月1日
 */
public class UUIDUtil {
    
    private static SecureRandom random=new SecureRandom();
    //private static final String RANDOM_STRING_RANGE1="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String RANDOM_STRING_RANGE2 ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String RANDOM_NUMBER_RANGE = "0123456789";
    
    
    /**
     * <一句话功能简述>
     * <功能详细描述> 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String get32LenUUId(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    
    /**
     * <一句话功能简述>
     * <功能详细描述>封装JDK自带的UUID, 通过Random数字生成, 中间有-分割
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String get36LenUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>使用SecureRandom随机生成Long. 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static long randomLong(){
        return Math.abs(random.nextLong()+1);
    }
    
  
    
    /**
     * <一句话功能简述>
     * <功能详细描述>根据指定位数随机生成uuid
     * @param lengt
     * @param isNumber
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String randomString(int length, boolean isNumber){
        StringBuilder sb=new StringBuilder(isNumber ?RANDOM_NUMBER_RANGE:RANDOM_STRING_RANGE2);
        StringBuilder resultBuilder=new StringBuilder();
        Random random=new Random();
        int range=sb.length();
        for(int i = 0; i < length; i++){
            resultBuilder.append(sb.charAt(random.nextInt(range)));
        }   
        return resultBuilder.toString();

    }
    
    
    public static void main(String[] args){
//        int length=16;
//        boolean isNumber=false;
//        String randomUUID=UUIDUtil.randomString(length, isNumber);
//        System.out.println("randomUUID:"+randomUUID);
    		  System.out.println(randomLong());
    }
    
    
    
}
