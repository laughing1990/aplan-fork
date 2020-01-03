package com.augurit.aplanmis.common.service.window;
import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 项目拆分申请表-Service服务调用接口类
*/
public interface AeaProjApplySplitService {
    public void saveAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;
    public void updateAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;
    public void deleteAeaProjApplySplitById(String id) throws Exception;
    public PageInfo<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit, Page page) throws Exception;
    public AeaProjApplySplit getAeaProjApplySplitById(String id) throws Exception;
    public List<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

}
