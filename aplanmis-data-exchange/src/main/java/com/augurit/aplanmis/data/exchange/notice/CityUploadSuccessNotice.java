package com.augurit.aplanmis.data.exchange.notice;

import com.augurit.aplanmis.data.exchange.dto.HttpResponseEntity;
import com.augurit.aplanmis.data.exchange.service.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 * @Date 2019/7/17
 */
@Component
@Slf4j
public class CityUploadSuccessNotice implements Notice {

    HttpClientService httpClientService = new HttpClientService();

    @Autowired
    CityNoticeProperties cityNoticeProperties;

    @Override
    public Boolean notice() {
        boolean allSuccess = true;
        String[] serverUrls = cityNoticeProperties.getServerUrls();
        for(String url:serverUrls){
            try {
                HttpResponseEntity httpResponseEntity = httpClientService.get(url);
                if(httpResponseEntity.getStatusCode() != 200){
                    allSuccess = false;
                    log.error("调用接口失败：{}",url);
                }else{
                    log.info("调用接口成功：{}",url);
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error("调用接口失败：{}",url);
            }

        }
        return allSuccess;
    }

}
