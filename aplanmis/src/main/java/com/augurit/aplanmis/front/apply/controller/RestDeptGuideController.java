package com.augurit.aplanmis.front.apply.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.front.apply.vo.guide.GuideDetailVo;
import com.augurit.aplanmis.front.apply.vo.guide.GuideQueryVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept/guide")
@Slf4j
public class RestDeptGuideController {

    @Autowired
    private AeaHiGuideService aeaHiGuideService;

    @GetMapping("/list")
    public ContentResultForm<PageInfo<AeaHiGuide>> list(GuideQueryVo guideQueryVo, Page page) {
        try {
            AeaHiGuide param = guideQueryVo.toAeaHiGuide();
            PageInfo<AeaHiGuide> aeaHiGuidePageInfo = aeaHiGuideService.list(param, page);
            return new ContentResultForm<>(true, aeaHiGuidePageInfo, "success.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ContentResultForm<GuideDetailVo> detail() {
        // todo 部门辅导详情
        return null;
    }
}
