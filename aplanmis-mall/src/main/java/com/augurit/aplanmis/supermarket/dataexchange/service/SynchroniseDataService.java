package com.augurit.aplanmis.supermarket.dataexchange.service;

import com.augurit.aplanmis.supermarket.dataexchange.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SynchroniseDataService {
    @Autowired
    private TestMapper testMapper;
}
