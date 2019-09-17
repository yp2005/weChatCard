package com.weChatCard.config;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.UserRepository;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 登录验证拦截器
 *
 * @Author: yupeng
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UserRepository userRepository;

    @Value("${swagger.enable}")
    private Boolean swaggerEnable;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String referer = request.getHeader("Referer");  // 从 http 请求头中取出 Referer
        if(swaggerEnable && referer != null && referer.indexOf("swagger-ui.html") != -1) { // 给swagger测试开发admin权限
            User admin = this.userRepository.findByUserName(Constants.ADMIN_USER_NAME);
            request.setAttribute("loginUser", admin);
            return true;
        }
        // 判断接口是否需要登录
        LoginRequired classAnnotation = handlerMethod.getBeanType().getAnnotation(LoginRequired.class);
        LoginRequired methodAnnotation = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
        String token = request.getHeader("Authorization");  // 从 http 请求头中取出 token
        // 有 @LoginRequired 注解，需要认证
        if (classAnnotation != null || methodAnnotation != null) {
            if (token == null) {
                throw new BusinessException(Messages.CODE_50401);
            } else {
                token = token.replace("Bearer ", "");
                String userStr = redisClient.get(Constants.USER_TOKEN + token);
                if (userStr != null) {
                    User user = JSONObject.parseObject(userStr, User.class);
                    user.setToken(token);
                    String teacherRequired = "0";
                    String adminRequired = "0";
                    if(classAnnotation != null) {
                        adminRequired = classAnnotation.adminRequired();
                    }
                    if(methodAnnotation != null) {
                        adminRequired = methodAnnotation.adminRequired();
                    }
                    // 需admin权限访问的接口拦截其他用户
                    if(adminRequired.equals("1") && !user.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
                        throw new BusinessException(Messages.CODE_50200);
                    }
                    request.setAttribute("loginUser", user);
                    redisClient.set(Constants.USER_TOKEN + token, userStr, Constants.TOKEN_EXPIRE_TIME);
                } else {
                    throw new BusinessException(Messages.CODE_50401);
                }
            }

        }
        else if(StringUtils.isNotEmpty(token)) {
            token = token.replace("Bearer ", "");
            String userStr = redisClient.get(Constants.USER_TOKEN + token);
            if (userStr != null) {
                User user = JSONObject.parseObject(userStr, User.class);
                user.setToken(token);
                request.setAttribute("loginUser", user);
                redisClient.set(Constants.USER_TOKEN + token, userStr, Constants.TOKEN_EXPIRE_TIME);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private String readRequest(HttpServletRequest request) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}