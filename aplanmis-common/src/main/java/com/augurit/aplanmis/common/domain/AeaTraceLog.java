package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.framework.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
* 系统操作日志-模型
 *
 * @author jjt
 * @date 2019/10/15
 *
*/
@ApiModel(description = "系统操作日志")
@Data
public class AeaTraceLog implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String logId;

    @ApiModelProperty("日志名称")
    private String logTitle;

    @ApiModelProperty("登记时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date logTime;

    @ApiModelProperty("日志等级")
    private String logLevel;

    @ApiModelProperty("线程名称")
    private String logThread;

    @ApiModelProperty("日志信息")
    private String logMessage;

    @ApiModelProperty("异常信息")
    private String logException;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("岗位ID")
    private String posId;

    @ApiModelProperty("岗位名称")
    private String posName;

    @ApiModelProperty("机构ID")
    private String orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("操作应用名称")
    private String operationApp;

    @ApiModelProperty("操作模块名称")
    private String operationModule;

    @ApiModelProperty("操作功能名称")
    private String operationFunc;

    @ApiModelProperty("操作JAVA类")
    private String operationClass;

    @ApiModelProperty("操作JAVA方法")
    private String operationMethod;

    @ApiModelProperty("操作指令描述")
    private String operationDesc;

    @ApiModelProperty("远程地址")
    private String remoteAddr;

    @ApiModelProperty("用户代理")
    private String userAgent;

    @ApiModelProperty("HTTP请求路径")
    private String requestPath;

    @ApiModelProperty("请求参数")
    private String requestParams;

    @ApiModelProperty("url请求方式")
    private String requestMethod;

    @ApiModelProperty("操作返回结果")
    private String operationResult;

    @ApiModelProperty("操作执行时间")
    private Long operationDuration;

    @ApiModelProperty("根组织id")
    private String rootOrgId;

    /**
     * 扩展查询字段: 关键字查询
     */
    @ApiModelProperty("关键字查询")
    private String keyword;

    /**
     * 扩展查询字段: 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 扩展查询字段: 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 设置请求参数
     * @param paramMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String convertParams(Map paramMap){

        if (paramMap == null){
            return null;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        return params.toString();
    }

    /**
     * 后期放到StringUtils工具类中
     *
     * 缩略字符串（不区分中英文字符）
     * @param str 目标字符串
     * @param length 截取长度
     * @return
     */
    public String abbr(String str, int length) {

        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : StringUtils.replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
