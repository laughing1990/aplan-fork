package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Augurit on 2018-08-17.
 */
public class CommonTools {

    //  执行EL表达式
    public static Object convertToCode(String expression, Map<String, Object> map) throws Exception {
        try {
            if (StringUtils.isBlank(expression)) {
                return true;
            }
            // 编译表达式
            Expression compiledExp = AviatorEvaluator.compile(expression);
            if (map == null || map.size() == 0) {
                // 执行表达式
                return compiledExp.execute() instanceof Boolean && (Boolean) compiledExp.execute();
            }
            return compiledExp.execute(map) instanceof Boolean && (Boolean) compiledExp.execute(map);
        } catch (Exception e) {
            return false;
        }
    }

    public static Date addDateByWorkDay(Calendar calendar, int day) {
        try {
            for (int i = 0; i < day; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (checkHoliday(calendar)) {
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    /**
     * 下划线转驼峰
     */
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     */
    private static Pattern pattern = Pattern.compile("[A-Z]");
    public static String humpToLine(String camel) {

        Matcher matcher = pattern.matcher(camel);
        while (matcher.find()) {
            String w = matcher.group().trim();
            camel = camel.replace(w, "_" + w);
        }
        return camel.toUpperCase();
    }

    private static boolean checkHoliday(Calendar calendar) throws Exception {
        //判断日期是否是周六周日
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        //判断日期是否是节假日
//        for (Calendar ca : holidayList) {
//            if (ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
//                    ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
//                    ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
//                return true;
//            }
//        }
        return false;
    }

    public static List<String> getTags(String str) {
        List<String> list = new ArrayList<String>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(str)) {
            String regAll = "\\#\\[[\\w]+\\]";
            Pattern pattern = Pattern.compile(regAll);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String temp = matcher.group();
                list.add(temp.substring(2, temp.length() - 1));
            }
        }
        return list;
    }

    public static Map getProperty(Object object, List<String> tags) throws Exception {
        Field[] field = object.getClass().getDeclaredFields();// 获取实体类的所有属性，返回Field数组
        Map<String, Object> map = new HashMap<String, Object>();
        for (int j = 0; j < field.length; j++) {
            String name = field[j].getName();// 获取属性的名字
            for (String str : tags) {
                if (name.equals(str)) {
                    String str1 = "#[" + str + "]";
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);// 将属性的首字符大写，方便构造get，set方法
                    String type = field[j].getGenericType().toString();// 获取属性的类型
                    Method m = object.getClass().getMethod("get" + name);
                    if ("class java.lang.String".equals(type)) {
                        map.put(str1, (String) m.invoke(object)); // 调用getter方法获取属性值
                    } else if ("class java.lang.Long".equals(type)) {
                        map.put(str1, (Long) m.invoke(object));
                    } else if ("class java.util.Date".equals(type)) {
                        map.put(str1, (Date) m.invoke(object));
                    } else if ("class java.lang.Double".equals(type)) {
                        map.put(str1, (Double) m.invoke(object));
                    } else if ("class java.lang.Boolean".equals(type)) {
                        map.put(str1, (Boolean) m.invoke(object));
                    } else if ("class java.lang.Integer".equals(type)) {
                        map.put(str1, (Integer) m.invoke(object));
                    }
                    break;
                }
            }
        }
        return map;
    }

    /**
     *  JDK8 去重方法
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 判断项目代码是否符合规则
     *
     * @param keyword
     * @return
     */
    public static boolean isComplianceWithRules(String keyword) {
        if (StringUtils.isBlank(keyword)) return false;
        //判断是否包含中文
        if (isContainChinese(keyword)) return false;
        //判断是否符合2019-441900-78-03-056565格式,先简单处理，后期改为正则匹配
        if (keyword.length() != 24) return false;
        String match = "^\\d{4}-\\d{6}-\\d{2}-\\d{2}-\\d{6}$";
        if (!keyword.matches(match)) return false;
        return true;
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String[] ListToArr(List<String> strList) {
        String[] array = new String[strList.size()];
        strList.toArray(array);
        return array;
    }

    public static String[] fitleElements(String[] parallelItemVerIds, List<String> allSssxList) {
        List<String> list = new ArrayList<>();
        if (allSssxList.size() == 0) return parallelItemVerIds;
        if (parallelItemVerIds.length > 0 && allSssxList.size() > 0) {
            for (String str : parallelItemVerIds) {
                if (!allSssxList.contains(str)) {
                    list.add(str);
                }
            }
        }
        return list.size() > 0 ? ListToArr(list) : new String[0];
    }

    public static String createDateNo() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 字符串list下划线转驼峰（默认小驼峰）
     *
     * @param list
     *            源字符串列表
     * @param smallCamel
     *            大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串列表
     */
    public static List<String> underline2Camel(List<String> list, boolean... smallCamel) {
        List<String> newList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                newList.add(underline2Camel(str, smallCamel));
            }
        }
        return newList;
    }

    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line
     *            源字符串
     * @param smallCamel
     *            大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean ... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if((smallCamel.length ==0 || smallCamel[0] ) && matcher.start()==0){
                sb.append(Character.toLowerCase(word.charAt(0)));
            }else{
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line
     *            源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String line = "ARE_YOU_DOU_BI_YELLOWCONG";
        //下划线转驼峰（大驼峰）
        //AreYouDouBiYellowcong
        String camel = underline2Camel(line, false);
        System.out.println(camel);

        //下划线转驼峰（小驼峰）
        //areYouDouBiYellowcong
        String camel2 = underline2Camel(line);
        System.out.println(camel);

        //驼峰转下划线
        //ARE_YOU_DOU_BI_YELLOWCONG
        System.out.println(camel2Underline(camel));
    }
}
