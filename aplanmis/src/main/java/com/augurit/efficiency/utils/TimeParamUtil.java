package com.augurit.efficiency.utils;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Ma Yanhao
 * @date 2019/11/18 9:25
 */
public class TimeParamUtil {

    /**
     * 检查时间参数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public static ResultForm checkTimeParam(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return new ContentResultForm<>(false, null, "请求缺少参数！");
        }

        if (startTime.compareTo(endTime) > 0) {
            return new ContentResultForm<>(false, null, "开始日期大于结束日期！");
        }
        try {
            Date _startTime = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date _endTime = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
        } catch (ParseException e) {
            return new ContentResultForm<>(false, null, "传入日期格式错误！");
        }

        return null;
    }

    /**
     * 检查时间参数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public static ResultForm checkTimeParam(String startTime, String endTime, String format) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return new ContentResultForm<>(false, null, "请求缺少参数！");
        }

        if (startTime.compareTo(endTime) > 0) {
            return new ContentResultForm<>(false, null, "开始日期大于结束日期！");
        }
        try {
            Date _startTime = DateUtils.convertStringToDate(startTime, format);
            Date _endTime = DateUtils.convertStringToDate(endTime, format);
        } catch (ParseException e) {
            return new ContentResultForm<>(false, null, "传入日期格式错误！");
        }

        return null;
    }

}
