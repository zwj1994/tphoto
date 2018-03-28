package com.bs.tphoto.entity;

import java.util.Date;

/**
 * @Author:zhuwj
 * @Description: 相册喜欢
 * @Date:Created in 11:35 2018/3/23
 */
public class YAlbumLike {

    private String alId;
    private Date alCreateDate;
    private String aId;
    private String uId;

    public String getAlId() {
        return alId;
    }

    public void setAlId(String alId) {
        this.alId = alId;
    }

    public Date getAlCreateDate() {
        return alCreateDate;
    }

    public void setAlCreateDate(Date alCreateDate) {
        this.alCreateDate = alCreateDate;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "YAlbumLike{" +
                "alId='" + alId + '\'' +
                ", alCreateDate=" + alCreateDate +
                ", aId='" + aId + '\'' +
                ", uId='" + uId + '\'' +
                '}';
    }
}
