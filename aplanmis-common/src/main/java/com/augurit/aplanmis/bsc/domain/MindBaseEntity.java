package com.augurit.aplanmis.bsc.domain;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * 思维导图基础封装类
 *
 * @author hzl
 * @date 2019/4/17
 */
public class MindBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String priority;
    private String progress;
    private String nodeTypeCode;
    private String nodeTypeName;
    private String note;

    //绑定节点类型 ref AeaMindConst.MIND_NODE_TYPE_CODE
    private String bindDestTypeCode;

    //多个用逗号隔开(,)
    private String bindDestId;

    private String isQuestion;

    /*
    绑定目标节点信息相关json
    [{
        'bindDestTypeCode':'item'
        ,'bindDestTypeName':'事项'
        ,'bindDestProfile':'国有使用权登记、企业投资主体备案'
    }]
    */
    private String bindDisplayJson;


    //是否全局 1表示是 0表示否
    //适用于材料
    private String isGlobal;


    //节点扩展属性
    private Map extra;

    // 情形关联流程启动
    private String linkProcessStart;

    /**
     * 情形关联是否告知承诺制
     */
    private String isInformCommit;

    //终止情形
    private String terminateSituation;

    //情形答案个数
    private String situationAnswerNum;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getNodeTypeCode() {
        return nodeTypeCode;
    }

    public void setNodeTypeCode(String nodeTypeCode) {
        this.nodeTypeCode = nodeTypeCode;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBindDestId() {
        return bindDestId;
    }

    public void setBindDestId(String bindDestId) {
        this.bindDestId = bindDestId;
    }
    public String getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(String isGlobal) {
        this.isGlobal = isGlobal;
    }


    public String getBindDestTypeCode() {
        return bindDestTypeCode;
    }

    public void setBindDestTypeCode(String bindDestTypeCode) {
        this.bindDestTypeCode = bindDestTypeCode;
    }

    public String getBindDisplayJson() {
        return bindDisplayJson;
    }

    public void setBindDisplayJson(String bindDisplayJson) {
        this.bindDisplayJson = bindDisplayJson;
    }

    public Map getExtra() {
        return extra;
    }

    public void setExtra(Map extra) {
        this.extra = extra;
    }

    public String getLinkProcessStart() {
        return linkProcessStart;
    }
    public void setLinkProcessStart(String linkProcessStart) {
        this.linkProcessStart = linkProcessStart;
    }
    public String getIsQuestion() {
        return isQuestion;
    }

    public String getTerminateSituation() {
        return terminateSituation;
    }
    public void setTerminateSituation(String terminateSituation) {
        this.terminateSituation = terminateSituation;
    }
    public String getSituationAnswerNum() {
        return situationAnswerNum;
    }
    public void setSituationAnswerNum(String situationAnswerNum) {
        this.situationAnswerNum = situationAnswerNum;
    }
    public void setIsQuestion(String isQuestion) {
        this.isQuestion = isQuestion;
    }

    public String getIsInformCommit() {
        return isInformCommit;
    }

    public void setIsInformCommit(String isInformCommit) {
        this.isInformCommit = isInformCommit;
    }
}
