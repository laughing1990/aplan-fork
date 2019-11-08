package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: Michael
 * @Date: 2018-11-23 11:18
 * @Version: v0.1
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String FMT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String FMT_yyyy_MM = "yyyy-MM";
    private static final String yyyyMMdd = "yyyyMMdd";

    public static final int TIME_OF_DAY = 24*3600*1000;

    public static String addDateByWorkDay(Date date,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < day; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if(checkHoliday(calendar)){
                i--;
            }
        }
        return DateUtils.convertDateToString(calendar.getTime(), DateUtils.FMT_yyyy_MM_dd);
    }

    public static boolean checkHoliday(Calendar calendar){
        //判断日期是否是周六周日
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        return false;
    }

    public static String convertDateToString(Date date, String format){
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
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDay(Date startDate, Date endDate){
        int workDay = 0;
        Calendar start = toCalendar(startDate);
        Calendar end = toCalendar(endDate);
        while(true){
            if(DateUtils.checkHoliday(start)) {
                start.add(Calendar.DATE, 1);
                continue;
            }
            if(start.before(end) && !isSameDay(start, end)){
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
     * 获取本月最后一天, e.g.: 2019-01-31 23:59:59
     */
    public static Date lastDayOfMonth(Date last) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(last);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar calen = Calendar.getInstance();
        calen.set(ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),ca.get(Calendar.DAY_OF_MONTH),23,59,59);
        Date monthLastDate = calen.getTime();
        return monthLastDate;
    }

    /**
     * 计算两个日期之间的月度
     * @param startTime yyyy-MM
     * @param endTime   yyyy-MM
     * @return
     */
    public static List<String> calculateMonthly(String startTime, String endTime)throws ParseException{
        List<String> result = new ArrayList<>();
        if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date endDate = dateFormat.parse(startTime);
            Date endDate2 = dateFormat.parse(endTime);
            Calendar c1=Calendar.getInstance();
            Calendar c2=Calendar.getInstance();
            c1.setTime(endDate);
            c2.setTime(endDate2);
            int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
            if(year<0){
//            year = -year;
//            int num = year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            }else {
                if (year == 0) {
                    int num2 = year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
                    for (int i = 1; i <= num2 + 1; i++) {
                        String value = c1.get(Calendar.YEAR) + "-" + (c1.get(Calendar.MONDAY) + i);
                        result.add(value);
                    }
                } else{
                    int y = c1.get(Calendar.YEAR);
                    int y2 = c2.get(Calendar.YEAR);
                    int m = c1.get(Calendar.MONDAY);
                    int num = 12;
                    for (int i = 0; i <= year; i++) {
                        if (y == y2) {
                            num = (c2.get(Calendar.MONDAY) + 1);
                        }
                        for (int k = 1; m+k <= num; k++) {
                            String value = y + "-" + (m + k);
                            result.add(value);
                        }
                        y += 1;
                        m = 0;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 比较时间
     */
    public static Boolean compareDate(Date date, Date compare) {
        if(date == null && compare == null){
            return true;
        }
        if(date != null && compare == null){
            return true;
        }
        if(date == null && compare != null){
            return false;
        }
        return date.getTime() >= compare.getTime();
    }

    /**
     * 获取前一个日期
     * @param timeFormat
     * @return
     */
    public static String getYesterdayByFormat(String timeFormat){
        return LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern(timeFormat));
    }
    /**
     * 获取指定格式的字符串
     * @param timeFormat
     * @return
     */
    public static String getDateString(String timeFormat){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
    }
    /**
     * 获取前n天的字符串日期
     * @param date
     * @param days
     * @return
     */
    public static String getMinusDay(String date ,int days){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(date, dtf);
        return parse.minusDays(days).toString();
    }
    /**
     * 获取指定日期的前一天
     * @param date
     * @return
     */
    public static Date getPreDateByDate(Date date){
        SimpleDateFormat sdf =new SimpleDateFormat(FMT_yyyy_MM_dd);
        String preDateStr = getPreDateByDate(sdf.format(date));
        Date preDate;
        try {
            preDate = sdf.parse(preDateStr);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
        return preDate;
    }
    /**
     * 获取指定日期的前一天
     * @param strData
     * @return
     */
    public static String getPreDateByDate(String strData) {
        String preDate = "";
        SimpleDateFormat sdf =new SimpleDateFormat(FMT_yyyy_MM_dd);
        Date date = null;
        try {
            date = sdf.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 - 1);
        preDate = sdf.format(c.getTime());
        return preDate;
    }
    /**
     * 获取本周周一
     * @param date
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }
    /**
     * 获取本周周日
     * @param date
     * @return
     */
    public static Date getThisWeekSunday(Date date) {
        Date monday = DateUtils.getThisWeekMonday(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(monday);
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }
    /**
     * 获取本年第一天, e.g.: 2019-01-01 00:00:00
     */
    public static Date firstDayOfYear(Date now) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR),0,1,0,0,0);
        return cal.getTime();
    }

    /**
     * 获取本年最后一天, e.g.: 2019-12-31 23:59:59
     */
    public static Date lastDayOfYear(Date now) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR),11,31,23,59,59);
        return cal.getTime();
    }

    /**
     * 获取两个日期字符串之间的日期
     * @param startDay  开始时间
     * @param endDay    结束时间
     * @return
     * @throws ParseException
     */
    public static List<Date> calBetweenDaysDate(String startDay,String endDay) throws ParseException{
        List<Date> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_yyyy_MM_dd);
        Calendar ca = Calendar.getInstance();
        ca.setTime(sdf.parse(startDay));
        long t1 = ca.getTime().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(endDay));
        long t2 = cal.getTime().getTime();
        for(int i=0;t1 <= t2;i++){
            result.add(ca.getTime());
            ca.add(Calendar.DATE,1);
            t1 = ca.getTime().getTime();
        }
        return result;
    }

    /**
     * 获取当月一号到指定日的天数
     * @param date
     * @return
     */
    public static List<String> calFirstDay2Date(Date date) throws ParseException{
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_yyyy_MM_dd);
        Date firstDay = DateUtils.firstDayOfMonth(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(firstDay);
        long t1 = ca.getTime().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDate(date,FMT_yyyy_MM_dd));
        long t2 = cal.getTime().getTime();
        for(int i=0;t1 <= t2;i++){
            result.add(sdf.format(ca.getTime()));
            ca.add(Calendar.DATE,1);
            t1 = ca.getTime().getTime();
        }
        return result;
    }

    /**
     * 获取周一到指定日的天数
     * @param date
     * @return
     */
    public static List<String> calMonday2Date(Date date) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_yyyy_MM_dd);
        Date monday = DateUtils.getThisWeekMonday(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(monday);
        long t1 = ca.getTime().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long t2 = cal.getTime().getTime();
        for(int i=0;t1 <= t2;i++){
            result.add(sdf.format(ca.getTime()));
            ca.add(Calendar.DATE,1);
            t1 = ca.getTime().getTime();
        }
        return result;
    }

    /**
     * 获取本周是一年中第几周
     * @param date
     * @return
     */
    public static int getThisWeekNum(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.YEAR, ca.get(Calendar.YEAR));
        cal.set(Calendar.MONTH,ca.get(Calendar.MONTH));
        cal.set(Calendar.DATE, ca.get(Calendar.DATE));
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 获取指定月份的同比月份
     * @param month yyyy-MM
     * @return
     */
    public static String getLastYearMonth(String month) {
        String lastYearMonth = "";
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR,  -1);
        lastYearMonth = sdf.format(c.getTime());
        return lastYearMonth;
    }

    /**
     * 清零时分秒毫秒
     * @return
     */
    public static Date clearDateTail(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
       return cal1.getTime();
    }

    /**
     * 填充指定日期至最大毫秒
     * @return
     */
    public static Date fullDate(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 999);
        return cal1.getTime();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DateUtils.firstDayOfMonth(new Date()));
    }

    /**
     * 获取上个月第一天  yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String firstDayOfLastMonth() {
      return  LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).toString()+" 00:00:00";
    }
    /**
     * 获取上个月最后一天  yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String lastDayOfLastMonth() {
        return  LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).toString()+" 23:59:59";
    }

    /**
     * 获取上一年本月第一天
     * @return
     */
    public static String firstDayOfLastYearMonth() {
        return LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).toString() + " 00:00:00";
    }

    /**
     *获取上一年本月的最后一天
     * @return
     */
    public static String lastDayOfLastYearMonth() {
        return LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).toString() + " 23:59:59";
    }

    public static String yyyymmdd() {
        return DateTimeFormatter.ofPattern(yyyyMMdd).format(LocalDate.now());
    }

    /**
     * 检验日期合格性
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static boolean checkTimeParam(String startTime, String endTime, String format) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return  false;
        }

        if (startTime.compareTo(endTime) > 0) {
            return false;
        }
        try {
            Date _startTime = DateUtils.convertStringToDate(startTime, format);
            Date _endTime = DateUtils.convertStringToDate(endTime, format);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
