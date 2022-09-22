package com.lyx.services;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import com.lyx.common.CommonResult;
import org.springframework.stereotype.Service;

@Service
public class FunService
{
    public CommonResult<String> calc(String filePath, Boolean isCm)
    {
        Assert.isTrue(FileUtil.exist(filePath), "文件「{}」不存在", filePath);

        return CommonResult.successData("完成");
    }
}
