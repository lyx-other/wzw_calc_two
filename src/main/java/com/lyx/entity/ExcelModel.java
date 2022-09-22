package com.lyx.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 一个对象是一行excel元素
 */
@Data
@Builder
public class ExcelModel
{
    private BigDecimal s;

    private BigDecimal mama;
}
