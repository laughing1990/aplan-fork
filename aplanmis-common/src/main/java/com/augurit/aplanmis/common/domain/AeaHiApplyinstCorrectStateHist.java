package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
/**
* 事项输入输出实例表-模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-08-28 17:34:26</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:26</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
@Data
public class AeaHiApplyinstCorrectStateHist implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String applyinstCorrectStateHistId; // (ID)
        private String applyinstCorrectId; // (材料补全实例ID)
        private String applyinstStateHistId; // (申请实例历史记录ID)
        private String creater; // (创建人)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
}
