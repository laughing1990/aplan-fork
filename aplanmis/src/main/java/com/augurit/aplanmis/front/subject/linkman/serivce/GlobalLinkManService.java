package com.augurit.aplanmis.front.subject.linkman.serivce;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.utils.Md5Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class GlobalLinkManService {

    private static Logger logger = LoggerFactory.getLogger(GlobalLinkManService.class);

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
//    @Autowired
//    private BpmProcessRestService bpmProcessRestService;
//    @Autowired
//    private AeaBusinessService aeaBusinessService;

    public void saveAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        List<AeaLinkmanInfo> links = aeaLinkmanInfoService.getAeaLinkmanInfoListByCertNo(aeaLinkmanInfo.getLinkmanCertNo());
        if (links.size() > 0) throw new Exception("该身份证号已注册用户！");
        aeaLinkmanInfo.setCreateTime(new Date());
        aeaLinkmanInfo.setIsActive(Status.ON);
        aeaLinkmanInfo.setIsDeleted(Status.OFF);
        aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);
    }

    public void updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        List<AeaLinkmanInfo> links = aeaLinkmanInfoService.getAeaLinkmanInfoListByCertNo(aeaLinkmanInfo.getLinkmanCertNo());
        if (links.size() > 0 && !aeaLinkmanInfo.getLinkmanInfoId().equals(links.get(0).getLinkmanInfoId()))
            throw new Exception("该身份证号已注册用户！");
        aeaLinkmanInfo.setModifyTime(new Date());
        aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);
    }

    public void deleteAeaLinkmanInfoById(String id) throws Exception {
        Assert.notNull(id, "id is null.");
        aeaLinkmanInfoMapper.deleteAeaLinkmanInfo(id);
    }

    public PageInfo<AeaLinkmanInfo> listAeaLinkMans(String keyword, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findLinkmanInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    public AeaLinkmanInfo getAeaLinkmanInfoById(String id) throws Exception {
        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(id);
        aeaLinkmanInfo.setLinkmanType(aeaLinkmanInfo.getLinkmanType().trim());
        List<AeaUnitInfo> unitInfoList = aeaUnitInfoMapper.getAeaUnitListByLinkmanInfoId(id);
        if(unitInfoList.size()>0){
            aeaLinkmanInfo.setUnitInfoId(aeaUnitInfoMapper.getAeaUnitListByLinkmanInfoId(id).get(0).getUnitInfoId());
            aeaLinkmanInfo.setApplicant(aeaUnitInfoMapper.getAeaUnitListByLinkmanInfoId(id).get(0).getApplicant());
        }
        return aeaLinkmanInfo;
    }

    public void batchDeleteAeaLinkMan(String[] ids) {
        aeaLinkmanInfoMapper.batchLinkmanInfo(ids);
        logger.debug("批量删除联系人成功,ids:{}", JsonUtils.toJson(ids));
    }

    public Map generatePassWord(String linkmanInfoId, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
        if (aeaLinkmanInfo != null) {
            String key = GeneratePasswordUtils.generatePassword(8);
            AeaLinkmanInfo updateParam = new AeaLinkmanInfo();
            updateParam.setLinkmanInfoId(linkmanInfoId);
            if (StringUtils.isNotBlank(key)) updateParam.setLoginPwd(Md5Utils.encrypt32(key));
            String loginName = aeaLinkmanInfo.getLoginName();
            if (StringUtils.isBlank(loginName) && StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanCertNo())) {
                loginName = aeaLinkmanInfo.getLinkmanCertNo();//将身份证号作为登录名
                updateParam.setLoginName(loginName);
            }
            aeaLinkmanInfoMapper.updateAeaLinkmanInfo(updateParam);
            request.getSession().setAttribute("linkmanLoginName", loginName);
            request.getSession().setAttribute("linkmanLoginPwd", key);
            map.put("loginName",loginName);
            map.put("loginPwd",key);
        }
        return map;
    }

    public void beforeSaveOrUpdateAeaUnitLinkman(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        if ("u".equalsIgnoreCase(aeaLinkmanInfo.getLinkmanType().trim())) {//只有单位联系人时，才有的操作
            if (StringUtils.isNotBlank(aeaLinkmanInfo.getApplicant())
                    && StringUtils.isBlank(aeaLinkmanInfo.getUnitInfoId())) {
                //单位有值，但是单位ID为空，说明用户直接填写的单位，没有查询，此时需要先查询是否有该单位
                AeaUnitInfo unitQuery = new AeaUnitInfo();
                unitQuery.setApplicant(aeaLinkmanInfo.getApplicant());
                List<AeaUnitInfo> unitList = aeaUnitInfoMapper.listAeaUnitInfo(unitQuery);
                if (unitList.size() == 0) throw new Exception("没有该单位，请确认后再添加！");
            } else {
                if (aeaLinkmanInfo.getUnitInfoId() == null) throw new Exception("单位为空，请确认后再添加！");
            }
        }
    }

    public void saveOrUpdateAeaUnitLinkmanByAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        if ("u".equalsIgnoreCase(aeaLinkmanInfo.getLinkmanType().trim())) {//只有单位联系人时，才有的操作
            AeaUnitLinkman query = new AeaUnitLinkman();
            if (StringUtils.isNotBlank(aeaLinkmanInfo.getApplicant())
                    && StringUtils.isBlank(aeaLinkmanInfo.getUnitInfoId())) {
                //单位有值，但是单位ID为空，说明用户直接填写的单位，没有查询，此时需要先查询是否有该单位
                AeaUnitInfo unitQuery = new AeaUnitInfo();
                unitQuery.setApplicant(aeaLinkmanInfo.getApplicant());
                List<AeaUnitInfo> unitList = aeaUnitInfoMapper.listAeaUnitInfo(unitQuery);
                if (unitList.size() == 0) throw new Exception("没有该单位，请确认后再添加！");
                query.setUnitInfoId(unitList.get(0).getUnitInfoId());
            } else {
                if (aeaLinkmanInfo.getUnitInfoId() == null) throw new Exception("单位为空，请确认后再添加！");
                query.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
            }
            query.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            List<AeaUnitLinkman> list = aeaUnitLinkmanMapper.listAeaUnitLinkman(query);
            if (list.size() == 0) {//不存在，则新增
                query.setUnitLinkmanId(UUID.randomUUID().toString());
                query.setCreater(SecurityContext.getCurrentUserName());
                query.setCreateTime(new Date());
                String isBindAccoun = aeaLinkmanInfo.getIsBindAccount();
                query.setIsBindAccount(StringUtils.isNotBlank(isBindAccoun) ? isBindAccoun : "1"); //是否绑定账号：1 是 0否
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(query);
            }
        }
    }

    public List<BscAttFileAndDir> getAttFiles(String linkmanInfoId, String tableName, String pkName) throws Exception {
        String[] recordIds = {linkmanInfoId};
        List<BscAttFileAndDir> list = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            list.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), tableName, pkName, recordIds));
        }
        return list;
    }
}
