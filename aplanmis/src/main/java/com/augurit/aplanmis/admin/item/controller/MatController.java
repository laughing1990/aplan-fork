package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.dto.InoutMatAddDto;
import com.augurit.aplanmis.common.dto.InoutMatEditDto;
import com.augurit.aplanmis.common.service.admin.item.impl.MatService;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mat/controller")
public class MatController {

    @Autowired
    private MatService matService;

    @PostMapping("/add/one")
    public ResultForm addItemMat(HttpServletRequest request, InoutMatAddDto inoutMatAddDto) throws Exception {

        Assert.notNull(inoutMatAddDto.getItemVerId(), "事项版本id不能为空");
        if(StringUtils.isNotBlank(inoutMatAddDto.getIsInput())){
            if(inoutMatAddDto.getIsInput().equals(Status.ON)) {
                Assert.notNull(inoutMatAddDto.getStateVerId(), "情形版本id不能为空");
            }else{
                inoutMatAddDto.setStateVerId(null);
            }
        }
        matService.addMat(request, inoutMatAddDto);
        return new ResultForm(true);
    }

    @PostMapping("/edit/one")
    public ResultForm editItemMat(HttpServletRequest request, InoutMatEditDto inoutMatEditDto) throws Exception {

        Assert.notNull(inoutMatEditDto.getItemVerId(), "事项版本id不能为空");
        if(StringUtils.isNotBlank(inoutMatEditDto.getIsInput())){
            if(inoutMatEditDto.getIsInput().equals("1")) {
                Assert.notNull(inoutMatEditDto.getStateVerId(), "情形版本id不能为空");
            }else{
                inoutMatEditDto.setStateVerId(null);
            }
        }
        Assert.notNull(inoutMatEditDto.getMatId(), "材料id不能为空");
        matService.editMat(request, inoutMatEditDto);
        return new ResultForm(true);
    }

    /**
     *  根据matinstId 显示 /bpm/getAttFiles.do 的材料
     *
     *  跳转材料列表页面
     * @return
     */
    @GetMapping("/matListView.do")
    public ModelAndView matListView(String matinstId,ModelAndView modelAndView){

        modelAndView.addObject("matinstId",matinstId);
        modelAndView.setViewName("ui-jsp/global_view/global_mat/global_mat_view");
        return modelAndView;
    }
}
