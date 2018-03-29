package com.bs.tphoto.controller;

import com.bs.tphoto.cache.MemoryData;
import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.po.Connect;
import com.bs.tphoto.po.ResBody;
import com.bs.tphoto.service.SysService;
import com.bs.tphoto.utils.encryption.des.Des3Util;
import com.bs.tphoto.utils.encryption.md5.Md5Utils;
import com.bs.tphoto.utils.encryption.rsa.RSACoder;
import com.bs.tphoto.utils.token.annotation.Authorization;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import com.bs.tphoto.utils.token.model.ResultModel;
import com.bs.tphoto.utils.token.model.ResultStatus;
import com.bs.tphoto.utils.token.model.TokenModel;
import com.bs.tphoto.utils.token.service.TokenManager;
import com.bs.tphoto.utils.token.service.impl.RedisTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/sys")
public class SysController {

    @Autowired
    private SysService sysService;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public ResponseEntity login(@RequestParam String identifier, @RequestParam String username, @RequestParam String password){
        try {
            TokenManager tokenManager = new RedisTokenManager();
            tokenManager.setRedis(redisTemplate);
            Object key = MemoryData.USERS_KEY.get(identifier);
            if( null == key){
                return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_KEY_ERROR), HttpStatus.OK);//密钥不存在
            }
            username = Des3Util.decode(username,key.toString());
            password = Des3Util.decode(password,key.toString());
            password = Md5Utils.MD5Encode(password,"utf-8",true);
            YUser yUser = new YUser();
            yUser.setuAccount(username);
            yUser.setuPwd(password);
            yUser = sysService.login(yUser);
            if(null == yUser){
                return new ResponseEntity<>(ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.OK);//账号或密码不正确
            }
            if(-2 == yUser.getuState()){
                return new ResponseEntity<>(ResultModel.error(ResultStatus.USER_NOT_CAN_LOGIN), HttpStatus.OK);//账号禁用
            }
            TokenModel model = tokenManager.createToken(yUser.getuAccount());
            model.setHeaderImg(yUser.getuHeadImg());
            model.setSign(yUser.getuSign());
            return new ResponseEntity<>(ResultModel.ok(model), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultModel.error(ResultStatus.SYS_ERROR), HttpStatus.OK);//系统异常
        }
    }

    @DeleteMapping("/logout")
    @Authorization
    public ResponseEntity logout(@CurrentUser YUser user) {
        System.out.println(user);
        TokenManager tokenManager = new RedisTokenManager();
        tokenManager.setRedis(redisTemplate);
        tokenManager.deleteToken(user.getuAccount());
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


}
