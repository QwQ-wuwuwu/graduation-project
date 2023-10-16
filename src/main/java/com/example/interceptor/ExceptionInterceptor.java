package com.example.interceptor;

import com.example.exception.XException;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionInterceptor {
    @ExceptionHandler(XException.class)
    public ResultVo XException(XException e) {
        return ResultVo.builder()
                .code(e.getCode())
                .message(e.getMessage()).build();
    }
    @ExceptionHandler(Exception.class)
    public ResultVo Exception(Exception e) {
        return ResultVo.builder()
                .code(Code.REQUEST_BAD)
                .message(e.getMessage()).build();
    }
    @ExceptionHandler(IOException.class)
    public ResultVo Exception(IOException e) {
        return ResultVo.builder()
                .code(Code.IO_ERROR)
                .message(e.getMessage()).build();
    }
    @ExceptionHandler(JsonProcessingException.class)
    public ResultVo JsonProcessingException(JsonProcessingException e) {
        return ResultVo.error(Code.JSONERROR,"序列化失败");
    }
}
