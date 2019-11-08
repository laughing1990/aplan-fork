package com.augurit.efficiency.utils;

import com.augurit.agcloud.framework.util.StringUtils;

import java.math.BigDecimal;

public class CalculateUtil {
    /**
     * 计算环比
     * 环比：（今年8月份 - 今年7月份）÷ 今年7月份×100%
     * 环比增长率=（本期数-上期数）÷上期数×100%。
     * @param count      本期数
     * @param preCount  上期数
     */
    public static double calcuLrr(int count,int preCount){
        double lrr = 0.0;
        if(count > 0 && preCount == 0){
            lrr = 1.0;
        }
        if(preCount > 0){
            lrr = (double)(count-preCount)/preCount;
        }
        return lrr;
    }

    /**
     * 计算同比
     * 同比：（今年8月份 - 去年8月份）÷|去年8月份| ×100%
     * 同比增长率=（本期数－同期数）÷ |同期数|×100%
     * @param count             本期数
     * @param lastYearCount    同期数
     */
    public static double calcuOnYoyBasis(int count,int lastYearCount){
        double onYoyBasis = 0.0;
        if(count > 0 && lastYearCount == 0){
            onYoyBasis = 1.0;
        }
        if(lastYearCount > 0){
            onYoyBasis = (double)(count-lastYearCount)/Math.abs(lastYearCount);
        }
        return onYoyBasis;
    }
    /**
     * 保留两位小数
     * @param f
     * @return
     */
    public static double formatDoubleValue(double f){
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 拼接查询字段
     * @return
     */
    public static String getQueryFieldValue(String[] params){
        String result = null;
        if(params != null && params.length>0){
            StringBuilder sb = new StringBuilder();
            for(int i=0,len=params.length;i<len;i++){
                String param = params[i];
                if(StringUtils.isNotBlank(param)){
                    sb.append(",").append("'").append(param).append("'");
                }
            }
            if(sb.length()>0){
                result = sb.toString().substring(1);
            }
        }
        return result;
    }

    /**
     * 计算比率
     * @param preCount
     * @param count
     * @return
     */
    public static double calculRate(long preCount,long count){
        double rate = 0.0;
        if(preCount > 0 && count == 0){
            rate = 1.0;
        }
        if(count > 0){
            rate = (double)preCount/count;
        }
        return rate;
    }
}
