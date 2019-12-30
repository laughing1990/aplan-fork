package com.augurit.aplanmis.supermarket.register.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.utils.Md5Utils;
import com.augurit.aplanmis.common.vo.OwnerRegisterVo;
import com.augurit.aplanmis.supermarket.clientManage.service.ClientManageService;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import com.augurit.aplanmis.supermarket.register.service.AgentRegisterService;
import com.augurit.aplanmis.supermarket.register.service.OwnerRegisterService;
import com.augurit.aplanmis.supermarket.serviceMatter.service.ServiceMatterPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = RuntimeException.class)
@Slf4j
public class OwnerRegisterServiceImpl implements OwnerRegisterService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    ServiceMatterPublishService serviceMatterPublishService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private ClientManageService clientManageService;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;

    @Autowired
    AgentRegisterService agentRegisterService;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;


    @Override
    public void saveAgentRegisterInfo(OwnerRegisterVo ownerRegisterVo, HttpServletRequest request) throws Exception {
        AeaUnitInfo unitInfo = ownerRegisterVo.getUnitInfo();
        if (unitInfo != null) {

            OpuOmUser opuOmUser = new OpuOmUser();
            opuOmUser.setUserId(unitInfo.getApplicant());
            agentRegisterService.generateVirtualUser(opuOmUser, topOrgId);

            String loginName = unitInfo.getLoginName();
            if (StringUtils.isBlank(loginName) && StringUtils.isNotBlank(unitInfo.getUnifiedSocialCreditCode())) {
                loginName = unitInfo.getUnifiedSocialCreditCode();//将证照号作为登录名
            }
            //随机生成8位密码，注册成功后返回显示
            String password = GeneratePasswordUtils.generatePassword(8);
            ownerRegisterVo.setLoginName(loginName);
            ownerRegisterVo.setPassword(password);

            unitInfo.setLoginName(loginName);
            unitInfo.setLoginPwd(Md5Utils.encrypt32(password));
            unitInfo.setUnitType("1");//建设单位
            unitInfo.setIsOwnerUnit("1");
            unitInfo.setAuditFlag("2");

            //保存单位信息
            aeaUnitInfoService.insertAeaUnitInfo(unitInfo);
            String unitInfoId = unitInfo.getUnitInfoId();
            AeaLinkmanInfo contactManInfo = ownerRegisterVo.getContactManInfo();
            if (contactManInfo != null) {
                //查询联系人是否存在
                String certNo = contactManInfo.getLinkmanCertNo();
                List<AeaLinkmanInfo> linkman = aeaLinkmanInfoMapper.getAeaLinkmanByCertNo(certNo);
                if (linkman.isEmpty()) {
                    contactManInfo.setUnitInfoId(unitInfoId);
                    contactManInfo.setLinkmanType("u");
                    contactManInfo.setIsBindAccount("1");
                    contactManInfo.setIsAdministrators("1");
                    //保存联系人信息
                    this.insertLinkmanInfo(contactManInfo);
                } else {
                    throw new Exception("授权用户已存在");
                }
            }
            List<MultipartFile> unitFiles = this.getFileListByName(request, "unitFile");
            fileUtilsService.uploadAttachments("AEA_UNIT_INFO", "UNIT_INFO_ID", unitInfo.getUnitInfoId(), unitFiles);

            /////////////////////////////////////////////


            //保存受权用户信息
            AeaLinkmanInfo authorManInfo = ownerRegisterVo.getAuthorManInfo();
            if (authorManInfo != null) {
                String certNo = authorManInfo.getLinkmanCertNo();
                if (null == contactManInfo || !contactManInfo.getLinkmanCertNo().equals(certNo)) {
                    //查询联系人是否存在
                    List<AeaLinkmanInfo> linkman = aeaLinkmanInfoMapper.getAeaLinkmanByCertNo(certNo);
                    if (linkman.isEmpty()) {
                        authorManInfo.setUnitInfoId(unitInfo.getUnitInfoId());
                        authorManInfo.setLinkmanType("u");
                        authorManInfo.setIsAdministrators("0");
                        authorManInfo.setIsBindAccount("1");
                        this.insertLinkmanInfo(authorManInfo);
                    } else {
                        throw new Exception("授权用户已存在");
                    }
                }

                List<MultipartFile> authorManFiles = this.getFileListByName(request, "authorManFile");
                fileUtilsService.uploadAttachments("AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", authorManInfo.getLinkmanInfoId(), authorManFiles);

            }
        }
        log.debug(unitInfo.getUnitInfoId() + "机构注册成功,账号：" + ownerRegisterVo.getLoginName() + "密码：" + ownerRegisterVo.getPassword());
    }

    private List<MultipartFile> getFileListByName(HttpServletRequest request, String fileName) {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles(fileName);
        return files;
    }

    public void insertLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {

        aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
        aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserId());
        aeaLinkmanInfo.setCreateTime(new Date());
        aeaLinkmanInfo.setIsActive("1");
        aeaLinkmanInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);

        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
        aeaUnitLinkman.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
        aeaUnitLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
        aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitLinkman.setCreateTime(new Date());
        aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
    }
}
