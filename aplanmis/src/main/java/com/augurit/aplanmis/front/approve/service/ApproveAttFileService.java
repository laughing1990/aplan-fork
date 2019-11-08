package com.augurit.aplanmis.front.approve.service;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttDirMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.service.BscAttDirService;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.front.approve.vo.AttDirTreeVo;
import com.augurit.aplanmis.front.approve.vo.AttDirsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ApproveAttFileService {

    @Autowired
    private BscAttDirMapper bscAttDirMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private BscAttDirService bscAttDirService;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    public AttDirsVo getDirsOrFileList(String dirId, String[] recordIds, String tableName, String attType, HttpServletRequest request, String isSeries) throws Exception {
        //20191016修改，将材料定义作为文件夹返回，附件再挂到材料定义下
        AttDirsVo vo = new AttDirsVo();
        List<BscAttForm> files = new ArrayList<>();
        List<BscAttDir> dirs = new ArrayList<>();
        if (StringUtils.isNotBlank(tableName) && recordIds != null && recordIds.length > 0) {
            String orgId = SecurityContext.getCurrentOrgId();
            if (StringUtils.isBlank(dirId)) {
                dirs = new ArrayList();
                BscAttDir applyDir = new BscAttDir();
                applyDir.setDirId("sbcl_2018_augurit");
                applyDir.setDirName("申办材料");
                applyDir.setDirSeq("sbcl_2018_augurit");
                applyDir.setDirNameSeq("申办材料");
                ((List) dirs).add(applyDir);
                BscAttDir officDir = new BscAttDir();
                officDir.setDirId("pwpf_2018_augurit");
                officDir.setDirName("批文批复");
                officDir.setDirSeq("pwpf_2018_augurit");
                officDir.setDirNameSeq("批文批复");
                ((List) dirs).add(officDir);
                List<BscAttDir> bscAttDirs = bscAttDirService.listRootDirsByBus(orgId, tableName, recordIds);
                ((List) dirs).addAll(bscAttDirs);
                files = bscAttService.findByTableNameAndRecordIdsAndDirId(tableName, recordIds, orgId, dirId, attType);
            } else if (dirId.startsWith("sbcl_dir.")) {
                String matId = dirId.substring(9);
                List<BscAttForm> bscAttForms;
                if ("0".equals(isSeries)) {
                    bscAttForms = bscAttMapper.listUnitApplyAtt(recordIds, orgId, (String) null);
                } else {
                    bscAttForms = bscAttMapper.listApplyAtt(recordIds, orgId, (String) null);
                }
                for (BscAttForm bscAttForm : bscAttForms) {
                    String matinstId = bscAttForm.getRecordId();
                    AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
                    if (aeaHiItemMatinst != null && aeaHiItemMatinst.getMatId().equals(matId)) {
                        files.add(bscAttForm);
                    }
                }
            } else if ("sbcl_2018_augurit".equals(dirId)) {
                List<AeaItemMat> mats = aeaItemMatService.getMatListByApplyinstIdContainsMatinst(recordIds[0], null);
                for (AeaItemMat aeaItemMat : mats) {
                    BscAttDir applyDir = new BscAttDir();
                    applyDir.setDirId("sbcl_dir." + aeaItemMat.getMatId());
                    applyDir.setDirName(aeaItemMat.getMatName());
                    applyDir.setDirSeq("sbcl_2018_augurit." + aeaItemMat.getMatId());
                    applyDir.setDirNameSeq("申办材料." + aeaItemMat.getMatName());
                    dirs.add(applyDir);
                }
            } else if (dirId.startsWith("pwpf_dir.")) {
                String matinstId = dirId.substring(9);
                List<BscAttForm> bscAttForms = bscAttMapper.listOfficAtt(recordIds, orgId, (String) null);
                for (BscAttForm bscAttForm : bscAttForms) {
                    if (bscAttForm.getRecordId().equals(matinstId)) {
                        files.add(bscAttForm);
                    }
                }
            } else if ("pwpf_2018_augurit".equals(dirId)) {
                List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(recordIds[0]);
                if (aeaHiIteminstList.size() > 0) {
                    //第二版
                    String[] iteminstIds = new String[aeaHiIteminstList.size()];
                    for (int i = 0; i < aeaHiIteminstList.size(); i++) {
                        iteminstIds[i] = aeaHiIteminstList.get(i).getIteminstId();
                    }
                    List<AeaHiItemMatinst> outMatinstList = aeaHiItemMatinstService.getMatinstListByIteminstIds(iteminstIds, "0");
                    for (AeaHiItemMatinst matinst : outMatinstList) {
                        BscAttDir applyDir = new BscAttDir();
                        applyDir.setDirId("pwpf_dir." + matinst.getMatinstId());
                        applyDir.setDirName(matinst.getOfficialDocTitle());
                        applyDir.setDirSeq("pwpf_2018_augurit." + matinst.getMatinstId());
                        applyDir.setDirNameSeq("批文批复." + matinst.getOfficialDocTitle());
                        dirs.add(applyDir);
                    }
                } else {
                    files.addAll(bscAttMapper.listOfficAtt(recordIds, orgId, (String) null));
                }
            } else {
                dirs = bscAttDirService.listDirsByParentId(dirId);
                files = bscAttService.findByTableNameAndRecordIdsAndDirId(tableName, recordIds, orgId, dirId, attType);
            }
            vo.setDirs(dirs);
            vo.setFiles(files);
        }

        return vo;
    }

    public PageInfo<BscAttFileAndDir> searchFileAndDirsSimple(String keyword, String orgId, String tableName, String pkName, String[] recordIds, Page page) throws Exception {
        PageHelper.startPage();
        List<BscAttFileAndDir> list = bscAttDetailMapper.searchFileAndDirsSimple(keyword, orgId, tableName, pkName, recordIds);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 附件管理文件树
     *
     * @param dirId
     * @param recordIds
     * @param tableName
     * @param attType
     * @param request
     * @param isSeries
     * @return
     * @throws Exception
     */
    public List<AttDirTreeVo> getDirsOrFileListTree(String dirId, String[] recordIds, String tableName, String attType, HttpServletRequest request, String isSeries) throws Exception {
        List<AttDirTreeVo> list = new ArrayList<>();
        List<BscAttDir> dirs = new ArrayList<>();

        if (StringUtils.isBlank(tableName) || recordIds == null || recordIds.length == 0) return list;
        String orgId = SecurityContext.getCurrentOrgId();
        if (StringUtils.isBlank(dirId)) {
            BscAttDir applyDir = new BscAttDir();
            applyDir.setDirId("sbcl_2018_augurit");
            applyDir.setDirName("申办材料");
            applyDir.setDirSeq("sbcl_2018_augurit");
            applyDir.setDirNameSeq("申办材料");
            ((List) dirs).add(applyDir);
            BscAttDir officDir = new BscAttDir();
            officDir.setDirId("pwpf_2018_augurit");
            officDir.setDirName("批文批复");
            officDir.setDirSeq("pwpf_2018_augurit");
            officDir.setDirNameSeq("批文批复");
            ((List) dirs).add(officDir);
            List<BscAttDir> bscAttDirs = bscAttDirService.listRootDirsByBus(orgId, tableName, recordIds);
            ((List) dirs).addAll(bscAttDirs);
        } else {
            dirs = bscAttDirService.listDirsByParentId(dirId);
        }
        for (BscAttDir dir : dirs) {
            String dirId1 = dir.getDirId();
            AttDirTreeVo vo = new AttDirTreeVo();
            BeanUtils.copyProperties(dir, vo);
            List<BscAttForm> files = new ArrayList<>();
            if (StringUtils.isNotBlank(dirId1) && "sbcl_2018_augurit".equals(dirId1)) {
                if ("0".equals(isSeries)) {
                    files = bscAttMapper.listUnitApplyAtt(recordIds, orgId, (String) null);
                } else {
                    files = bscAttMapper.listApplyAtt(recordIds, orgId, (String) null);
                }
            } else if (StringUtils.isNotBlank(dirId1) && "pwpf_2018_augurit".equals(dirId1)) {
                files = bscAttMapper.listOfficAtt(recordIds, orgId, (String) null);
            } else {
                files = bscAttService.findByTableNameAndRecordIdsAndDirId(tableName, recordIds, orgId, dirId1, attType);
            }
            vo.setFiles(null == files ? new ArrayList<BscAttForm>() : files);
            list.add(vo);
        }


        return list;
    }


}
