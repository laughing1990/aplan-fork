package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.mapper.aplanmis.AplanmisDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/11/8
 */
@Service
public class AplanmisDataService {

    @Autowired
    AplanmisDataMapper aplanmisDataMapper;

    public Long getAplanmisProjCount(){
        return aplanmisDataMapper.getAplanmisProjCount();
    }

    public List<String> findAplanmisProjLocalCodeList(){
        return aplanmisDataMapper.findAplanmisProjLocalCodeList();
    }

    public Long getNonAcceptAplanmisProjCount() {
        return aplanmisDataMapper.getNonAcceptAplanmisProjCount();
    }

    public List<String> findNonAcceptAplanmisProjLocalCodeList() {
        return aplanmisDataMapper.findNonAcceptAplanmisProjLocalCodeList();
    }

    public Long getAplanmisProjCountHasItemStateLog() {
        return aplanmisDataMapper.getAplanmisProjCountHasItemStateLog();
    }

    public List<String> findAplanmisProjLocalCodeListHasItemStateLog() {
        return aplanmisDataMapper.findAplanmisProjLocalCodeListHasItemStateLog();
    }

    public Long getOnlyA2ProjCount() {
        return aplanmisDataMapper.getOnlyA2ProjCount();
    }

    public List<String> getOnlyA2ProjLocalCodeList() {
        return aplanmisDataMapper.getOnlyA2ProjLocalCodeList();
    }

}
