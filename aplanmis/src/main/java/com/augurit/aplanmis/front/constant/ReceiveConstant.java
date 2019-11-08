package com.augurit.aplanmis.front.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 回执固定参数
 */
public class ReceiveConstant {


    public static final String[] RECEIVE_TYP = {"1", "2", "3", "4", "5", "6"};
    public static final List<String> RETURNED_CERT_TYPE = Arrays.asList(new String[]{"4", "5", "0"});
    //行政许可-唐山
    public static final List<String> ADMINISTRATIVE_LICENSE_TYPE = Arrays.asList(new String[]{"7", "8", "9", "10", "11"});


    public static final String MAT_TYPE = "1";//物料回执
    public static final String ACCEPT_TYPE = "2";//受理回执
    public static final String REJECT_TYPE = "3";//不受理回执
    public static final String RETURNED_TYPE = "4";//退件回执
    public static final String CERT_TYPE = "5";//领证回执
    public static final String MAT_CORRECT_TYPE = "6";//材料补正回执-部门
    public static final String APPLY_MAT_CORRECT_TYPE = "7";//材料补全回执-窗口
    public static final String ITEM_MAT_CORRECT_MAT_RECEIVE_TYPE = "12";//材料补正物料回执-部门
    public static final String APPLY_MAT_CORRECT_MAT_RECEIVE_TYPE = "13";//材料补全物料回执-部门


    public static final String LICENSE_TYPE8 = "8";//领证回执
    public static final String LICENSE_TYPE9 = "9";//领证回执
    public static final String LICENSE_TYPE10 = "10";//领证回执
    public static final String LICENSE_TYPE11 = "11";//领证回执
    public static final String LICENSE_TYPE0 = "0";//

    //回执对应的html路径---平台默认
    public static final String DEFAULT_RECEIVE = "receive/default/unAccept_advice_note";//默认回执，防止文件不存在报错；
    public static final String MAT_RECEIVE = "receive/ts/mat_receive_list";//1 物料回执
    public static final String ACCEPT_RECEIVE = "receive/ts/ts_mat_receive_advice";//2 受理回执
    public static final String REJECT_RECEIVE = "receive/ts/ts_unAccept_advice_note";// 3 不受理回执
    public static final String RETURNED_RECEIVE = "receive/ts/ts_mat_receive_advice";//4：退件回执
    public static final String CERT_RECEIVE = "receive/ts/administrative_licensing_receipt";// 5：领证回执
    public static final String ITEM_MAT_CORRECT_RECEIVE = "receive/ts/mat_correct_receive_list";//6 材料补正回执-部门
    public static final String APPLY_MAT_CORRECT_RECEIVE = "receive/ts/apply_mat_correct_receive_list";//7 材料补全回执-窗口

    //唐山的
    public static final String LICENSE_RECEIVE6 = "receive/ts/ts_application";//
    public static final String LICENSE_RECEIVE7 = "receive/ts/ts_refuse_accept";//
    public static final String LICENSE_RECEIVE8 = "receive/ts/ts_accept_application";
    public static final String LICENSE_RECEIVE9 = "receive/ts/ts_correction";//唐山市行政许可申请材料补正告知书
    public static final String LICENSE_RECEIVE10 = "receive/ts/ts_approval_form";
    public static final String LICENSE_RECEIVE11 = "receive/ts/ts_refuse_application";
    public static final String LICENSE_RECEIVE0 = "receive/ts/shouli_huizheng";

    //根据回执类型返回对应的html路径
    public static final String getModelByReceiveType(String receiveType) {
        String path = DEFAULT_RECEIVE;
        switch (receiveType) {
            case ITEM_MAT_CORRECT_MAT_RECEIVE_TYPE:
            case APPLY_MAT_CORRECT_MAT_RECEIVE_TYPE:
            case MAT_TYPE:
                path = MAT_RECEIVE;
                break;
            case ACCEPT_TYPE:
                path = ACCEPT_RECEIVE;
                break;
            case REJECT_TYPE:
                path = REJECT_RECEIVE;
                break;
            case RETURNED_TYPE:
                return RETURNED_RECEIVE;
            case CERT_TYPE:
                path = CERT_RECEIVE;
                break;
            case MAT_CORRECT_TYPE:
                path = ITEM_MAT_CORRECT_RECEIVE;
                break;
            case APPLY_MAT_CORRECT_TYPE:
                path = APPLY_MAT_CORRECT_RECEIVE;
                break;
            case LICENSE_TYPE8:
                path = LICENSE_RECEIVE8;
                break;
            case LICENSE_TYPE9:
                path = LICENSE_RECEIVE9;
                break;
            case LICENSE_TYPE10:
                path = LICENSE_RECEIVE10;
                break;
            case LICENSE_TYPE11:
                path = LICENSE_RECEIVE11;
                break;
            case LICENSE_TYPE0:
                path = LICENSE_RECEIVE0;
                break;
            default:
                path = DEFAULT_RECEIVE;
                break;
        }
        return path;

    }

    @Getter
    public static enum ReceiveTypeEnum {
        MAT_TYPE("材料回执", "1"),
        ACCEPT_TYPE("受理回执", "2"),
        REJECT_TYPE("不受理回执", "3"),
        RETURNED_TYPE("退件回执", "4"),
        CERT_TYPE("领证回执", "5"),
        MAT_CORRECT_TYPE("材料补正回执", "6"),
        APPLY_MAT_CORRECT_TYPE("材料补全回执", "7"),
        ITEM_MAT_CORRECT_MAT_RECEIVE("材料补正物料回执", "12"),
        APPLY_MAT_CORRECT_MAT_RECEIVE("材料补全物料回执", "13"),

        LICENSE_TYPE8("行政许可8", "8"),
        LICENSE_TYPE9("行政许可9", "9"),
        LICENSE_TYPE10("行政许可10", "10"),
        LICENSE_TYPE11("行政许可11", "11"),
        TEST("2", "行政许可11"),
        LICENSE_TYPE0("行政许可0", "0");


        private String name;
        private String code;

        ReceiveTypeEnum(String name, String code) {
            this.code = code;
            this.name = name;
        }
    }

}