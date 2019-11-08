package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.AeaItemSeq;
import com.augurit.aplanmis.common.domain.AeaItemStateVer;
import com.augurit.aplanmis.common.mapper.AeaItemSeqMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateVerMapper;
import com.augurit.aplanmis.common.qo.state.AeaItemStateVersionQo;
import com.augurit.aplanmis.common.vo.AeaItemStateVersionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/state/ver")
public class StateVerController {

    @Autowired
    private AeaItemStateVerMapper aeaItemStateVerMapper;

    @Autowired
    private AeaItemSeqMapper aeaItemSeqMapper;

    /**
     * 根据事项版本ID查询所有的历史版本，不包含已删除的
     *
     * @param itemVerId
     * @return
     */
    @RequestMapping("/histories.do")
    @Transactional(readOnly = true)
    public List<AeaItemStateVersionVo> viewHistoricVersions(String itemVerId) {

        Assert.notNull(itemVerId, "itemVerId is null");
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemStateVersionQo qo = AeaItemStateVersionQo.createQuery().itemVerIdEq(itemVerId).rootOrgIdEq(rootOrgId);
        return aeaItemStateVerMapper.listAeaItemStateVersionByQueryCriteria(qo)
                .stream()
                .map(AeaItemStateVersionVo::from)
                .collect(Collectors.toList());
    }

    @RequestMapping("/testRun.do")
    @Transactional
    public ResultForm testRun(String stateVerId, Double verNum) {

        if(StringUtils.isBlank(stateVerId)){
            throw new IllegalArgumentException("情形版本id为空！");
        }
        Assert.notNull(verNum, "情形序号verNum为空!");
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 情形版本
        AeaItemStateVer stateVer = aeaItemStateVerMapper.getAeaItemStateVerById(stateVerId);
        Assert.notNull(stateVer, "无法找到对应的情形版本, stateVerId: " + stateVerId);
        stateVer.setVerStatus(PublishStatus.TEST_RUN.getValue());
        stateVer.setVerNum(verNum);
        stateVer.setModifier(userId);
        stateVer.setModifyTime(new Date());
        aeaItemStateVerMapper.updateAeaItemStateVer(stateVer);

        // 情形序号
        AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(null, stateVer.getItemVerId(), rootOrgId);
        Assert.notNull(seq, "无法找到对应的情形版本序号, stateVerId: " + stateVerId);
        seq.setStateVerMax(verNum);
        seq.setModifier(userId);
        seq.setModifyTime(new Date());
        aeaItemSeqMapper.updateAeaItemSeq(seq);

        // 当点击试运行时，其他试运行或已发布版本就要变成已过时
        aeaItemStateVerMapper.deprecateAllTestRunAndPublishedVersion(rootOrgId, stateVer.getItemVerId(), stateVerId);
        return new ResultForm(true, "发布成功");
    }

    @RequestMapping("/publish.do")
    @Transactional
    public ResultForm publish(String stateVerId, Double verNum) {

        if(StringUtils.isEmpty(stateVerId)){
            throw new IllegalArgumentException("情形版本id为空！");
        }
        Assert.notNull(verNum, "情形序号verNum为空!");
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 情形版本
        AeaItemStateVer stateVer = aeaItemStateVerMapper.getAeaItemStateVerById(stateVerId);
        Assert.notNull(stateVer, "无法找到对应的情形版本, stateVerId: " + stateVerId);
        stateVer.setVerStatus(PublishStatus.PUBLISHED.getValue());
        stateVer.setVerNum(verNum);
        stateVer.setModifier(userId);
        stateVer.setModifyTime(new Date());
        aeaItemStateVerMapper.updateAeaItemStateVer(stateVer);

        // 情形序号
        AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(null, stateVer.getItemVerId(), rootOrgId);
        Assert.notNull(seq, "无法找到对应的情形版本序号, stateVerId: " + stateVerId);
        seq.setStateVerMax(verNum);
        seq.setModifier(userId);
        seq.setModifyTime(new Date());
        aeaItemSeqMapper.updateAeaItemSeq(seq);

        // 当点击试运行时，其他试运行或已发布版本就要变成已过时
        aeaItemStateVerMapper.deprecateAllTestRunAndPublishedVersion(rootOrgId, stateVer.getItemVerId(), stateVerId);
        return new ResultForm(true, "发布成功");
    }
}
