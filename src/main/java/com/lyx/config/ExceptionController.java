package com.lyx.config;

import com.lyx.common.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handException(Exception e) {
        return CommonResult.errorMsg(e.getMessage());
    }
}