package com.augurit.aplanmis.common.mapper;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class AeaHiItemMatinstTest extends BaseTest {

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

//    @Test
//    void getMatinstListByProjInfoIdAndMatIds() throws Exception {
//        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.getMatinstListByProjInfoIdAndMatIds("87e25032-5f52-409d-b3dd-8a6ce49f7994", new String[]{"5499c7f0-3eba-49a1-9ec1-36c3f1bd94ab"});
//        for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
//            System.out.println(aeaHiItemMatinst);
//        }
//
//    }

//    @Test
//    void getMatinstListByIteminstIds() throws Exception {
//        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.getMatinstListByIteminstIds(new String[]{"f28e7fb4-7caa-46d6-9b00-e817ee99cf80"}, "0");
//
//        for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
//            System.out.println(aeaHiItemMatinst);
//        }
//    }

//    @Test
//    void deleteAeaHiItemMatinsts() throws Exception {
//        aeaHiItemMatinstMapper.deleteAeaHiItemMatinsts(new String[]{"909e0ee9-f938-42bd-8274-d7ac73cc3043"});
//    }


    @Test
    @DisplayName("测试mapper返回的列表是null还是空的list")
    public void arrayListTest() throws Exception {
        List<AeaHiItemMatinst> matinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(new String[]{"000"});

        Assertions.assertEquals(0, matinsts.size());
    }
}