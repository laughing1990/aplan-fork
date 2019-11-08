package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageQuestions;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageQuestionsAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
* 阶段办事指南常见问题回答-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/par/stage/questions")
public class AeaParStageQuestionsController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageQuestionsController.class);

    @Autowired
    private AeaParStageQuestionsAdminService stageQuestionsService;

    @RequestMapping("/indexAeaParStageQuestions.do")
    public ModelAndView indexAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions){

        return new ModelAndView("aea/par/stage/stage_questions_index");
    }

    @RequestMapping("/listQuestAnswers.do")
    public EasyuiPageInfo<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions, Page page) {
        
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStageQuestions);
        PageInfo pageInfo = stageQuestionsService.listAeaParStageQuestions(aeaParStageQuestions, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "查询见问题列表,带分页", notes = "查询见问题列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "questions", value = "必填" , dataType = "AeaParStageQuestions" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions questions, @ModelAttribute PageParam page){

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", questions);
        PageInfo pageInfo = stageQuestionsService.listAeaParStageQuestions(questions, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "通过id获取常见问题答案", notes = "通过id获取常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getQuestAnswerById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaParStageQuestions getAeaParStageQuestions(String id) {

        if (StringUtils.isNotBlank(id)){
            
            logger.debug("根据ID获取AeaParStageQuestions对象，ID为：{}", id);
            return stageQuestionsService.getAeaParStageQuestionsById(id);
        }else {
            logger.debug("构建新的AeaParStageQuestions对象");
            return new AeaParStageQuestions();
        }
    }

    @RequestMapping("/updateAeaParStageQuestions.do")
    public ResultForm updateAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) {
        
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStageQuestions);
        stageQuestionsService.updateAeaParStageQuestions(aeaParStageQuestions);
        return new ResultForm(true);
    }


    /**
     *
    * 保存或编辑阶段办事指南常见问题回答
     *
    * @param questions 阶段办事指南常见问题回答
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @ApiOperation(value = "保存或编辑阶段办事指南常见问题回答", notes = "保存或编辑阶段办事指南常见问题回答")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "questions", value = "必填" , dataType = "AeaParStageQuestions" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveOrUpdateQuestAnswer.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveAeaParStageQuestions(AeaParStageQuestions questions) {

        if(StringUtils.isNotBlank(questions.getQuestionId())){
            stageQuestionsService.updateAeaParStageQuestions(questions);
        }else{
            questions.setQuestionId(UUID.randomUUID().toString());
            stageQuestionsService.saveAeaParStageQuestions(questions);
        }
        return  new ContentResultForm<AeaParStageQuestions>(true,questions);
    }

    @ApiOperation(value = "通过id删除常见问题答案", notes = "通过id删除常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/delQuestAnswerById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm delQuestAnswerById(String id) throws Exception{

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除阶段办事指南常见问题回答Form对象，对象id为：{}", id);
        stageQuestionsService.deleteAeaParStageQuestionsById(id);
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过ids批量删除常见问题答案", notes = "通过ids批量删除常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填" , required = true, allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/batchDelQuestAnswerByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelQuestAnswerByIds(String[] ids) throws Exception{

        if(ids!=null && ids.length>0) {
            logger.debug("批量删除阶段办事指南常见问题回答Form对象，对象ids为：{}", ids);
            stageQuestionsService.batchDelQuestAnswerByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @ApiOperation(value = "通过阶段stageId获取常见问题最大排序号", notes = "通过阶段stageId获取常见问题最大排序号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stageId", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getMaxSortNo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getMaxSortNo(String stageId) throws Exception{

        if(StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        return stageQuestionsService.getMaxSortNo(stageId, SecurityContext.getCurrentOrgId());
    }
}
