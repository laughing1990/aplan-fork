package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                    import org.springframework.format.annotation.DateTimeFormat;
/**
* 事项输入输出实例表-模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-08-03 10:34:55</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:34:55</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
public class AeaHiItemSpecialStateHist implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String specialStateHistId; // (ID)
        private String specialId; // (事项实例ID)
        private String stateHistId; // (单个事项输入输出定义ID)
        private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getSpecialStateHistId(){
        return specialStateHistId;
    }
    public void setSpecialStateHistId( String specialStateHistId ) {
        this.specialStateHistId = specialStateHistId == null ? null : specialStateHistId.trim();
    }
    public String getSpecialId(){
        return specialId;
    }
    public void setSpecialId( String specialId ) {
        this.specialId = specialId == null ? null : specialId.trim();
    }
    public String getStateHistId(){
        return stateHistId;
    }
    public void setStateHistId( String stateHistId ) {
        this.stateHistId = stateHistId == null ? null : stateHistId.trim();
    }
    public String getCreater(){
        return creater;
    }
    public void setCreater( String creater ) {
        this.creater = creater == null ? null : creater.trim();
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime( java.util.Date createTime ){
        this.createTime = createTime;
    }

    //public String getTableName()  {
    //    return "AeaHiItemSpecialStateHist";
    //}
}
