package com.bs.tphoto.utils.token.resolvers;

import com.bs.tphoto.entity.YUser;
import com.bs.tphoto.service.SysService;
import com.bs.tphoto.utils.token.annotation.CurrentUser;
import com.bs.tphoto.utils.token.model.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 增加方法注入，将含有CurrentUser注解的方法参数注入当前登录用户
 * @see com.bs.tphoto.utils.token.resolvers
 * @author zwj
 * @date 2018/03/28.
 */
@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SysService sysService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果参数类型是User并且有CurrentUser注解则支持
        if (parameter.getParameterType().isAssignableFrom(YUser.class) &&
                parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //取出鉴权时存入的登录用户Id
        String currentUserId =  webRequest.getAttribute(Constants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST).toString();
        if (currentUserId != null) {
            //从数据库中查询并返回
            return sysService.findOne(currentUserId);
        }
        throw new MissingServletRequestPartException(Constants.CURRENT_USER_ID);
    }
}
