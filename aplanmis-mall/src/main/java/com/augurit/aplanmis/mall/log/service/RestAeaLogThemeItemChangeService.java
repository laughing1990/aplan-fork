package com.augurit.aplanmis.mall.log.service;

import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaLogThemeItemChange;
import com.augurit.aplanmis.common.mapper.AeaLogThemeItemChangeMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.mall.log.vo.ApplyIteminstConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestAeaLogThemeItemChangeService {
    @Autowired
    private AeaLogThemeItemChangeMapper aeaLogThemeItemChangeMapper;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;

    /**
     *  1.根据applyinstId去aea_log_theme_item_change表找变更项目类型的数据，若有，则表示变更了项目类型，若没有，则表示没有变更项目类型。
     * 2.1若变更了项目类型，则只需根据applyinstId查出事项实例（关联log表带出部门意见）即可。
     * 2.2 若没有变更项目类型，则首先根据applyinstId查出所有事项实例(含删除的),然后去aea_log_theme_item_change表找出新增/删除的事项实例标记并附上部门意见，事项实例表有但日志表没有的事项则是没有变动的事项。
     * 3.根据业主再次筛选的事项，将业主没有选择的事项实例is_deleted字段置为1(或者直接删除该数据？)
     */

    public ApplyIteminstConfirmVo getApplyIteminstConfirmDetail(String applyinstId) throws Exception {
        ApplyIteminstConfirmVo applyIteminstConfirmVo=new ApplyIteminstConfirmVo();
        List<AeaHiParStateinst> stageList = aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null);
        if(stageList.size()>0) {
            AeaHiParStateinst aeaHiParStateinst= stageList.get(0);

        }
        return applyIteminstConfirmVo;
    }

    public List<AeaLogThemeItemChange> getAeaLogThemeItemChangeByApplyinstId(String applyinstId,String themeId) throws Exception {
        AeaLogThemeItemChange aeaLogThemeItemChange=new AeaLogThemeItemChange();
        aeaLogThemeItemChange.setApplyinstId(applyinstId);
        aeaLogThemeItemChange.setOldThemeId(themeId);
        return aeaLogThemeItemChangeMapper.listAeaLogThemeItemChange(aeaLogThemeItemChange);
    }
}
