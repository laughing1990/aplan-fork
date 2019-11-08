package com.augurit.aplanmis.supermarket.main.service.login;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.BindUnitInfoVo;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.supermarket.utils.ContentRestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class LoginService {
    @Autowired
    private BscAttMapper bscAttMapper;

    private String topOrgId;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    public void setTopOrgId(String topOrgId) {
        this.topOrgId = topOrgId;
    }

    private ContentRestResult getRestResult(boolean success, String message) {
        ContentRestResult restResult = new ContentRestResult(success);
        restResult.setMessage(message);
        return restResult;
    }


    public ContentRestResult<LoginInfoVo> changeLoginUnitInfo(String unitInfoId, String isOwner, HttpServletRequest request) {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo == null) {
            return getRestResult(false, "查询不到登录用户信息，请重新登录");
        }

        List<BindUnitInfoVo> bindUnitInfoVos = loginInfoVo.getUnitInfos();

        if (bindUnitInfoVos == null || bindUnitInfoVos.isEmpty()) {
            return getRestResult(false, "没有绑定的单位信息");
        }

        BindUnitInfoVo bindUnitInfoVo = null;
        for (BindUnitInfoVo unitInfoVo : bindUnitInfoVos) {
            if (unitInfoVo.getUnitInfoId().equals(unitInfoId)) {

                if ("1".equals(unitInfoVo.getIsImUnit()) && "0".equals(isOwner)) {
                    bindUnitInfoVo = unitInfoVo;
                } else if ("1".equals(unitInfoVo.getIsOwnerUnit()) && "1".equals(isOwner)) {
                    bindUnitInfoVo = unitInfoVo;
                }
            }
        }

        if (bindUnitInfoVo == null) {
            return getRestResult(false, "没有匹配的单位信息");
        }

        loginInfoVo.setIsAdministrators(bindUnitInfoVo.getIsAdministrators());
        loginInfoVo.setUnitId(bindUnitInfoVo.getUnitInfoId());
        loginInfoVo.setIsOwner(isOwner);
        loginInfoVo.setUnitName(bindUnitInfoVo.getApplicant());

        loginInfoVo = SessionUtil.saveLoginInfoVo(request, loginInfoVo);

        return new ContentRestResult(true, loginInfoVo);
    }

    public boolean uploadHeadImage(MultipartFile file, HttpServletRequest request) throws Exception {

        if (file == null) {
            throw new RuntimeException("参数有误");
        }

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo == null) {
            throw new RuntimeException("查询不到登录用户信息，请重新登录");
        }

        String tableName = "AEA_UNIT_INFO";
        String pkName = "UNIT_INFO_ID";
        String recordId = loginInfoVo.getUnitId();


        if (StringUtils.isNotBlank(loginInfoVo.getUserId())) {
            tableName = "AEA_LINKMAN_INFO";
            pkName = "LINKMAN_INFO_ID";
            recordId = loginInfoVo.getUserId();
        }

        List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetail(tableName, pkName,
                recordId, null, topOrgId, null);

        if (!FileUtils.uploadFile(tableName, pkName, recordId, file)) {
            throw new RuntimeException("上传失败");
        }

        if (!bscAttForms.isEmpty()) {
            String[] detailIds = new String[bscAttForms.size()];
            for (int i = 0; i < bscAttForms.size(); i++) {
                detailIds[i] = bscAttForms.get(i).getDetailId();
            }
            FileUtils.deleteFiles(detailIds);
        }

        return true;

    }

    public byte[] getHeadImage(HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo == null) {
            return null;
        }

        String tableName = "AEA_UNIT_INFO";
        String pkName = "UNIT_INFO_ID";
        String recordId = loginInfoVo.getUnitId();

        if (StringUtils.isNotBlank(loginInfoVo.getUserId())) {
            tableName = "AEA_LINKMAN_INFO";
            pkName = "LINKMAN_INFO_ID";
            recordId = loginInfoVo.getUserId();
        }

        List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetail(tableName, pkName,
                recordId, null, topOrgId, null);

        if (!bscAttForms.isEmpty()) {

            bscAttForms.sort((BscAttForm o1, BscAttForm o2) -> {
                if (StringUtils.isNotBlank(o1.getCreaterName()) && StringUtils.isNotBlank(o2.getCreaterName())) {
                    return o2.getCreaterName().compareTo(o1.getCreaterName());
                } else {
                    return 0;
                }
            });

            BscAttForm bscAttForm = bscAttForms.get(0);

            return FileUtils.getFile(bscAttForm.getDetailId());
        }

        return null;
    }
}
