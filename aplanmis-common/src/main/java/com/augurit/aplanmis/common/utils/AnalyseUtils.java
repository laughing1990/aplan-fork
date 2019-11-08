package com.augurit.aplanmis.common.utils;

import java.math.BigDecimal;

public class AnalyseUtils {
    /**
     *
     * @param past 往期數
     * @param current 本期數
     * @return
     */
    public static Double calculateGrowRate(Long past , Long current) {
        if(past==0 && current == 0){
            return 0.0;
        }else if(past == 0){
            return 1.0;
        }else{
            BigDecimal b1 = new BigDecimal(current-past);
            BigDecimal b2 = new BigDecimal(past);
            BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();

        }
    }
    /**
     * 计算比率，保留两位小数
     * @param
     * @param
     * @return
     */
    public static Double calculateRate(long dividend, long divisor) {
        if(dividend==0 && divisor == 0){
            return 0.0;
        }else if(divisor == 0){
            return 1.0;
        }else{
            BigDecimal b1 = new BigDecimal(dividend);
            BigDecimal b2 = new BigDecimal(divisor);
            BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();
        }
    }
}
