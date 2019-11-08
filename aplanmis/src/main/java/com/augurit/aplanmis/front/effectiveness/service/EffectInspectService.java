package com.augurit.aplanmis.front.effectiveness.service;

import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.EffectInspectForm;
import com.augurit.aplanmis.common.mapper.EffectivenessInspectionMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class EffectInspectService {
    @Autowired
    private EffectivenessInspectionMapper effectivenessInspectionMapper;
    @Autowired
    private WorkdayHolidayService workdayHolidayService;

    //预警办件
    public PageInfo<EffectInspectForm> yuJingBanJianList(Page page,String keyword){
        PageHelper.startPage(page);
        List<EffectInspectForm> list = effectivenessInspectionMapper.yuJingBanJianList(keyword);
        this.convertEffectInspectDateTime(list);
        return new PageInfo<EffectInspectForm>(list);
    }
    public Integer yuJingBanJianCount(){
        Integer count = effectivenessInspectionMapper.yuJingBanJianCount();

        if(count==null)
            count = 0;

        return count;
    }

    //逾期办件
    public PageInfo<EffectInspectForm> yuQiBanJianList(Page page,String keyword){
        PageHelper.startPage(page);
        List<EffectInspectForm> list = effectivenessInspectionMapper.yuQiBanJianList(keyword);
        this.convertEffectInspectDateTime(list);
        return new PageInfo<EffectInspectForm>(list);
    }
    public Integer yuQiBanJianCount(){
        Integer count = effectivenessInspectionMapper.yuQiBanJianCount();

        if(count==null)
            count = 0;

        return count;
    }

    //网上待预审
    public PageInfo<EffectInspectForm> wangShangDaiYuShenList(Page page,String keyword){
        PageHelper.startPage(page);
        List<EffectInspectForm> list = effectivenessInspectionMapper.wangShangDaiYuShenList(keyword);
        this.convertEffectInspectDateTime(list);
        return new PageInfo<EffectInspectForm>(list);
    }
    public Integer wangShangDaiYuShenCount(){
        return effectivenessInspectionMapper.wangShangDaiYuShenCount();
    }

    //材料待补全
    public PageInfo<EffectInspectForm> caiLiaoBuQuanBanJianList(Page page,String keyword){
        PageHelper.startPage(page);
        List<EffectInspectForm> list = effectivenessInspectionMapper.caiLiaoBuQuanBanJianList(keyword);
        this.convertEffectInspectDateTime(list);
        return new PageInfo<EffectInspectForm>(list);
    }
    public Integer caiLiaoBuQuanBanJianCount(){
        return effectivenessInspectionMapper.caiLiaoBuQuanBanJianCount();
    }

    //待过程补正
    public PageInfo<EffectInspectForm> daiGuoChengBuZhengBanJianList(Page page,String keyword){
        PageHelper.startPage(page);
        List<EffectInspectForm> list = effectivenessInspectionMapper.daiGuoChengBuZhengBanJianList(keyword);

        this.convertEffectInspectDateTime(list);

        return new PageInfo<EffectInspectForm>(list);
    }
    public Integer daiGuoChengBuZhengBanJianCount(){
        return effectivenessInspectionMapper.daiGuoChengBuZhengBanJianCount();
    }

    private String convertEffectInspectDateTime(List<EffectInspectForm> list){
        if(list!=null&&list.size()>0){
            if(list!=null&&list.size()>0){
                for(EffectInspectForm form:list){
                    if(form.getApplyinstTime()!=null){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateStr = dateFormat.format(form.getApplyinstTime());

                        form.setApplyinstTimeStr(dateStr);
                    }
                }
            }
        }
        return "";
    }
}
