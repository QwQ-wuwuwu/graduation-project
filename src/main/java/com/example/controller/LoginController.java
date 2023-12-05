package com.example.controller;

import com.example.component.JWTComponent;
import com.example.config.PasswordEncodeConfig;
import com.example.pojo.MyToken;
import com.example.exception.XException;
import com.example.dox.User;
import com.example.repository.UserRepository;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncodeConfig passwordEncodeConfig;
    @Autowired
    private JWTComponent jwtComponent;
    @PostMapping("/login")
    public ResultVo login(@RequestBody Map<String,String> map, HttpServletResponse response) {
        String number = map.get("number");
        String password = map.get("password");
        int role = Integer.parseInt(map.get("role"));
        User user = userRepository.findByNumber(number, role);
        if (user == null || !passwordEncodeConfig.passwordEncoder().matches(password, user.getPassword())) {
            return ResultVo.builder()
                    .code(Code.FAILLOGGIN)
                    .message("账号或密码错误").build();
        }
        MyToken myToken = new MyToken();
        myToken.setId(user.getId());
        myToken.setRole(user.getRole());
        try {
            String json = objectMapper.writeValueAsString(myToken);
            Map<String, Object> token = Map.of("token", json);
            String encode = jwtComponent.encode(token);
            // 设置响应头，以暴露自定义响应头字段,很重要，否则前端axios无法获取token
            response.addHeader("Access-Control-Expose-Headers", "Token");
            // 设置自定义响应头字段
            response.addHeader("Token", encode);
        } catch (JsonProcessingException e) {
            throw new XException(Code.JSONERROR, "序列化失败");
        }
        return ResultVo.success(Code.SUCCESS,"登录成功！",Map.of("user",user));
    }
}
