package com.augurit.aplanmis.thirdPlatform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.HttpUtil;
import com.augurit.aplanmis.common.vo.ProUnitLinkVo;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 监管平台联网查询接口对接：以广东省为例
 */
@Service
public class ProjectCodeServiceImpl implements ProjectCodeService {

    private static Logger logger = LoggerFactory.getLogger(ProjectCodeServiceImpl.class);

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private BscDicRegionService bscDicRegionService;

    /**
     * 测试环境：http://tzba.forgov.com:9000/tybm
     * 生产环境：http://www.gdtz.gov.cn/tybm
     */
    private String url = "http://www.gdtz.gov.cn/tybm";

    @Override
    public ProUnitLinkVo getProjectInfo(String requestUrl, String username, String password, String projectCode) {
        if (StringUtils.isEmpty(projectCode) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }

        String captcha = md5((username + password).getBytes());

        if (requestUrl == null || requestUrl.isEmpty()) {
            requestUrl = url;
        }

//        requestUrl = requestUrl + "/check!query2.action?user=" + username + "&captcha=" + captcha + "&proofCode=" + projectCode + "&type=2&ywlsh=";
        requestUrl = requestUrl + "/check!query2.action";


        try {

            Map<String, String> map = new HashMap();
            map.put("user", username);
            map.put("captcha", captcha);
            map.put("proofCode", projectCode);
            map.put("type", "2");
            map.put("ywlsh", "");
            map.put("dataType", "2");
            JSONObject responseJSON = HttpUtil.sendPost(requestUrl, map, null);
            if (responseJSON != null && (
                    Result.完成审批.getCode().equals(responseJSON.getInteger("result")) ||
                            Result.审批中.getCode().equals(responseJSON.getInteger("result")) ||
                            Result.已作废.getCode().equals(responseJSON.getInteger("result")))) {

                String ajson = responseJSON.getString("a");//项目信息
                AeaProjInfo aeaProjInfo = new AeaProjInfo();
                if (!StringUtils.isEmpty(ajson)) {
                    JSONObject aObject = JSONObject.parseObject(ajson);
                    if (aObject != null) {
                        String regionCode = aObject.getString("divisionCode");
                        if(!StringUtils.isEmpty(regionCode)){
                            BscDicRegion query=new BscDicRegion();
                            query.setRegionNum(regionCode);
                            List<BscDicRegion> regionList = bscDicRegionService.listBscDicRegion(query);
                            if(regionList.size()>0) regionCode=regionList.get(0).getRegionId();
                        }
                        aeaProjInfo.setDistrict(regionCode);//项目所属行政区划
                        aeaProjInfo.setRegionalism(regionCode);//项目所属行政区划
                        aeaProjInfo.setProjName(aObject.getString("projectName"));//项目名称
                        aeaProjInfo.setCentralCode(aObject.getString("projectCode"));//项目统一编号
                        aeaProjInfo.setLocalCode(aObject.getString("projectCode"));//项目统一编号
                        aeaProjInfo.setProjType(aObject.getString("projectType"));//项目类型
                        aeaProjInfo.setProjNature(aObject.getString("projectNature"));//建设性质
                        aeaProjInfo.setNstartTime(aObject.getString("startYearMonth"));//项目计划开工时间，只显示年份月份；格式办：YYYY-MM，例如:2018-02
                        aeaProjInfo.setEndTime(aObject.getString("endYearMonth"));//项目计划建成时间，只显示年份月份；格式为：YYYY-MM，例如:2021-03。
                        aeaProjInfo.setInvestSum(Double.valueOf(aObject.getString("totalMoney")));//总投资（万元）
                        String jsddStr = aObject.getString("placeCode");
                        String regionIds = "";
                        if (!StringUtils.isEmpty(jsddStr)) {
                            String[] jsddArr = jsddStr.split(",");
                            for (String regionNum : jsddArr) {
                                BscDicRegion query = new BscDicRegion();
                                query.setRegionNum(regionNum);
                                List<BscDicRegion> regionList = bscDicRegionService.listBscDicRegion(query);
                                if (regionList.size() > 0) regionIds += regionList.get(0).getRegionId() + ",";
                            }
                        }
                        aeaProjInfo.setProjectAddress(regionIds.length() == 0 ? null : regionIds.substring(0, regionIds.length() - 1));//建设地点行政区代码

                        aeaProjInfo.setAreaDetailCode(aObject.getString("placeCodeDetail"));//建设地点跨地域详细的行政区划代码，多个行政区划以“,”分割。如辽宁省级项目建设地跨了沈阳市和大连市，值为210100,210200
                        aeaProjInfo.setProjAddr(aObject.getString("placeDetail"));//建设地点详细地址
                        aeaProjInfo.setTheIndustry(aObject.getString("theIndustry"));//所属行业
                        aeaProjInfo.setIndustry(aObject.getString("industry"));//国标行业
                        aeaProjInfo.setScaleContent(aObject.getString("scaleContent"));//建设规模及内容
                        aeaProjInfo.setProjectCreateDate(aObject.getString("applyDate"));//项目申报日期 格式为：yyyy-mm-dd
                        aeaProjInfo.setIsForeign(aObject.getString("foreignAbroadFlag"));//0：否 1：外商投资项目 2：境外投资项目
                        aeaProjInfo.setIsDeleted("0");
                        aeaProjInfo.setCreater(SecurityContext.getCurrentUserName());
                        aeaProjInfo.setCreateTime(new Date());
                        aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());

                        if ("1".equals(aObject.getString("foreignAbroadFlag"))) {//外商投资项目
                            aeaProjInfo.setForeignInvolveSecurity(aObject.getString("isCountrySecurity"));//是否涉及国家安全
                            aeaProjInfo.setForeignInvestmentWay(aObject.getString("investmentMode"));//投资方式
                            aeaProjInfo.setForeignTotalDollar(Double.valueOf(aObject.getString("totalMoneyDollar")));//总投资额折合美元(万元)，精确到小数点后4位，如9000.8899
                            aeaProjInfo.setForeignTotalRate(Double.valueOf(aObject.getString("totalMoneyDollarRate")));//总投资额使用的汇率（人民币/美元），精确到小数点后4位
                            aeaProjInfo.setForeignCapital(Double.valueOf(aObject.getString("projectCapitalMoney")));//项目资本金(万元)（人民币），精确到小数点后4位
                            aeaProjInfo.setForeignCapitalDollar(Double.valueOf(aObject.getString("projectCapitalMoneyDollar")));//项目资本金折合美元(万元)（美元），精确到小数点后4位
                            aeaProjInfo.setForeignCapitalRate(Double.valueOf(aObject.getString("projectCapitalMoneyDollarRate")));//项目资本金使用的汇率（人民币/美元）
                            aeaProjInfo.setForeignPolicyType(aObject.getString("industrialpolicyType"));//适用产业政策条目类型
                            aeaProjInfo.setForeignPolicyItem(aObject.getString("industrialPolicy"));//适用产业政策条目
                            aeaProjInfo.setForeignLandWay(aObject.getString("getLandMode"));//土地获取方式
                            aeaProjInfo.setForeignLandArea(Double.valueOf(aObject.getString("landArea")));//总用地面积（平方米） ，精确到小数点后4位
                            aeaProjInfo.setForeignBuildingArea(Double.valueOf(aObject.getString("builtArea")));//总建筑面积（平方米），精确到小数点后4位
                            aeaProjInfo.setForeignIsAddEquipment(aObject.getString("isAddDevice"));//是否新增设备
                            aeaProjInfo.setForeignEquipmentNum(aObject.getString("importSeviceNumbermoney"));//拟进口设备数量及金额
                        }
                    }
                }

                JSONArray jsonArray = responseJSON.getJSONArray("e");//企业信息和联系人信息
                AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
                AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
                if (jsonArray != null && jsonArray.size() > 0) {
                    //企业信息
                    JSONObject ebject = jsonArray.getJSONObject(0);
                    aeaUnitInfo.setApplicant(ebject.getString("enterpriseName"));//企业名称
                    aeaUnitInfo.setApplicantDistrict(ebject.getString("deAreaname"));//行政区（园区）
                    aeaUnitInfo.setApplicantDetailSite(ebject.getString("address"));//具体地址
                    aeaUnitInfo.setUnitType("1");//单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构',
                    aeaUnitInfo.setManagementScope(ebject.getString("area"));//经营范围
                    aeaUnitInfo.setUnitNature("1");//单位性质：1 企业，2 事业单位，3 社会组织
                    aeaUnitInfo.setPostalCode(ebject.getString("zipCode"));//邮政编码
                    aeaUnitInfo.setInduCommRegNum(ebject.getString("certno"));//工商登记号,企业营业执照注册号
                    aeaUnitInfo.setOrganizationalCode(ebject.getString("organCode"));//组织机构代码
                    aeaUnitInfo.setUnifiedSocialCreditCode(ebject.getString("creditCode"));//统一社会信用代码
                    aeaUnitInfo.setTaxpayerNum("");//纳税人识别号
                    aeaUnitInfo.setCreditFlagNum("");//信用标记码
                    aeaUnitInfo.setRegisteredCapital("");//注册资本
                    aeaUnitInfo.setRegisterAuthority("");//注册登记机关
                    aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaUnitInfo.setIsDeleted("0");
                    aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitInfo.setCreateTime(new Date());

                    //法人信息
                    aeaUnitInfo.setIdrepresentative(ebject.getString("lerepName"));//法人姓名
                    aeaUnitInfo.setIdmobile(ebject.getString("lerepTel"));//法人手机号码
                    aeaUnitInfo.setIdno(ebject.getString("lerepIdcardNumber"));//法人身份证号码

                    //登录信息
                    aeaUnitInfo.setLoginName(ebject.getString("userName"));//单位登录名
                    aeaUnitInfo.setLoginPwd(ebject.getString("password"));

                    //联系人信息
                    aeaLinkmanInfo.setLinkmanName(ebject.getString("contactName"));
                    aeaLinkmanInfo.setLinkmanMail(ebject.getString("contactEmail"));
                    aeaLinkmanInfo.setLinkmanMobilePhone(ebject.getString("contactTel"));
                    aeaLinkmanInfo.setLinkmanCertNo(ebject.getString("contactIdcardNumber"));
                    aeaLinkmanInfo.setLinkmanType("u");
                    aeaLinkmanInfo.setIsActive("1");
                    aeaLinkmanInfo.setIsDeleted("0");
                    aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserName());
                    aeaLinkmanInfo.setCreateTime(new Date());
                    aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                }

                ProUnitLinkVo proUnitLinkVo = new ProUnitLinkVo();
                proUnitLinkVo.setAeaProjInfo(aeaProjInfo);
                proUnitLinkVo.setAeaUnitInfo(aeaUnitInfo);
                proUnitLinkVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                return proUnitLinkVo;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String md5(byte[] s) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public enum Result {
        审批中(1),
        完成审批(2),
        已作废(3);

        private Integer code;

        Result(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }


    public enum ProjectType {
        审批("A00001"),
        核准("A00002"),
        备案("A00003");

        private String code;

        ProjectType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static String getProjectTypeName(String code) {
            for (ProjectType p : ProjectType.values()) {
                if (p.getCode().equals(code)) {
                    return p.name();
                }
            }

            return null;
        }
    }

    @Override
    public List<AeaProjInfo> getProjInfoFromThirdPlatform(String projCode, String unitName,String unifiedSocialCreditCode) throws Exception {
        List<AeaProjInfo> list = new ArrayList<>();
        ProUnitLinkVo proUnitLinkVo = this.getProjectInfo(null, "swfg2zj", "sw190718a3p", projCode);
        if (proUnitLinkVo == null) throw new Exception("监管平台找不到该项目！");
        if (!StringUtils.isEmpty(proUnitLinkVo.getAeaUnitInfo().getUnifiedSocialCreditCode())
                &&!proUnitLinkVo.getAeaUnitInfo().getUnifiedSocialCreditCode().equals(unifiedSocialCreditCode)){
            throw new Exception("找不到该项目或者当前用户不能申报该项目！");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(unitName)
                && !unitName.equals(proUnitLinkVo.getAeaUnitInfo().getApplicant())) {
            throw new Exception("找不到该项目或者当前用户不能申报该项目！");
        }

        saveProjUnitLinkmanInfo(proUnitLinkVo);

        list.add(proUnitLinkVo.getAeaProjInfo());
        return list;
    }

    @Override
    public void saveProjUnitLinkmanInfo(ProUnitLinkVo proUnitLinkVo) throws Exception {
        if (proUnitLinkVo.getAeaProjInfo() != null) {
            aeaProjInfoService.insertAeaProjInfo(proUnitLinkVo.getAeaProjInfo());
        }
        if (proUnitLinkVo.getAeaLinkmanInfo() != null) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(proUnitLinkVo.getAeaLinkmanInfo().getLinkmanCertNo())) {
                List<AeaLinkmanInfo> list = aeaLinkmanInfoService.getAeaLinkmanInfoListByCertNo(proUnitLinkVo.getAeaLinkmanInfo().getLinkmanCertNo());
                if (list.size() == 0) {
                    aeaLinkmanInfoService.insertAeaLinkmanInfo(proUnitLinkVo.getAeaLinkmanInfo());
                }else{
                    proUnitLinkVo.getAeaLinkmanInfo().setLinkmanInfoId(list.get(0).getLinkmanInfoId());
                }
            } else {
                aeaLinkmanInfoService.insertAeaLinkmanInfo(proUnitLinkVo.getAeaLinkmanInfo());
            }
        }
        if (proUnitLinkVo.getAeaUnitInfo() != null) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(proUnitLinkVo.getAeaUnitInfo().getUnifiedSocialCreditCode())) {
                List<AeaUnitInfo> list = aeaUnitInfoService.getUnitInfoListByIdCard(proUnitLinkVo.getAeaUnitInfo().getUnifiedSocialCreditCode());
                if (list.size() == 0) {
                    aeaUnitInfoService.insertAeaUnitInfo(proUnitLinkVo.getAeaUnitInfo());
                }else{
                    proUnitLinkVo.getAeaUnitInfo().setUnitInfoId(list.get(0).getUnitInfoId());
                }
            } else {
                aeaUnitInfoService.insertAeaUnitInfo(proUnitLinkVo.getAeaUnitInfo());
            }

        }
        if (proUnitLinkVo.getAeaProjInfo() != null && proUnitLinkVo.getAeaUnitInfo() != null) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setUnitInfoId(proUnitLinkVo.getAeaUnitInfo().getUnitInfoId());
            aeaUnitProj.setProjInfoId(proUnitLinkVo.getAeaProjInfo().getProjInfoId());
            aeaUnitProj.setIsOwner("1"); //0表示代建单位，1表示业主/建设单位',
            aeaUnitProj.setUnitType("1");//1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构'

            List<AeaUnitInfo> unitProjs = aeaUnitProjMapper.listProjUnitInfo(aeaUnitProj);
            if (unitProjs.size() == 0) {
                aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
                aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
                aeaUnitProj.setCreateTime(new Date());
                aeaUnitProj.setIsDeleted("0");
                aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
            }
        }
        if (proUnitLinkVo.getAeaLinkmanInfo() != null && proUnitLinkVo.getAeaUnitInfo() != null) {
            AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
            aeaUnitLinkman.setLinkmanInfoId(proUnitLinkVo.getAeaLinkmanInfo().getLinkmanInfoId());
            aeaUnitLinkman.setUnitInfoId(proUnitLinkVo.getAeaUnitInfo().getUnitInfoId());
            List<AeaUnitLinkman> unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(aeaUnitLinkman);
            if (unitLinkmans.size() == 0) {
                aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
                aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserName());
                aeaUnitLinkman.setCreateTime(new Date());
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
            }
        }
    }

}
