package com.lyx.services;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.lyx.common.CommonResult;
import com.lyx.entity.ExcelModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        List<ExcelModel> excelData = this.readData(filePath, isCm);

        // 进行计算
        excelData.forEach(el -> this.calc(el, isCm));

        return CommonResult.successData("完成");
    }

    /**
     * 读取excel数据
     * @param filePath 文件路径
     * @return 文件数据
     */
    private List<ExcelModel> readData(String filePath, boolean isCm)
    {
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<BigDecimal> firstColumn = reader.readColumn(0, 1)
                                            .stream()
                                            .map(Convert::toBigDecimal)
                                            .map(el -> el.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_EVEN))
                                            .collect(Collectors.toList());
        List<BigDecimal> secondColumn = reader.readColumn(1, 1)
                                            .stream()
                                            .map(Convert::toBigDecimal)
                                            .collect(Collectors.toList());
        if (isCm)
        {
            secondColumn.stream().map(el -> el.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_DOWN));
        }
        reader.close();
        Assert.isTrue(firstColumn.size()==secondColumn.size(), "数据两列数目不一致");

        List<ExcelModel> result = CollUtil.newArrayList();
        for (int i = 0; i <= firstColumn.size()-1; i++)
        {
            ExcelModel model = ExcelModel.builder().s(firstColumn.get(i)).mama(secondColumn.get(i)).build();
            result.add(model);
        }

        return result;
    }

    /**
     * 核心方法，输入一行excel数据，计算输出的帧.
     * @param row 一行excel数据
     * @param isCm 这个数据单位是不是cm.
     * @return 输出的帧
     */
    private String calc(ExcelModel row, boolean isCm)
    {
        BigDecimal t;
        if (isCm)
        {
            t = row.getMama().divide(V, 5 , RoundingMode.HALF_EVEN);
        }
        else
        {
            BigDecimal fracChild = row.getS().multiply(BigDecimal.valueOf(Math.sin(Math.toRadians(row.getMama().doubleValue()))));
            BigDecimal fracMother = BigDecimal.valueOf(Math.sin(Math.toRadians(BigDecimal.valueOf(110).subtract(row.getMama()).doubleValue()))).multiply(V);
            t = fracChild.divide(fracMother, 5, RoundingMode.HALF_EVEN);
        }

        // 需要采集的帧数量
        BigDecimal count = BigDecimal.valueOf(1).divide(t, 5, RoundingMode.HALF_EVEN);

        // 需要采集的帧间隔
        BigDecimal stepSize = BigDecimal.valueOf(50).divide(count, 5, RoundingMode.HALF_EVEN);
        int stepSizeRound = NumberUtil.round(stepSize, 0, RoundingMode.CEILING).intValue();

        // 计算帧
        if (stepSizeRound >= 50)
        {
            return "间隔大于等于50";
        }
        List<Integer> numberList = CollUtil.newArrayList();
        for (int i=1; i<=60; i=i+stepSizeRound)
        {
            numberList.add(i);
            if (i >= 50)
            {
                break;
            }
        }
        if (CollUtil.getLast(numberList) != 50)
        {
            numberList.set(numberList.size()-1, 50);
        }
        return numberList.stream().map(StrUtil::toString).collect(Collectors.joining(", "));
    }
}
