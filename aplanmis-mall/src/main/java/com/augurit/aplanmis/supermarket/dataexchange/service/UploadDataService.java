package com.augurit.aplanmis.supermarket.dataexchange.service;

import com.augurit.aplanmis.supermarket.dataexchange.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class UploadDataService {
    @Autowired
    private TestMapper testMapper;

    public void insertOne() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(new Date());
        testMapper.insertOne(UUID.randomUUID().toString(), "测试多数据-" + format1, new Date());
    }
}
