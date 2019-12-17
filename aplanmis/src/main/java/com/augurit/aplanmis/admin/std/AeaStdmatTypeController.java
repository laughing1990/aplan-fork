package com.augurit.aplanmis.admin.std;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaStdmatType;
import com.augurit.aplanmis.common.service.admin.std.AeaStdmatTypeAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* 材料类型表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/stdmat/type")
public class AeaStdmatTypeController {

private static Logger logger = LoggerFactory.getLogger(AeaStdmatTypeController.class);

    @Autowired
    private AeaStdmatTypeAdminService aeaStdmatTypeService;

    @RequestMapping("/indexAeaStdmatType.do")
    public ModelAndView indexAeaStdmatType(AeaStdmatType aeaStdmatType){

        return new ModelAndView("aea/stdmat/type_index");
    }

    @RequestMapping("/listAeaStdmatType.do")
    public PageInfo<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaStdmatType);
        return aeaStdmatTypeService.listAeaStdmatType(aeaStdmatType,page);
    }

    @RequestMapping("/getAeaStdmatType.do")
    public AeaStdmatType getAeaStdmatType(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaStdmatType对象，ID为：{}", id);
            return aeaStdmatTypeService.getAeaStdmatTypeById(id);
        }else {
            logger.debug("构建新的AeaStdmatType对象");
            return new AeaStdmatType();
        }
    }

    @RequestMapping("/updateAeaStdmatType.do")
    public ResultForm updateAeaStdmatType(AeaStdmatType aeaStdmatType) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaStdmatType);
        aeaStdmatTypeService.updateAeaStdmatType(aeaStdmatType);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑材料类型表
     *
    * @param aeaStdmatType 材料类型表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaStdmatType.do")
    public ResultForm saveAeaStdmatType(AeaStdmatType aeaStdmatType) throws Exception {

        if (StringUtils.isNotBlank(aeaStdmatType.getStdmatTypeId())){
            aeaStdmatTypeService.updateAeaStdmatType(aeaStdmatType);
        }else{
            aeaStdmatType.setStdmatTypeId(UUID.randomUUID().toString());
            aeaStdmatTypeService.saveAeaStdmatType(aeaStdmatType);
        }
        return  new ContentResultForm<AeaStdmatType>(true, aeaStdmatType);
    }

    @RequestMapping("/deleteAeaStdmatTypeById.do")
    public ResultForm deleteAeaStdmatTypeById(String id) throws Exception{

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaStdmatTypeService.deleteAeaStdmatTypeById(id);
        return new ResultForm(true);
    }

    @RequestMapping("/deleteSelfAndAllChildById.do")
    public ResultForm deleteSelfAndAllChildById(String id) throws Exception{

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaStdmatTypeService.deleteSelfAndAllChildById(id, rootOrgId);
        return new ResultForm(true);
    }

    @RequestMapping("/gtrStdTypeMatZtree.do")
    public List<ZtreeNode> gtrStdTypeMatZtree() throws Exception{

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaStdmatTypeService.gtrStdTypeMatZtree(rootOrgId);
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo() throws Exception{

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaStdmatTypeService.getMaxSortNo(rootOrgId);
    }

    @ApiOperation(value = "查找是否已存在该分类编号", notes = "查找是否已存在该分类编号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stdmatTypeId", value = "类型id", dataType = "String"),
        @ApiImplicitParam(name = "typeCode", value = "类型编码", dataType = "String", required = true)
    })
    @RequestMapping(value ="/checkUniqueTypeCode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkUniqueTypeCode(String stdmatTypeId, String typeCode) {

        if (StringUtils.isBlank(typeCode)) {
            return "false";
        }
        return aeaStdmatTypeService.checkUniqueTypeCode(stdmatTypeId, typeCode) + "";
    }
}
