package com.lyx.services;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.lyx.common.CommonResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunService
{
    /**
     * 速度是 1.08 m/s
     */
    private static final BigDecimal V = BigDecimal.valueOf(1.08d);

    public CommonResult<String> calc(String filePath, Boolean isCm)
    {
        // 读取excel数据
        Assert.isTrue(FileUtil.exist(filePath), "文件「{}」不存在", filePath);
        List<BigDecimal> excelData = this.readData(filePath);

        return CommonResult.successData("完成");
    }

    /**
     * 读取excel数据
     * @param filePath 文件路径
     * @return 文件数据
     */
    private List<BigDecimal> readData(String filePath)
    {
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<BigDecimal> firstColumn = reader.readColumn(0, 1)
                                            .stream()
                                            .map(Convert::toBigDecimal)
                                            .map(el -> el.divide(BigDecimal.valueOf(100)))
                                            .collect(Collectors.toList());
        List<BigDecimal> secondColumn = reader.readColumn(1, 1)
                                            .stream()
                                            .map(Convert::toBigDecimal)
                                            .map(el -> el.divide(BigDecimal.valueOf(100)))
                                            .collect(Collectors.toList());
        reader.close();
        Assert.isTrue(firstColumn.size()==secondColumn.size(), "数据再列数目不一致");

        return secondColumn;
    }

    // /**
    //  * 核心方法，输入一个MAMA数据，计算输出的帧.
    //  * @param Mama 一个MAMA数据
    //  * @param isCm 这个数据单位是不是cm.
    //  * @return 输出的帧
    //  */
    // private String calc(BigDecimal mama, boolean isCm)
    // {
    //     BigDecimal t;
    //     if (isCm)
    //     {
    //         t = mama.divide(V);
    //     }
    //     else
    //     {
    //
    //     }
    //
    //     return null;
    // }
}
