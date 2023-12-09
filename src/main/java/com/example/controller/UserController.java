package com.example.controller;

import com.example.dox.Process;
import com.example.dox.User;
import com.example.service.UserService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PutMapping("/password/{newPassword}")
    public ResultVo updatePassword(@PathVariable("newPassword") String newPassword, @RequestAttribute("uid") String uid) {
        Integer integer = userService.updatePassword(uid, newPassword);
        if (integer < 0) {
            return ResultVo.error(Code.ERROR,"更新失败");
        }
        return ResultVo.success(Code.SUCCESS,"更新成功");
    }
    @GetMapping("/process")
    public ResultVo getProcess() { // 向所有用户展示过程
        List<Process> processes = userService.processList();
        return ResultVo.builder()
                .code(Code.SUCCESS)
                .message("操作成功")
                .data(Map.of("process", processes)).build();
    }
}
