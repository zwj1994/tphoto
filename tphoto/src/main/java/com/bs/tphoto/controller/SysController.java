package com.bs.tphoto.controller;

import com.bs.tphoto.cache.MemoryData;
import com.bs.tphoto.po.Connect;
import com.bs.tphoto.utils.encryption.aes.AESUtil;
import com.bs.tphoto.utils.encryption.rsa.RSACoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/sys")
public class SysController {


    /**
     * 请求公钥
     * @param identifier
     * @return
     */
    @PostMapping("/requestPublicKey")
    public Connect requestPublicKey(@RequestParam String identifier){
        Connect con = null;
        try {
            Map<String, Object> keyMap  = RSACoder.initKey();
            //公钥
            String publicKey = RSACoder.getPublicKey(keyMap);
            //私钥
            String privateKey = RSACoder.getPrivateKey(keyMap);
            //将用户、私钥绑定到内存中
            MemoryData.USERS_PRIVATEKEY.put(identifier.trim(),privateKey);
            con = new Connect(publicKey.trim());
        }catch (Exception e){
            e.printStackTrace();
        }

        return con;
    }

    /**
     * 绑定密钥
     * @param identifier
     * @param key
     * @return
     */
    @PostMapping("/bindKey")
    public String bindKey(@RequestParam String identifier,@RequestParam String key){
        Object privateKey = MemoryData.USERS_PRIVATEKEY.get(identifier);
        if( null == privateKey){
            return "not have privateKey";
        }
        try {
            byte[] decodedData = RSACoder.decryptByPrivateKey(key.getBytes("ISO-8859-1"),privateKey.toString());
            key = new String(decodedData);
            MemoryData.USERS_KEY.put(identifier,key);
            MemoryData.USERS_PRIVATEKEY.remove(identifier);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "ok";
    }

    /**
     * 登录
     * @param identifier
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String identifier,@RequestParam String password){
        System.out.println("密钥工作前：");
        System.out.println("username="+identifier);
        System.out.println("password="+password);
        Object key = MemoryData.USERS_KEY.get(identifier);
        if( null == key){
            return "not have key";
        }

        try {
            password = AESUtil.decrypt(password,key.toString());
            System.out.println("密钥工作后：");
            System.out.println("username="+identifier);
            System.out.println("password="+password);
        } catch (Exception e) {
            e.printStackTrace();
            return  "fail";
        }
        return "ok";
    }

}
