package com.augurit.aplanmis.thirdPlatform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaProjApplySplitService;
import com.augurit.aplanmis.common.utils.HttpUtil;
import com.augurit.aplanmis.thirdPlatform.service.ProjectSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class ProjectSplitServiceImpl implements ProjectSplitService {

    @Autowired
    private AeaProjApplySplitService aeaProjApplySplitService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    //    private String url = "https://tzba.forgov.com/tybmcheck";     // 最新的测试环境地址（未启用）
    private String url = "http://tybmcheck.forgov.com:9000/tybmcheck";  // 测试环境地址（已启用）
//    private String url = "http://www.gdtz.gov.cn/tybmcheck";  // 生产环境地址

    @Override
    public String getGCBM(String applySplitId) throws Exception {

        if (StringUtils.isNotBlank(applySplitId)) return null;
        AeaProjApplySplit aeaProjApplySplit = aeaProjApplySplitService.getAeaProjApplySplitById(applySplitId);
        if (aeaProjApplySplit == null || StringUtils.isBlank(aeaProjApplySplit.getProjInfoId()) || StringUtils.isBlank(aeaProjApplySplit.getUnitInfoId()) || StringUtils.isBlank(aeaProjApplySplit.getLeaderOrgId()))
            return null;

        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaProjApplySplit.getProjInfoId());
        AeaUnitInfo unitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(aeaProjApplySplit.getUnitInfoId());
        OpuOmOrg org = opuOmOrgMapper.getOrg(aeaProjApplySplit.getLeaderOrgId());
        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaProjApplySplit.getApplyUserId());
        if (aeaProjInfo == null || unitInfo == null || org == null || linkmanInfo == null) return null;

        JSONObject param = new JSONObject();
        JSONObject projectInfo = new JSONObject();

        //测试环境账号密码
        String username = "jmzjtest";
        String password = "test20200114";

        //生产环境账号密码
//        String username = "swfg2zj";
//        String password = "sw190718a3p";

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(date);
        String captcha = md5((timestamp + username + password + timestamp).getBytes()).toUpperCase();

        //项目信息
        projectInfo.put("SCJD", aeaProjApplySplit.getStageNo());
        projectInfo.put("GCFW", StringUtils.isBlank(aeaProjInfo.getForeignManagement()) ? "" : aeaProjInfo.getForeignManagement());
        projectInfo.put("XMMC", aeaProjInfo.getProjName());
        projectInfo.put("ZTZE", aeaProjInfo.getInvestSum() == null ? 0 : aeaProjInfo.getInvestSum());
        projectInfo.put("JSGMJNR", StringUtils.isBlank(aeaProjInfo.getScaleContent()) ? "" : aeaProjInfo.getScaleContent());
        projectInfo.put("YDMJ", aeaProjInfo.getXmYdmj() == null ? 0 : aeaProjInfo.getXmYdmj());
        projectInfo.put("JZMJ", aeaProjInfo.getBuildAreaSum() == null ? 0 : aeaProjInfo.getBuildAreaSum());

        projectInfo.put("QJDGCDM", "2019-440605-47-03-031018");

        projectInfo.put("DWLX", "01");  //默认是建设单位，01	建设单位，02	施工单位, 03	勘察单位，04	设计单位， 05 监理单位，06 代建单位，07 其他
        projectInfo.put("DWMC", unitInfo.getApplicant());
        projectInfo.put("ZJLX", "A05300");  //证件类型默认是统一社会信用码，A05100	企业营业执照(工商注册号)，A05200	组织机构代码证，A05300	统一社会信用代码，A05900	其他
        projectInfo.put("ZJBH", unitInfo.getUnifiedSocialCreditCode());
        projectInfo.put("FRXM", unitInfo.getIdrepresentative());
        projectInfo.put("LXDH", unitInfo.getIdmobile());
        projectInfo.put("BMMC", org.getOrgName());
        projectInfo.put("BMBM", org.getOrgCode());
        projectInfo.put("DYZYPTBMDM", "2"); //对应中央平台部门代码，待扩展
        projectInfo.put("SPBMDM", "12440605737591298K");    // 工程项目所属审批部门代码，为18位统一社会信用代码，待扩展
        projectInfo.put("SQRXM", linkmanInfo.getLinkmanName());
        projectInfo.put("SQRUID", aeaProjApplySplit.getApplyAccountUid());

        //账号和项目代码
        param.put("user", username);
        param.put("captcha", captcha);
        param.put("timestamp", timestamp);
        param.put("isDirectPass", "1");   //默认免审模式
        param.put("xmdm", aeaProjInfo.getLocalCode());
        param.put("projectInfo", projectInfo);

        JSONObject responseJSON = HttpUtil.sendPostJson(url + "/zj/gcdm/project/create", param);
        System.out.println(responseJSON);
        String engineeringCode = null;
        if (responseJSON != null) {
            if ((RESULT.操作成功.getCode() == responseJSON.getInteger("result")) && (XMZT.通过.code == responseJSON.getInteger("xmzt"))) {
                responseJSON.getString("engineeringCode");//工程编码
                String releaseDate = responseJSON.getString("releaseDate");//赋码日期
            } else {
                String message = responseJSON.getString("message");//错误提示信息
            }
        }

        return engineeringCode;
    }

    public static String md5(byte[] s) {
        // 16进制字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
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

    //项目当前审批状态
    public enum XMZT {
        待审批(00),
        通过(01),
        不通过(02),
        退回修改(03),
        已注销(04),
        已废除(05);

        private Integer code;

        XMZT(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

    public enum RESULT {
        操作成功(1),
        账号名无效(-1),
        验证码无效(-2),
        表单数据验证不通过(-3),
        不可预知错误(-99);

        private Integer code;

        RESULT(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }
}
