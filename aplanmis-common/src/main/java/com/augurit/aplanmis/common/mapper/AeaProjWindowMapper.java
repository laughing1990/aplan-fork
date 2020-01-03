package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaProjWindow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* 项目与代办中心（本质也是窗口）代办关联表-Mapper数据与持久化接口类
*/
@Mapper
public interface AeaProjWindowMapper {

    public void insertAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;
    public void updateAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;
    public void deleteAeaProjWindow(@Param("id") String id) throws Exception;
    public List <AeaProjWindow> listAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;
    public AeaProjWindow getAeaProjWindowById(@Param("id") String id) throws Exception;
}
