package com.augurit.aplanmis.front.approve.vo;

import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * @description 关联申报批文批复列表
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("申报批文批复列表")
public class UnitOfficialDocVo {

    @ApiModelProperty(value = "申报批文批复列表", required = true, dataType="java.utl.List")
    private List<OfficialDocumentInfoVo>  itemMatinst;
    @ApiModelProperty(value = "组织名称", required = true, dataType="string")
    private String  orgName;
    @ApiModelProperty(value = "组织ID", required = true, dataType="string")
    private String  orgId;
    private String userName;


    public static List<UnitOfficialDocVo> toUnitOfficialDocVo(List<Map<String,Object>> unitOfficialDocs){
        List<UnitOfficialDocVo> unitOfficialDocVos = new ArrayList<UnitOfficialDocVo>();
        for(Map<String,Object> map:unitOfficialDocs){
            UnitOfficialDocVo unitOfficialDocVo = new UnitOfficialDocVo();
            unitOfficialDocVo.setItemMatinst((List<OfficialDocumentInfoVo>)map.get("itemMatinst"));
            unitOfficialDocVo.setOrgId((String)map.get("map"));
            unitOfficialDocVo.setOrgName((String)map.get("orgName"));
            unitOfficialDocVos.add(unitOfficialDocVo);
        }
        return  unitOfficialDocVos;
    }



}