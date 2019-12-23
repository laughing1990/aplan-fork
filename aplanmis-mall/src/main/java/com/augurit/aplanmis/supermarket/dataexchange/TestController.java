package com.augurit.aplanmis.supermarket.dataexchange;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.supermarket.dataexchange.service.UploadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/province/job")
public class TestController {
    @Autowired
    private UploadDataService uploadDataService;

    @RequestMapping("/testservice")
    public ResultForm testSerivce() throws Exception {
        uploadDataService.insertOne();
        return new ResultForm(true, "suceess");
    }
}
