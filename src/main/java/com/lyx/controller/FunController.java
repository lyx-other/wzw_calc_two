package com.lyx.controller;

import com.lyx.common.CommonResult;
import com.lyx.services.FunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunController
{
    @Autowired
    private FunService funService;

    @PutMapping("/calc")
    public CommonResult<String> calc(@RequestParam String filePath, @RequestParam Boolean isCm)
    {
        return funService.calc(filePath, isCm);
    }
}