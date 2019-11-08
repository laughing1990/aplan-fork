package com.augurit.aplanmis.front.form.controller;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.service.form.AeaExProjContractService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* 合同信息-Controller 页面控制转发类

*/
@RestController
@RequestMapping("/rest/form/contra")
public class AeaExProjContractController {

private static Logger logger = LoggerFactory.getLogger(AeaExProjContractController.class);

    @Autowired
    private AeaExProjContractService aeaExProjContractService;
    @Autowired
    private BscDicCodeService bscDicCodeService;



    @RequestMapping("/index.html")
    public ModelAndView indexAeaExProjContract(String projInfoId){
        ModelAndView view = new ModelAndView("form/contractInfo");
        view.addObject("projInfoId", projInfoId);
        return view;
    }

  /*  @RequestMapping("/listAeaExProjContract.do")
    public PageInfo<AeaExProjContract> listAeaExProjContract(  AeaExProjContract aeaExProjContract, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaExProjContract);
        return aeaExProjContractService.listAeaExProjContract(aeaExProjContract,page);
    }*/

    @RequestMapping("/contractType")
    public List<BscDicCodeItem> getLandAreaType(){
      String landAreaType = "contract_type";
      BscDicCodeType typeByTypeCode = bscDicCodeService.getTypeByTypeCode(landAreaType, SecurityContext.getCurrentOrgId());
      if (typeByTypeCode==null){
          BscDicCodeType bscDicCodeType = new BscDicCodeType();
          bscDicCodeType.setTypeId(UUID.randomUUID().toString());
          bscDicCodeType.setTypeCode(landAreaType);
          bscDicCodeType.setTypeName("合同类型");
          bscDicCodeType.setTypeIsTree("0");
          bscDicCodeType.setTypeIsActive("1");
          bscDicCodeType.setOrgId(SecurityContext.getCurrentOrgId());
          bscDicCodeService.saveType(bscDicCodeType);
          List<BscDicCodeItem> areaType = bscDicCodeService.getItemsByTypeCode(bscDicCodeType.getTypeCode());
          return areaType;
      }
      else {
          List<BscDicCodeItem> areaType = bscDicCodeService.getItemsByTypeCode(typeByTypeCode.getTypeCode());
          return areaType;
      }
  }

    @RequestMapping("/getAeaExProjContract.do")
    public AeaExProjContract getAeaExProjContract(String projInfoId) throws Exception {
        if (projInfoId != null){
            logger.debug("根据ID获取AeaExProjContract对象，ID为：{}", projInfoId);
            AeaExProjContract contractQuery = new AeaExProjContract();
            contractQuery.setProjInfoId(projInfoId);
            List<AeaExProjContract> aeaExProjContracts = aeaExProjContractService.listAeaExProjContract(contractQuery);
            AeaExProjContract aeaExProjContract = aeaExProjContracts.get(0);
            return aeaExProjContract;
        }
        else {
            logger.debug("构建新的AeaExProjContract对象");
            return new AeaExProjContract();
        }
    }

    @RequestMapping("/updateAeaExProjContract.do")
        public ResultForm updateAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaExProjContract);
        aeaExProjContractService.updateAeaExProjContract(aeaExProjContract);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑合同信息
    * @param aeaExProjContract 合同信息
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaExProjContract.do")
    public ResultForm saveAeaExProjContract(AeaExProjContract aeaExProjContract, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存合同信息Form对象出错");
            throw new InvalidParameterException(aeaExProjContract);
        }

        if(aeaExProjContract.getContractId()!=null&&!"".equals(aeaExProjContract.getContractId())){
            aeaExProjContractService.updateAeaExProjContract(aeaExProjContract);
        }else{
        if(aeaExProjContract.getContractId()==null||"".equals(aeaExProjContract.getContractId()))
            aeaExProjContract.setContractId(UUID.randomUUID().toString());
            aeaExProjContractService.saveAeaExProjContract(aeaExProjContract);
        }

        return  new ContentResultForm<AeaExProjContract>(true,aeaExProjContract);
    }

    @RequestMapping("/deleteAeaExProjContractById.do")
    public ResultForm deleteAeaExProjContractById(String id) throws Exception{
        logger.debug("删除合同信息Form对象，对象id为：{}", id);
        if(id!=null)
            aeaExProjContractService.deleteAeaExProjContractById(id);
        return new ResultForm(true);
    }

}
