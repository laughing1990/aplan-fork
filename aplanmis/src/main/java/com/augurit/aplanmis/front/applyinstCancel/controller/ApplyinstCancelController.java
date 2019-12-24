package com.augurit.aplanmis.front.applyinstCancel.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.vo.AeaHiApplyinstCancelVo;
import com.augurit.aplanmis.common.apply.vo.AeaHiItemCancelVo;
import com.augurit.aplanmis.common.apply.vo.ApplyinstCancelInfoVo;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancel;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.front.applyinstCancel.service.RestApplyinstCancelService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/applyinst")
@Api(value = "申报撤销申请相关接口", tags = "申报撤销申请相关接口")
@Slf4j
public class ApplyinstCancelController {

    @Autowired
    private RestApplyinstCancelService restApplyinstCancelService;

    @RequestMapping("/checkState")
    public String checkState(String applyinstId) throws Exception {
        return restApplyinstCancelService.checkApplyinstAndIteminstState(applyinstId);
    }

    @RequestMapping("/saveApplyinstCancelInfo")
    public ResultForm saveApplyinstCancelInfo(AeaHiApplyinstCancel aeaHiApplyinstCancel) {
        try {
            String mesg = restApplyinstCancelService.saveRevokeData(aeaHiApplyinstCancel);
            return StringUtils.isBlank(mesg) ? new ResultForm(true, "保存成功!") : new ResultForm(false, mesg);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部出错!");
        }
    }

    @RequestMapping("/saveIteminstCancelInfo")
    public ResultForm saveIteminstCancelInfo(AeaHiItemCancelVo itemCancelVo) {
        try {
            String mesg = restApplyinstCancelService.revokeApplyinst(itemCancelVo);
            return StringUtils.isBlank(mesg) ? new ResultForm(true, "保存成功!") : new ResultForm(false, mesg);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部出错!");
        }
    }

    @RequestMapping("/signUpIteminstCancelTask")
    public ResultForm SignUpIteminstCancelTask(String iteminstCancelId) {
        try {
            restApplyinstCancelService.signUpIteminstCancelTask(iteminstCancelId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部出错!");
        }
        return new ResultForm(true, "保存成功!");
    }

    @RequestMapping("/signUpApplyinstCancelTask")
    public ResultForm SignUpApplyinstCancelTask(String applyinstCancelId, String taskId) {
        try {
            restApplyinstCancelService.signUpApplyinstCancelTask(applyinstCancelId, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部出错!");
        }
        return new ResultForm(true, "保存成功!");
    }

    @RequestMapping("/uploadAttFile")
    public ResultForm uploadAttFile(String attId, String tableName, HttpServletRequest request) {
        try {
            attId = restApplyinstCancelService.uploadAttFile(attId, tableName, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "附件上传失败!");
        }
        return new ContentResultForm(true, attId, "保存成功!");
    }

    @RequestMapping("/deleteAttFile")
    public ResultForm deleteAttFile(String detailIds) {
        try {
            if (StringUtils.isNotBlank(detailIds) && restApplyinstCancelService.deleteAttFile(detailIds)) {
                return new ResultForm(true, "删除成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "附件删除失败!");
    }

    @RequestMapping("/getAttFiles")
    public ResultForm getAttFiles(String attId, String tableName) {
        List<BscAttFileAndDir> fileAndDirs = new ArrayList();
        try {
            if (StringUtils.isNotBlank(attId) && StringUtils.isNotBlank(tableName)) {
                fileAndDirs.addAll(restApplyinstCancelService.getAttFiles(attId, tableName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, fileAndDirs, "获取数据失败！");
        }
        return new ContentResultForm(true, fileAndDirs, "获取数据成功！");
    }

    @RequestMapping("/saveUnitLinkman")
    public ResultForm saveUnitLinkman(String applyinstId, AeaLinkmanInfo linkmanInfo) {
        try {
            restApplyinstCancelService.saveUnitLinkman(applyinstId, linkmanInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存失败!");
        }
        return new ResultForm(true, "保存成功!");
    }

    @RequestMapping("/getLinkmanInfoList")
    public ResultForm getLinkmanInfoList(String applyinstId) {
        List<AeaLinkmanInfo> linkmanInfos = new ArrayList();
        try {
            if (StringUtils.isNotBlank(applyinstId)) {
                linkmanInfos.addAll(restApplyinstCancelService.getLinkmanInfoList(applyinstId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, linkmanInfos, "获取数据失败！");
        }
        return new ContentResultForm(true, linkmanInfos, "获取数据成功！");
    }

    @RequestMapping("/getApplyinstCancelListByIteminstId")
    public ResultForm getApplyinstCancelListByIteminstId(String iteminstId) {
        List<AeaHiApplyinstCancel> aeaHiApplyinstCancels = new ArrayList();
        try {
            if (StringUtils.isNotBlank(iteminstId)) {
                aeaHiApplyinstCancels.addAll(restApplyinstCancelService.getApplyinstCancelListByIteminstId(iteminstId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, aeaHiApplyinstCancels, "获取数据失败！");
        }
        return new ContentResultForm(true, aeaHiApplyinstCancels, "获取数据成功！");
    }

    @RequestMapping("/getApplyinstCancelListByApplyinstId")
    public ResultForm getApplyinstCancelListByApplyinstId(String applyinstId) {
        List<AeaHiApplyinstCancel> aeaHiApplyinstCancels = new ArrayList();
        try {
            if (StringUtils.isNotBlank(applyinstId)) {
                aeaHiApplyinstCancels.addAll(restApplyinstCancelService.getApplyinstCancelListByApplyinstId(applyinstId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, aeaHiApplyinstCancels, "获取数据失败！");
        }
        return new ContentResultForm(true, aeaHiApplyinstCancels, "获取数据成功！");
    }

    @RequestMapping("/updateIteminstCancelInfo")
    public ResultForm updateIteminstCancelInfo(AeaHiApplyinstCancelVo cancelVo) {
        try {
            if (StringUtils.isBlank(restApplyinstCancelService.updateIteminstCancelInfo(cancelVo))) {
                return new ResultForm(true, "保存成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败！");
    }

    @RequestMapping("/createApplyinstCancelInfo")
    public ResultForm createApplyinstCancelInfo(ApplyinstCancelInfoVo applyinstCancelInfoVo) {
        try {
            if (StringUtils.isBlank(restApplyinstCancelService.createApplyinstCancelInfo(applyinstCancelInfoVo))) {
                return new ResultForm(true, "保存成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败！");
    }
}
