package com.example.exception;

import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class XException extends RuntimeException{
    private Integer code;
    public XException(Integer code,String message) {
        super(message);
        this.code = code;
    }
}
