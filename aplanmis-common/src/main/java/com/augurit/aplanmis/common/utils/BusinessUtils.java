package com.augurit.aplanmis.common.utils;

import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.vo.BusinessZtreeNode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/17
 */
public class BusinessUtils {

    private static int codeIndex = 1;

    private final static int MAX_CODE_INDEX_SIZE = 3;

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 获取自动生成的项目编码
     * @return
     */
    public synchronized static String getAutoProjCode(){

        String dateFormatString = new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(new Date());

        StringBuffer sb =  new StringBuffer(dateFormatString);

        sb.append("-").append(System.currentTimeMillis()).append("-").append(String.format("%03d", codeIndex));

        codeIndex++;

        if(Integer.toString(codeIndex).length()>MAX_CODE_INDEX_SIZE){
            codeIndex = 1;
        }

        return sb.toString();
    }

    /**
     * 将list转为tree，实体类必须继承 BusinessZtreeNode
     * @param sourceNodeList
     * @param <T>
     * @return
     */
    public static <T extends BusinessZtreeNode>List<T> listToTree(List<T> sourceNodeList) {
        List<T> nodeList = new ArrayList();
        for (T node1 : sourceNodeList) {

            boolean mark = false;
            for (T node2 : sourceNodeList) {
                if (node1.getpId() != null && node1.getpId().equals(node2.getId())) {
                    mark = true;
                    if (node2.getChildren() == null) {
                        node2.setChildren(new ArrayList<>());
                    }
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark) {
                nodeList.add(node1);
            }
        }

        return nodeList;
    }

    /**
     * 资质等级排序
     * @param qualLevelList
     */
    public static void sort(List<AeaImQualLevel> qualLevelList){
        qualLevelList.sort((AeaImQualLevel o1, AeaImQualLevel o2)->{
            if(o1.getPriority()!=null && o2.getPriority()!=null){
                return o1.getPriority().compareTo(o2.getPriority());
            }else {
                return 0;
            }
        });
    }

    /**
     * 获取自动生成的合同编码
     * @return
     */
    public synchronized static String getAutoContractCode(){

        StringBuffer sb =  new StringBuffer();

        sb.append(System.currentTimeMillis()).append(String.format("%03d", codeIndex));

        codeIndex++;

        if(Integer.toString(codeIndex).length()>MAX_CODE_INDEX_SIZE){
            codeIndex = 1;
        }

        return sb.toString();
    }

    /**
     * 检查参数，返回参数错误信息
     * @param bindingResult
     * @return
     */
    public static String checkBindingResult(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            if(list!=null && !list.isEmpty()){
                for(ObjectError error:list){
                    FieldError fieldError = (FieldError) error;
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    return field + ":" + message;
                }
            }
        }

        return null;
    }


}
