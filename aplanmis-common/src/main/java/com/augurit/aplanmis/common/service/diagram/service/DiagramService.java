package com.augurit.aplanmis.common.service.diagram.service;

import com.augurit.aplanmis.common.service.diagram.dto.DiagramStatusDto;
import com.augurit.aplanmis.common.service.diagram.dto.MatAttachDto;
import com.augurit.aplanmis.common.service.diagram.qo.MatAttachQo;

public interface DiagramService {

    /**
     * 获取项目全景图节点办理信息
     *
     * @param projInfoId 项目id
     * @param themeId    主题id
     */
    DiagramStatusDto getGlobalDiagramStatus(String projInfoId, String themeId) throws Exception;

    /**
     * 获取事项的附件
     *
     * @param matAttachQo
     * @return
     */
    MatAttachDto getAllAttachmentsByIteminstId(MatAttachQo matAttachQo);
}
