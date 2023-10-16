package com.example.controller;

import com.example.pojo.Process;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PutMapping("/password")
    public ResultVo updatePassword(@RequestBody User user, @RequestAttribute("uid") String uid) {
        String password = user.getPassword();
        Integer integer = userService.updatePassword(uid, password);
        if (integer < 0) {
            return ResultVo.error(Code.ERROR,"更新失败");
        }
        return ResultVo.success(Code.SUCCESS,"更新成功");
    }
    @GetMapping("/process")
    public ResultVo getProcess() throws JsonProcessingException { // 向所有用户展示过程
        List<Process> processes = userService.processList();
        return ResultVo.builder()
                .code(Code.SUCCESS)
                .message("操作成功")
                .data(Map.of("process", processes)).build();
    }
}
