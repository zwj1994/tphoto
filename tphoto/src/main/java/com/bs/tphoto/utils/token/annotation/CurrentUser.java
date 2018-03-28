package com.bs.tphoto.utils.token.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在Controller的方法参数中使用此注解，该方法在映射时会注入当前登录的User对象
 * @see com.bs.tphoto.utils.token.annotation
 * @author zwj
 * @date 2015/7/31.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
