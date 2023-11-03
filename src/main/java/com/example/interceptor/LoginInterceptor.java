package com.example.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.component.JWTComponent;
import com.example.pojo.MyToken;
import com.example.exception.XException;
import com.example.vo.Code;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JWTComponent jwtComponent;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            response.setStatus(Code.NOT_LOGGIN);
            throw new XException(Code.NOT_LOGGIN,"未登录，请先登录");
        }
        DecodedJWT decode = jwtComponent.decode(token);
        String s = decode.getClaim("token").asString();
        MyToken myToken = objectMapper.readValue(s, MyToken.class);
        request.setAttribute("uid",myToken.getId());
        request.setAttribute("role",myToken.getRole());
        return true;
    }
}
