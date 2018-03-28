package com.bs.tphoto.utils.token.service;

import com.bs.tphoto.utils.token.model.TokenModel;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 对token进行操作的接口
 * @author zwj
 * @date 2018/03/28.
 */
public interface TokenManager {

    void setRedis(RedisTemplate redis);

    /**
     * 创建一个token关联上指定用户
     * @param userId 指定用户的id
     * @return 生成的token
     */
     TokenModel createToken(String userId);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
     boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
     TokenModel getToken(String authentication);

    /**
     * 清除token
     * @param userId 登录用户的id
     */
     void deleteToken(String userId);

}