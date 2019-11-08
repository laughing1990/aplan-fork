package com.augurit.aplanmis.front.supermarket.service;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AeaImProjPurchaseService {

    /**
     * 根据服务事项获取服务
     *
     * @param itemVerId
     * @return
     */
    List<AeaImServiceVo> getItemServiceListByItemVerId(String itemVerId) throws Exception;


    /**
     * 文件上传
     *批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile
     * @param request
     * @return
     */
    String uploadFiles(HttpServletRequest request)throws Exception;
}
