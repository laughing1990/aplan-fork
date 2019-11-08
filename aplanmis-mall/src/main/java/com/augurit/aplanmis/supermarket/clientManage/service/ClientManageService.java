package com.augurit.aplanmis.supermarket.clientManage.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaImClientService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaImClientServiceMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.supermarket.clientManage.vo.ClientManageVo;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientManageService {
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaImClientServiceMapper aeaImClientServiceMapper;

    /**
     * 根据条件查询企业绑定/未绑定  管理员 等
     *
     * @param clienManageVo
     * @param page
     * @return
     */
    public List<AeaLinkmanInfo> getLinkmanListByCond(ClientManageVo clienManageVo, Page page) {
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(clienManageVo.getUnitInfoId(), clienManageVo.getIsAdministrators(), clienManageVo.getIsBindAccount(), clienManageVo.getKeyword());
        //排序，管理员第一位
        return aeaLinkmanInfos.stream().sorted(Comparator.comparing(AeaLinkmanInfo::getIsBindAccount).reversed()).collect(Collectors.toList());
    }

    /**
     * 设置/解绑管理员，
     */
    public int updateAeaUnitLink(ClientManageVo clienManageVo) throws Exception {
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        //绑定、解绑用户
        if (!StringUtils.isEmpty(clienManageVo.getIsBindAccount())) {
            aeaUnitLinkman = new AeaUnitLinkman();
            aeaUnitLinkman.setUnitInfoId(clienManageVo.getUnitInfoId());
            aeaUnitLinkman.setLinkmanInfoId(clienManageVo.getLinkmanInfoId());
            List<AeaUnitLinkman> aeaUnitLinkmen = aeaLinkmanInfoMapper.listAeaUnitLinkman(aeaUnitLinkman);
            //查询不到，说明绑定的是非本公司人员，需要增加关联关系
            if (aeaUnitLinkmen.size() == 0) {
                aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
                aeaUnitLinkman.setLinkmanInfoId(clienManageVo.getLinkmanInfoId());
                aeaUnitLinkman.setUnitInfoId(clienManageVo.getUnitInfoId());
                aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserName());
                aeaUnitLinkman.setCreateTime(new Date());
                aeaLinkmanInfoMapper.insertUnitLinkman(aeaUnitLinkman);
            } else {
                aeaUnitLinkman = aeaUnitLinkmen.get(0);
            }
            //更新登录账号，如果解绑，置空登录名和密码；绑定：增加登录名和密码
            AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
            BeanUtils.copyProperties(clienManageVo, aeaLinkmanInfo);
            aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);

            // 增加、删除业务授权人关联
            aeaImClientServiceMapper.deleteAeaImClientServiceByUnitLinkmanId(aeaUnitLinkman.getUnitLinkmanId());
            if (!StringUtils.isEmpty(clienManageVo.getUnitServiceIds()) && "1".equals(clienManageVo.getIsBindAccount())) {
                for (String unitServiceId : clienManageVo.getUnitServiceIds().split(",")) {
                    AeaImClientService aeaImClientService = new AeaImClientService();
                    aeaImClientService.setClientServiceId(UUID.randomUUID().toString());
                    aeaImClientService.setUnitLinkmanId(aeaUnitLinkman.getUnitLinkmanId());
                    aeaImClientService.setUnitServiceId(unitServiceId);
                    aeaImClientService.setIsDeleted("0");
                    aeaImClientService.setCreater(SecurityContext.getCurrentUserName());
                    aeaImClientService.setCreateTime(new Date());
                    aeaImClientServiceMapper.insertAeaImClientService(aeaImClientService);
                }
            }
        }
        aeaUnitLinkman.setUnitInfoId(clienManageVo.getUnitInfoId());
        aeaUnitLinkman.setLinkmanInfoId(clienManageVo.getLinkmanInfoId());
        aeaUnitLinkman.setIsAdministrators(clienManageVo.getIsAdministrators());
        aeaUnitLinkman.setIsBindAccount(clienManageVo.getIsBindAccount());
        return aeaLinkmanInfoMapper.updateAeaUnitLinkman(aeaUnitLinkman);
    }

    public List<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page) {
        PageHelper.startPage(page);
        return aeaLinkmanInfoMapper.listUnBindLinkman(aeaLinkmanInfo);
    }
}
