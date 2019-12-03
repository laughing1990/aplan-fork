package com.augurit.aplanmis.supermarket.register.service.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.utils.Md5Utils;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.AgentRegisterVo;
import com.augurit.aplanmis.common.vo.ServiceAndQualVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.augurit.aplanmis.supermarket.certinst.service.AeaHiCertinstService;
import com.augurit.aplanmis.supermarket.clientManage.service.ClientManageService;
import com.augurit.aplanmis.supermarket.clientManage.vo.ClientManageVo;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import com.augurit.aplanmis.supermarket.register.service.AgentRegisterService;
import com.augurit.aplanmis.supermarket.serviceMatter.service.ServiceMatterPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = RuntimeException.class)
@Slf4j
public class AgentRegisterServiceImpl implements AgentRegisterService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    ServiceMatterPublishService serviceMatterPublishService;

    @Autowired
    private ClientManageService clientManageService;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;


    @Override
    public void saveAgentRegisterInfo(AgentRegisterVo agentRegisterVo, HttpServletRequest request) throws Exception {
        AeaUnitInfo unitInfo = agentRegisterVo.getUnitInfo();
        if (unitInfo != null) {
            OpuOmUser opuOmUser=new OpuOmUser();
            opuOmUser.setUserId(unitInfo.getApplicant());
            opuOmUser.setLoginName(unitInfo.getApplicant());
            this.generateVirtualUser(opuOmUser,topOrgId);

            String loginName = unitInfo.getLoginName();
            if (StringUtils.isBlank(loginName) && StringUtils.isNotBlank(unitInfo.getUnifiedSocialCreditCode())) {
                loginName = unitInfo.getUnifiedSocialCreditCode();//将证照号作为登录名
            }
            //随机生成8位密码，注册成功后返回显示
            String password = GeneratePasswordUtils.generatePassword(8);
            agentRegisterVo.setLoginName(loginName);
            agentRegisterVo.setPassword(password);

            unitInfo.setLoginName(loginName);
            unitInfo.setLoginPwd(Md5Utils.encrypt32(password));
            unitInfo.setIsImUnit("1");
            unitInfo.setAuditFlag("2");

            //********保存单位信息
            aeaUnitInfoService.insertAeaUnitInfo(unitInfo);
            if (agentRegisterVo.getContactManInfo() != null) {
                //保存联系人信息
                agentRegisterVo.getContactManInfo().setUnitInfoId(unitInfo.getUnitInfoId());
                agentRegisterVo.getContactManInfo().setLinkmanType("u");
                this.insertLinkmanInfo(agentRegisterVo.getContactManInfo(),unitInfo);
            }
            //上传单位信息相关附件
            List<MultipartFile> unitFiles = this.getFileListByName(request, "unitFile");
            FileUtils.uploadFile("AEA_UNIT_INFO", "UNIT_INFO_ID", unitInfo.getUnitInfoId(), unitFiles);

            /////////////////////////////////////////////


            //********保存单位服务信息
            ServiceMatterVo serviceMatterVo = new ServiceMatterVo();
            if (agentRegisterVo.getServiceAndQualVo() != null) {
                ServiceAndQualVo serviceAndQualVo = agentRegisterVo.getServiceAndQualVo();
                //先保存证件实例表和上传附件
                AeaHiCertinstBVo aeaHiCertinstBVo = serviceAndQualVo.getAeaHiCertinstBVo();
                aeaHiCertinstBVo.setUnitInfoId(unitInfo.getUnitInfoId());
                //保存证件 AEA_HI_CERTINST、上传附件（和证件实例关联）、AEA_IM_CERTINST_MAJOR 证件和专业关联
                AeaHiCertinstBVo aeaHiCertinstBVoResult = serviceMatterPublishService.saveCertificateInfo(aeaHiCertinstBVo, request);

                AeaImUnitService aeaImUnitService = serviceAndQualVo.getAeaImUnitService();
                aeaImUnitService.setUnitInfoId(unitInfo.getUnitInfoId());
                BeanUtils.copyProperties(aeaImUnitService, serviceMatterVo);
                if (StringUtils.isNotBlank(aeaHiCertinstBVoResult.getCertinstId())) {
                    List<AeaHiCertinstBVo> aeaHiCertinstBVoList = new ArrayList<AeaHiCertinstBVo>();
                    AeaHiCertinstBVo certinst = new AeaHiCertinstBVo();
                    certinst.setCertinstId(aeaHiCertinstBVoResult.getCertinstId());
                    aeaHiCertinstBVoList.add(certinst);
                    serviceMatterVo.setCertinstBVos(aeaHiCertinstBVoList);
                }
                //单位和服务关联 AEA_IM_UNIT_SERVICE、单位服务关联表和证件实例关联 AEA_BUS_CERTINST
                serviceMatterPublishService.saveServiceMatter(serviceMatterVo);
            }


            ////////////////////////////////////////

            //********保存受权用户信息
            if (CollectionUtils.isNotEmpty(agentRegisterVo.getAuthorManList())) {
                for (AeaLinkmanInfo aeaLinkmanInfo : agentRegisterVo.getAuthorManList()) {
                    aeaLinkmanInfo.setUnitInfoId(unitInfo.getUnitInfoId());
                    aeaLinkmanInfo.setLinkmanType("u");
                    this.insertLinkmanInfo(aeaLinkmanInfo,unitInfo);

                    String manLoginName = aeaLinkmanInfo.getLoginName();
                    if (StringUtils.isBlank(manLoginName) && StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanCertNo())) {
                        manLoginName = aeaLinkmanInfo.getLinkmanCertNo();//将身份证号作为登录名
                    }
                    String manPassword = GeneratePasswordUtils.generatePassword(8);
                    manPassword=Md5Utils.encrypt32(manPassword);

                    ClientManageVo clienManageVo = new ClientManageVo();
                    clienManageVo.setUnitInfoId(unitInfo.getUnitInfoId());
                    clienManageVo.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
                    clienManageVo.setIsBindAccount("1");
                    //clienManageVo.setLoginName(aeaLinkmanInfo.getLoginName());
                    //clienManageVo.setLoginPwd(aeaLinkmanInfo.getLoginPwd());
                    clienManageVo.setUnitServiceIds(serviceMatterVo.getUnitServiceId());//获取上面关联的服务id
                    //clienManageVo.setUnitServiceIds("7fd47063-345a-4bd7-8f56-e5d9e1c2200e");//获取上面关联的服务id
                    // 服务和委托人员关联表 AEA_IM_CLIENT_SERVICE
                    clientManageService.updateAeaUnitLink(clienManageVo);
                }
            }

            //********执业人员
            //保存证书信息
            //调http://106.52.77.101:8084/aplanmis-mall/supermarket/certinst/uploadAeaHiCertinstFile.do
            //保存人员信息
            AeaLinkmanInfo practiceManInfo = agentRegisterVo.getPracticeManInfo();
            if (practiceManInfo != null) {
                practiceManInfo.setUnitInfoId(unitInfo.getUnitInfoId());
                practiceManInfo.setLinkmanType("u");
                this.insertLinkmanInfo(practiceManInfo,unitInfo);
                //服务和执业人员关联表AEA_IM_SERVICE_LINKMAN、服务人员关联表和证件实例关联 AEA_BUS_CERTINST
                aeaLinkmanInfoService.saveAeaLinkmanInfo(practiceManInfo);
                List<MultipartFile> practiceManFiles = this.getFileListByName(request, "practiceManFile");
                FileUtils.uploadFile("AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", practiceManInfo.getLinkmanInfoId(), practiceManFiles);
            }
        }
        log.debug(unitInfo.getUnitInfoId() + "机构注册成功,账号：" + agentRegisterVo.getLoginName() + "密码：" + agentRegisterVo.getPassword());
    }

    /**
     * 根据表单文件名称获取文件列表
     * @param request
     * @param fileName
     * @return
     */
    private List<MultipartFile> getFileListByName(HttpServletRequest request, String fileName) {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles(fileName);
        return files;
    }


    public void insertLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo,AeaUnitInfo unitInfo) throws Exception {
        aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
        aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserId());
        aeaLinkmanInfo.setCreateTime(new Date());
        aeaLinkmanInfo.setIsActive("1");
        aeaLinkmanInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);

        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
        aeaUnitLinkman.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
        aeaUnitLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
        aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitLinkman.setCreateTime(new Date());
        aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
    }

    /**
     * 生成虚拟登录员，为后续需要获取创建人信息做准备
     * @param opuOmUser
     * @param orgId
     */
    public void generateVirtualUser(OpuOmUser opuOmUser,String orgId){
        OpusLoginUser virtualLoginUser=new OpusLoginUser();
        virtualLoginUser.setCurrentOrgId(orgId);
        virtualLoginUser.setUser(opuOmUser);
        SecurityContext.setCurrentLoginUser(virtualLoginUser,null);
    }
}
