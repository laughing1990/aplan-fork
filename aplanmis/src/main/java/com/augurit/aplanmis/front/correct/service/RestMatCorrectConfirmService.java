package com.augurit.aplanmis.front.correct.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectMapper;
import com.augurit.aplanmis.common.vo.MatCorrectConfirmVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RestMatCorrectConfirmService {

    @Autowired
    private AeaHiItemCorrectMapper itemCorrectMapper;

    public PageInfo<MatCorrectConfirmVo> searchStayMattCorrect(Page page, String keyword) throws Exception{
        PageHelper.startPage(page);
        List<MatCorrectConfirmVo> list = itemCorrectMapper.searchStayMatCorrectListByKeyword(keyword,SecurityContext.getCurrentOrgId(),SecurityContext.getCurrentUserId());
        log.info("成功执行分页查询！！");
        return new PageInfo<MatCorrectConfirmVo>(list);
    }
}
