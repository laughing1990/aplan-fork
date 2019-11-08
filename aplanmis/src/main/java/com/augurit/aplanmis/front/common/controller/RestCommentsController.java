package com.augurit.aplanmis.front.common.controller;

import com.augurit.agcloud.bpm.common.domain.ActUserOpinion;
import com.augurit.agcloud.bpm.common.service.ActUserOpinionService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.common.vo.ActUserOpinionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/comment")
@Api(tags = "审批意见")
public class RestCommentsController {
    @Autowired
    private ActUserOpinionService actUserOpinionService;

    @GetMapping("/getAllActiveUserOpinions")
    @ApiOperation("获取操作员审批意见列表")
    public ContentResultForm<List<ActUserOpinionVo>> getAllActiveUserOpinions() {
        ActUserOpinion userOpinion = new ActUserOpinion();
        userOpinion.setIsActive("1");
        userOpinion.setUserId(SecurityContext.getCurrentUser().getUserId());
        List<ActUserOpinion> actUserOpinions = actUserOpinionService.listActUserOpinion(userOpinion);
        List<ActUserOpinionVo> actUserOpinionVos = ActUserOpinionVo.toActUserOpinionVo(actUserOpinions);
        return new ContentResultForm<>(true, actUserOpinionVos);
    }

    @PostMapping("/saveUserOpinion")
    @ApiOperation("保存常用审批意见")
    @ApiImplicitParam(name = "message", value = "意见", required = true, type = "string")
    public ResultForm saveUserOpinion(String message) {
        String uid =SecurityContext.getCurrentUser().getUserId();
        ActUserOpinion sort = new ActUserOpinion();
        sort.setUserId(uid);
        if (!StringUtils.isEmpty(message)) {
            ActUserOpinion userOpinion = new ActUserOpinion();
            userOpinion.setOpinionId(UUID.randomUUID().toString());
            userOpinion.setOpinionContent(message);
            userOpinion.setUserId(SecurityContext.getCurrentUser().getUserId());
            userOpinion.setIsActive("1");
            List<ActUserOpinion> allUserActUserOpinionByUserId = actUserOpinionService.listActUserOpinion(sort);
            userOpinion.setSortNo(allUserActUserOpinionByUserId.size());
            userOpinion.setCreater(SecurityContext.getCurrentUserName());
            userOpinion.setCreateTime(new Date());
            actUserOpinionService.insertActUserOpinion(userOpinion);
        }
        return new ResultForm(true, "保存成功");
    }

    @DeleteMapping("/deleteUserOpinion/{opinionId}")
    @ApiOperation("删除常用审批意见")
    @ApiImplicitParam(name = "opinionId", value = "需要删除的意见ID", required = true, type = "string")
    public ResultForm deleteUserOpinion(@PathVariable String opinionId) {
        actUserOpinionService.deleteActUserOpinion(opinionId);
        return new ResultForm(true, "删除成功");
    }

    @DeleteMapping("/batchDeleteUserOpinion")
    @ApiOperation("批量删除常用审批意见")
    @ApiImplicitParam(name = "opinionIds", value = "需要删除的意见IDs", required = true, type = "list")
    public ResultForm batchDeleteUserOpinion(String[] opinionIds) {
        for (String id : opinionIds) {
            actUserOpinionService.deleteActUserOpinion(id);
        }
        return new ResultForm(true, "删除成功");
    }

    @GetMapping("/getOpinionById/{opinionId}")
    @ApiOperation("根据ID查询单个意见")
    @ApiImplicitParam(name = "opinionId", value = "意见ID", required = true, type = "string")
    public ContentResultForm getOpinionById(@PathVariable String opinionId) {
        ActUserOpinion actUserOpinionById = actUserOpinionService.getActUserOpinionById(opinionId);
        return new ContentResultForm<>(true, actUserOpinionById);
    }

    @PutMapping("/editUserOpinion")
    @ApiOperation("编辑常用审批意见")
    @ApiImplicitParams({@ApiImplicitParam(name = "opinionContent", value = "意见", required = true, type = "string"),
            @ApiImplicitParam(name = "opinionId", value = "意见ID", required = true, type = "string"),
            @ApiImplicitParam(name = "userId", value = "操作员ID", required = true, type = "string"),
            @ApiImplicitParam(name = "sortNo", value = "排序号", required = true, type = "int")})
    public ContentResultForm<String> editUserOpinion(String opinionId, String opinionContent, String userId, int sortNo) {

        String uid =SecurityContext.getCurrentUser().getUserId();
        ActUserOpinion sort = new ActUserOpinion();
        sort.setUserId(uid);

        if (StringUtils.isEmpty(opinionId)) {
            saveUserOpinion(opinionContent);
        } else {
            ActUserOpinion userOpinion = new ActUserOpinion();
            userOpinion.setOpinionId(opinionId);
            userOpinion.setOpinionContent(opinionContent);
            userOpinion.setUserId(userId);
            userOpinion.setIsActive("1");
            userOpinion.setSortNo(sortNo);
            userOpinion.setModifier(SecurityContext.getCurrentUserName());
            userOpinion.setModifyTime(new Date());
            //排序

            List<ActUserOpinion> allUserActUserOpinionByUserId = actUserOpinionService.listActUserOpinion(sort);
            ActUserOpinion actUserOpinionById = actUserOpinionService.getActUserOpinionById(opinionId);

            for (ActUserOpinion actUserOpinion   :allUserActUserOpinionByUserId) {
                if (actUserOpinion.getOpinionId()!=opinionId){

                   //小变大
                    if(actUserOpinionById.getSortNo()<sortNo ){
                        if (actUserOpinion.getSortNo()<=sortNo&&actUserOpinion.getSortNo()>actUserOpinionById.getSortNo()){
                            actUserOpinion.setSortNo(actUserOpinion.getSortNo()-1);
                            actUserOpinionService.updateActUserOpinion(actUserOpinion);
                        }
                    //大变小
                    }else {
                        if (actUserOpinion.getSortNo()>=sortNo&&actUserOpinion.getSortNo()<actUserOpinionById.getSortNo()){
                            actUserOpinion.setSortNo(actUserOpinion.getSortNo()+1);
                            actUserOpinionService.updateActUserOpinion(actUserOpinion);
                        }
                    }
                }
                }

             actUserOpinionService.updateActUserOpinion(userOpinion);
        }
        return new ContentResultForm<>(true, opinionId, "保存成功");
    }


}
