package com.augurit.aplanmis.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Michael
 * @Date: 2018-11-23 11:18
 * @Version: v0.1
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String FMT_yyyy_MM_dd = "yyyy-MM-dd";

    public static final int TIME_OF_DAY = 24 * 3600 * 1000;

    public static String addDateByWorkDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < day; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (checkHoliday(calendar)) {
                i--;
            }
        }
        return DateUtils.convertDateToString(calendar.getTime(), DateUtils.FMT_yyyy_MM_dd);
    }

    public static boolean checkHoliday(Calendar calendar) {
        //判断日期是否是周六周日
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date convertStringToDate(String dataStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dataStr);
    }

    public static Date formatDate(Date date, String format) throws ParseException {
        return DateUtils.convertStringToDate(DateUtils.convertDateToString(date, format), format);
    }


    /**
     * 计算两个日期之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDay(Date startDate, Date endDate) {
        int workDay = 0;
        Calendar start = toCalendar(startDate);
        Calendar end = toCalendar(endDate);
        while (true) {
            if (DateUtils.checkHoliday(start)) {
                start.add(Calendar.DATE, 1);
                continue;
            }
            if (start.before(end) && !isSameDay(start, end)) {
                workDay++;
                start.add(Calendar.DATE, 1);
                continue;
            }
            return workDay;
        }
    }

    /**
     * 获取本月第一天, e.g.: 2019-01-01 00:00:00
     */
    public static Date firstDayOfMonth(Date now) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_yyyy_MM_dd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDate(convertDateToString(now, FMT_yyyy_MM_dd), FMT_yyyy_MM_dd));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 比较时间
     */
    public static Boolean compareDate(Date date, Date compare) {
        if (date == null && compare == null) {
            return true;
        }
        if (date != null && compare == null) {
            return true;
        }
        if (date == null && compare != null) {
            return false;
        }
        return date.getTime() >= compare.getTime();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DateUtils.firstDayOfMonth(new Date()));
    }
}
