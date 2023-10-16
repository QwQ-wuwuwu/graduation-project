package com.example.controller;

import com.example.component.JWTComponent;
import com.example.config.PasswordEncodeConfig;
import com.example.entity.MyToken;
import com.example.exception.XException;
import com.example.pojo.User;
import com.example.repository.UserRepository;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public ResultVo login(@RequestBody Map<String,String> map, HttpServletRequest request , HttpServletResponse response) {
        String number = map.get("number");
        String password = map.get("password");
        Integer role = Integer.valueOf(map.get("role"));
        User user = userRepository.findByNumber(number, role);
        if (user == null || !passwordEncodeConfig.passwordEncoder().matches(password, user.getPassword())) {
            return ResultVo.builder()
                    .code(Code.FAILLOGGIN)
                    .message("账号或密码错误").build();
        }
        String msg = switch (role) { // 返回给前端信息,就算token令牌被破译了，也不知道是什么意思
            case 0 -> "dhjndj87d";
            case 1 -> "64dygcg62";
            case 2 -> "eyegy632h";
            default -> " ";
        };
        MyToken myToken = new MyToken();
        myToken.setId(user.getId());
        myToken.setRole(msg);
        try {
            String json = objectMapper.writeValueAsString(myToken);
            Map<String, Object> token = Map.of("token", json);
            String encode = jwtComponent.encode(token);
            response.addHeader("token",encode);
        } catch (JsonProcessingException e) {
            throw new XException(Code.JSONERROR, "序列化失败");
        }
        return ResultVo.success(Code.SUCCESS,"登录成功！",Map.of("user",user));
    }
}
