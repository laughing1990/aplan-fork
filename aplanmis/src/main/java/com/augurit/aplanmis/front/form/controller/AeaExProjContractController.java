package com.augurit.aplanmis.front.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.form.AeaExProjContractService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AeaProjInfoService aeaProjInfoService;



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



    @RequestMapping("/getAeaExProjContract.do")
    public ResultForm getAeaExProjContract(String projInfoId) throws Exception {

        if (projInfoId==null||"".equals(projInfoId)){
            return new ResultForm(false, "获取项目信息失败，项目id "+projInfoId);
        }

        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfoByProjInfoId==null)
        {
            return new ResultForm(false, "获取项目信息失败，项目id "+projInfoId);
        }
            logger.debug("根据ID获取AeaExProjContract对象，ID为：{}", projInfoId);
            AeaExProjContract contractQuery = new AeaExProjContract();
            contractQuery.setProjInfoId(projInfoId);
            List<AeaExProjContract> aeaExProjContracts = aeaExProjContractService.listAeaExProjContract(contractQuery);
            if (aeaExProjContracts.size()>0){
                AeaExProjContract aeaExProjContract = aeaExProjContracts.get(0);

                return new ContentResultForm<AeaExProjContract>(true, aeaExProjContract);

            }else {
                return new ContentResultForm<AeaExProjContract>(true, new AeaExProjContract());
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

    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaExProjContract.do")
    public ResultForm saveAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception {
        if (aeaExProjContract.getProjInfoId()==null||"".equals(aeaExProjContract.getProjInfoId())){
            return new ResultForm(false, "获取项目信息失败，项目id "+aeaExProjContract.getProjInfoId());
        }

        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaExProjContract.getProjInfoId());
        if (aeaProjInfoByProjInfoId==null)
        {
            return new ResultForm(false, "获取项目信息失败，项目id "+aeaExProjContract.getProjInfoId());
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
