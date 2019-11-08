package com.augurit.aplanmis.admin.market.service.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceQualService;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaImServiceQual;
import com.augurit.aplanmis.common.utils.ImageFileReader;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
* -服务资质关联控制类
*/
@RestController
@RequestMapping("/supermarket/serviceQual")
public class AeaImServiceQualController {

private static Logger logger = LoggerFactory.getLogger(AeaImServiceQualController.class);

    @Autowired
    private AeaImServiceQualService aeaImServiceQualService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaImServiceType(AeaImService aeaService, String infoType){
        return new ModelAndView("ui-jsp/supermarket_manage/service/service_qual");
    }

    @RequestMapping("/addServiceQualRelated.do")
    public ResultForm addServiceQualRelated(String serviceId,String qualIds){
        try {
            if(serviceId!=null&&!"".equals(serviceId)&&qualIds!=null&&!"".equals(qualIds)){
                String[] str = qualIds.split(",");
                AeaImServiceQual aeaImServiceQual = new AeaImServiceQual();
                for(int i=0;i<str.length;i++){

                    aeaImServiceQual.setServiceQualId(UUID.randomUUID().toString());
                    aeaImServiceQual.setServiceId(serviceId);
                    aeaImServiceQual.setQualId(str[i]);
                    aeaImServiceQual.setCreater(SecurityContext.getCurrentUserName());
                    aeaImServiceQual.setIsDelete("0");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    aeaImServiceQual.setCreateTime(df.parse(df.format(new Date())));
                    aeaImServiceQualService.saveAeaImServiceQual(aeaImServiceQual);
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
        return new ResultForm(true);
    }

    //删除服务关联的某个资质
    @RequestMapping("/deleteServiceQualByServiceId.do")
    public ResultForm deleteServiceQualByServiceId(String serviceId,String qualIds) throws Exception{
        try {
            if(serviceId!=null&&qualIds!=null){
                String[] str = qualIds.split(",");
                for(int i=0;i<str.length;i++){
                    aeaImServiceQualService.deleteServiceQualByServiceId(serviceId,str[i]);
                }

            }else{
                return new ResultForm(false);
            }

        }catch(Exception e){
            return new ResultForm(false);
        }

        return new ResultForm(true);
    }

    @RequestMapping("/checkServiceQualRelated.do")
    public List<String> checkServiceQualRelated(String serviceId) throws Exception{
        List<AeaImServiceQual> list = new ArrayList<AeaImServiceQual>();
        if(serviceId!=null){

            list = aeaImServiceQualService.getAeaImServiceQualListByServiceId(serviceId);
        }
        List<String> qualIdList = new ArrayList<String>();
        for(int i=0;i<list.size();i++){
            qualIdList.add(list.get(i).getQualId());
        }
        return qualIdList;
    }

    @RequestMapping("/indexAeaImServiceQual.do")
    public ModelAndView indexAeaImServiceQual(AeaImServiceQual aeaImServiceQual, String infoType){
        return new ModelAndView("aea/im/service/service_qual_index");
    }

    @RequestMapping("/listAeaImServiceQual.do")
    public PageInfo<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImServiceQual);
        return aeaImServiceQualService.listAeaImServiceQual(aeaImServiceQual,page);
    }

    @RequestMapping("/getAeaImServiceQual.do")
    public AeaImServiceQual getAeaImServiceQual(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImServiceQual对象，ID为：{}", id);
            return aeaImServiceQualService.getAeaImServiceQualById(id);
        }
        else {
            logger.debug("构建新的AeaImServiceQual对象");
            return new AeaImServiceQual();
        }
    }

    @RequestMapping("/updateAeaImServiceQual.do")
        public ResultForm updateAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImServiceQual);
        aeaImServiceQualService.updateAeaImServiceQual(aeaImServiceQual);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImServiceQual 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImServiceQual.do")
    public ResultForm saveAeaImServiceQual(AeaImServiceQual aeaImServiceQual, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImServiceQual);
        }

        if(aeaImServiceQual.getServiceQualId()!=null&&!"".equals(aeaImServiceQual.getServiceQualId())){
            aeaImServiceQualService.updateAeaImServiceQual(aeaImServiceQual);
        }else{
        if(aeaImServiceQual.getServiceQualId()==null||"".equals(aeaImServiceQual.getServiceQualId()))
            aeaImServiceQual.setServiceQualId(UUID.randomUUID().toString());
            aeaImServiceQualService.saveAeaImServiceQual(aeaImServiceQual);
        }

        return  new ContentResultForm<AeaImServiceQual>(true,aeaImServiceQual);
    }

    @RequestMapping("/deleteAeaImServiceQualById.do")
    public ResultForm deleteAeaImServiceQualById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaImServiceQualService.deleteAeaImServiceQualById(id);
        return new ResultForm(true);
    }

    /**
     * 根据条件不分页查询数据
     * @param aeaImServiceQual
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAeaImServiceQualNoPage.do")
    public List<AeaImServiceQual> listAeaImServiceQualNoPage(AeaImServiceQual aeaImServiceQual) throws Exception {
        logger.debug("不分页查询，过滤条件为{}", JsonUtils.toJson(aeaImServiceQual));
        List<AeaImServiceQual> list = aeaImServiceQualService.listAeaImServiceQual(aeaImServiceQual);
        return list;
    }

    @RequestMapping("/getServiceMattersImg.do")
    public List<Map<String,String>> getServiceMattersImg(String type) throws Exception {
        String filePath = "zjcs/serviceMatters/icon";
        if ("purchaseIcon".equals(type)) {
            filePath = "zjcs/serviceMatters/pic";
        }

        String realPath = Thread.currentThread().getContextClassLoader().getResource("static/").getPath();
        ImageFileReader imageRead = new ImageFileReader();
        List<Map<String,String>> fileNameList = imageRead.getImageFileList(realPath, filePath);
        return fileNameList;
    }
}
