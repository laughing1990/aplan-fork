package com.augurit.aplanmis.supermarket.agentService.vo;

import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 小糊涂
 */
@Data
public class AeaItemServiceBasicVo {
    /**
     * (设立依据ID)
     */
    private String basicId;
    /**
     * 业务记录数据
     */
    private String recordId;
    /**
     * 法律条款ID
     */
    private String legalClauseId;
    /**
     * 依据名称
     */
    private String legalName;
    /**
     * 依据文号
     */
    private String basicNo;
    /**
     * 颁发机构
     */
    private String issueOrg;
    /**
     * 条款名称
     */
    private String clauseTitle;
    /**
     * (条款具体内容)
     */
    private String clauseContent;
    /**
     * 设立依据说明
     */
    private String serviceLegalMemo;

    public static List<AeaItemServiceBasicVo> convert2list(List<AeaItemServiceBasic> aeaItemServiceBasics) {
        List<AeaItemServiceBasicVo> copyList = new ArrayList<>();
        AeaItemServiceBasicVo vo = null;
        for (AeaItemServiceBasic aeaItemServiceBasic : aeaItemServiceBasics) {
            vo = new AeaItemServiceBasicVo();
            BeanUtils.copyProperties(aeaItemServiceBasic, vo);
            copyList.add(vo);
        }
        return copyList;
    }
}
