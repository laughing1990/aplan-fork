package com.augurit.aplanmis.common.service.receive.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("建筑施工许可证对象")
@EqualsAndHashCode(callSuper = true)
public class ConstructPermitVo extends AeaHiCertinst {
    @ApiModelProperty("建筑施工许可证编号")
    private String constructPermitCode;
    @ApiModelProperty("二维码")
    private byte[] certBuildQrcode;
    @ApiModelProperty("发证机关名称")
    private String issueOrgName;
    @ApiModelProperty("发证日期-年")
    private String issueYear;
    @ApiModelProperty("发证日期-月")
    private String issueMonth;
    @ApiModelProperty("发证日期-日")
    private String issueDay;
    @ApiModelProperty("建设单位名称")
    private String buildUnitName;
    @ApiModelProperty("工程名称")
    private String projectName;
    @ApiModelProperty("建设地址")
    private String contructAddress;
    @ApiModelProperty("建设规模")
    private String contructScale;

    @ApiModelProperty("合同价格")
    private Double contractPrice;

    @ApiModelProperty("监理单位")
    private String supervisionUnitName;

    @ApiModelProperty("勘察单位")
    private String explorationUnitName;
    @ApiModelProperty("勘察单位项目负责人")
    private String explorationUnitLeader;

    @ApiModelProperty("工程总承包单位")
    private String gczcbUnitName;
    @ApiModelProperty("设计单位名称")
    private String designUnitName;
    @ApiModelProperty("设计单位项目负责人")
    private String designUnitLeader;
    @ApiModelProperty("工程总承包单位项目负责人")
    private String gczcbUnitLeader;
    @ApiModelProperty("施工单位")
    private String constructUnitName;
    @ApiModelProperty("施工单位项目负责人")
    private String constructUnitLeader;

    @ApiModelProperty("总监理工程师")
    private String chiefEngineer;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("合同工期")
    private String contractDuration;

    private List<AeaCert> certs;

    public static ConstructPermitVo buildDemoVo() {
        String issueCode = DateUtils.getDateString("yyyyMMddHHmmss");
        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();
        int day = LocalDateTime.now().getDayOfMonth();
        ConstructPermitVo vo = new ConstructPermitVo();
        vo.setConstructPermitCode("130202201909200101");
        vo.setIssueOrgName("唐山市住房保障和城乡建设管理局");
        vo.setIssueYear(String.valueOf(year));
        vo.setIssueMonth(String.valueOf(month));
        vo.setIssueDay(String.valueOf(day));
        vo.setBuildUnitName("中国南方电网有限责任公司超高压输电公司广州局");
        vo.setProjectName("东芝家用电器制造（南海）有限公司冰箱新建线投入项目（不含氯氟烃）");
//        vo.setProjectName("占地22000平方米，总建筑面积约19000平方米，建设3栋产品仓库，用于存放仪表、排气管、内外饰件、空调、水葙、大型注塑件、新产品开发件等产品");
        vo.setContructAddress("惠州市大亚湾惠州市大亚湾经济技术开发区西区新寮村龙盛二路1号");
        vo.setContructScale("230²");
        vo.setContractPrice(2000.00);
        vo.setSupervisionUnitName("中国南方电网有限责任公司超高压输电公司");
        vo.setExplorationUnitName("江门市新会区侨汇工业单丝有限公司");
        vo.setExplorationUnitLeader("童志龙");
        vo.setConstructUnitName("广东电网有限责任公司湛江供电局");
        vo.setConstructUnitLeader("吴广军");
        vo.setChiefEngineer("李华锋");
        vo.setRemarks("占地22000平方米，总建筑面积约19000平方米，建设3栋产品仓库，用于存放仪表、排气管、内外饰件、空调、水葙、大型注塑件、新产品开发件等产品");
        vo.setDesignUnitName("佛山市莱博特塑料制品有限公司");
        vo.setDesignUnitLeader("郝郝");
        vo.setContractDuration("2019年1月至2020年8月");
        return vo;
    }

    public void buidlUnitAndLink(String applicant, String linkmanName, String unitType) {
        if (StringUtils.isBlank(unitType) || UnitType.DEVELOPMENT_UNIT.getValue().equals(unitType)) {//建设单位
            this.setBuildUnitName(applicant);
        } else if (UnitType.SUPERVISION_UNIT.getValue().equals(unitType)) {//监理单位
            this.setSupervisionUnitName(applicant);
            this.setChiefEngineer(linkmanName);
        } else if (UnitType.SURVEY_UNIT.getValue().equals(unitType)) {//勘察单位
            this.setExplorationUnitName(applicant);
            this.setExplorationUnitLeader(linkmanName);
        } else if (UnitType.DESIGN_UNIT.getValue().equals(unitType)) {//设计单位
            this.setDesignUnitName(applicant);
            this.setDesignUnitLeader(linkmanName);
        } else if (UnitType.CONSTRUCTION_UNIT.getValue().equals(unitType)) {//CONSTRUCTION_UNIT
            this.setConstructUnitName(applicant);
            this.setConstructUnitLeader(linkmanName);
        }
    }
}
