package com.bs.tphoto.po;

/**
 * 连接类
 */
public class Connect {

    public Connect(){

    }

    public Connect(String publicKey){
        this.publicKey = publicKey;
    }

    /**
     * 公钥
     */
    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
