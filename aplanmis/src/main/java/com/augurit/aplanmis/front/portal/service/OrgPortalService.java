package com.augurit.aplanmis.front.portal.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.mapper.OrgPortalMapper;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.portal.vo.ItemCountVo;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OrgPortalService {
    @Autowired
    private OrgPortalMapper orgPortalMapper;
    @Autowired
    private ConditionalQueryService conditionalQueryService;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    public ItemCountVo countApply() throws Exception {
        Page page = new Page();
        page.setPageNum(1);
        page.setPageSize(10);
        ConditionalQueryRequest request = new ConditionalQueryRequest();
        request.setHandler(true);
        request.setEntrust(false);
        ConditionalQueryRequest sybjrequest = new ConditionalQueryRequest();
        sybjrequest.setHandler(false);
        sybjrequest.setEntrust(false);
        //所有办件
        long suoYouBanJian = conditionalQueryService.listPartsByPage(sybjrequest, page).getTotal(); //orgPortalMapper.querySuoYouBanJian(SecurityContext.getCurrentUserName(),null).size();
        //待补证
        long daiBuZheng = 0;//conditionalQueryService.listNeedCorrectTasksByPage(request,page).getTotal();//orgPortalMapper.countDaiBuZheng(SecurityContext.getCurrentUserName());
        //补正待确认
        long buZhengDaiQueRen = conditionalQueryService.listUnConfirmItemCorrect(request, page).getTotal();//orgPortalMapper.listBuZhengDaiQueRen(SecurityContext.getCurrentUserName(),null).size();
        //不通过
        long buTongGuo = conditionalQueryService.listItemDisgree(request, page).getTotal();//orgPortalMapper.countBuTongGuo(SecurityContext.getCurrentUserName());
        //不予受理
        long buYuShouLi = conditionalQueryService.listItemOutScope(request, page).getTotal();//orgPortalMapper.countBuYuShouLi(SecurityContext.getCurrentUserName());
        //时限预警数
        long shiXianYuJing = orgPortalMapper.countShiXianJian(SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserName(), "2");
        //时限逾期数
        long shiXianYuQi = orgPortalMapper.countShiXianJian(SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserName(), "3");
        //代办任务数
        long daiBanRenWu = conditionalQueryService.listWaitDoTasksByPage(request, page).getTotal();// orgPortalMapper.queryAllDaiBan(SecurityContext.getCurrentUserName(),null).size();
        //已办任务数
        long yiBanRenWu = conditionalQueryService.listDoneTasksByPage(request, page).getTotal();//orgPortalMapper.countYiBanRenWu(SecurityContext.getCurrentUserName());

        ItemCountVo vo = new ItemCountVo();
        vo.setSuoYouBanJian(suoYouBanJian);
        vo.setDaiBuZheng(daiBuZheng);
        vo.setBuZhengDaiQueRen(buZhengDaiQueRen);
        vo.setBuTongGuo(buTongGuo);
        vo.setBuYuShouLi(buYuShouLi);
        vo.setShiXianYuJing(shiXianYuJing);
        vo.setShiXianYuQi(shiXianYuQi);
        vo.setDaiBan(daiBanRenWu);
        vo.setYiBan(yiBanRenWu);

        return vo;

    }

    public PageInfo listDaiBan(int pageNum, int pageSize, String keyword , String sort) throws Exception {
        pageNum = pageNum < 0 ? 1 : pageNum;
        pageSize = pageSize < 0 ? 10 : pageSize;
        Page page = new Page(pageNum, pageSize);
        if(StringUtils.isBlank(sort)){
            sort = "desc";
        }
        page.setOrderBy(" dueNum  " + sort);
        ConditionalQueryRequest request = new ConditionalQueryRequest();
        request.setHandler(true);
        request.setEntrust(false);
        request.setKeyword(keyword);
        PageInfo pageInfo = conditionalQueryService.listWaitDoTasksByPage(request, page);
        return pageInfo;
    }

    public List<BscDicCodeItem> getDicViewCode(String typeCode) {
        List<BscDicCodeItem> items = bscDicCodeMapper.getActiveItemsByTypeCode(typeCode, SecurityContext.getCurrentOrgId());
        return items;
    }
}
