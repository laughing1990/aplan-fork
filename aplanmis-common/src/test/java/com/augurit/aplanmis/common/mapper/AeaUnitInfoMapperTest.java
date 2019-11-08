package com.augurit.aplanmis.common.mapper;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@DisplayName("建设单位及联系人测试")
class AeaUnitInfoMapperTest extends BaseTest {
    /*@Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;*/

  /*  @Test
    @DisplayName("测试多单位")
    public void insertAeaUnitLinkman() throws Exception{
        List<AeaUnitLinkman> aeaUnitLinkmen = null;//aeaUnitInfoMapper.listAeaUnitLinkman(new AeaUnitLinkman());
        Assertions.assertEquals(0,aeaUnitLinkmen.size());
        List<AeaUnitInfo> unitInfoList = aeaUnitInfoMapper.listAeaUnitInfo(new AeaUnitInfo());
        List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listAeaLinkmanInfo(new AeaLinkmanInfo());

        AeaUnitInfo aeaUnitInfo = unitInfoList.get(0);
        AeaUnitInfo aeaUnitInfo1 = unitInfoList.get(1);
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfos.get(0);
        AeaLinkmanInfo aeaLinkmanInfo1 = aeaLinkmanInfos.get(1);
        //测试一个联系人属于一个单位
        AeaUnitLinkman aeaUnitLinkman=new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
        aeaUnitLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
        aeaUnitLinkman.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
        aeaUnitLinkman.setCreater("xiaohutu");
        aeaUnitLinkman.setCreateTime(new Date());
        //aeaUnitInfoMapper.insertAeaUnitLinkman(aeaUnitLinkman);
        //aeaUnitLinkmen=aeaUnitInfoMapper.listAeaUnitLinkman(new AeaUnitLinkman());
        Assertions.assertEquals(1,aeaUnitLinkmen.size());

        //List<AeaLinkmanInfo> listLinkmanUnitById = aeaLinkmanInfoMapper.getListLinkmanUnitById(aeaLinkmanInfo.getLinkmanInfoId());
        *//*Assertions.assertEquals(1,listLinkmanUnitById.size());*//*

        //测试一个联系人属于多单位
        aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
        aeaUnitLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
        aeaUnitLinkman.setUnitInfoId(aeaUnitInfo1.getUnitInfoId());
        //aeaUnitInfoMapper.insertAeaUnitLinkman(aeaUnitLinkman);
        //aeaUnitLinkmen=aeaUnitInfoMapper.listAeaUnitLinkman(new AeaUnitLinkman());
        Assertions.assertEquals(2,aeaUnitLinkmen.size());

        *//*listLinkmanUnitById = aeaLinkmanInfoMapper.getListLinkmanUnitById(aeaLinkmanInfo.getLinkmanInfoId());
        Assertions.assertEquals(2,listLinkmanUnitById.size());*//*

    }
    
    @Test
    public void uund(){
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }*/

}