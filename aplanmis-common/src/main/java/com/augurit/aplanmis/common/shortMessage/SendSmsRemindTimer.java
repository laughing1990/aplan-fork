package com.augurit.aplanmis.common.shortMessage;

import com.augurit.agcloud.bsc.domain.BscJobRemind;
import com.augurit.agcloud.bsc.mapper.BscJobRemindMapper;
import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.shortMessage.utils.SendMsmContent;
import com.augurit.aplanmis.common.shortMessage.utils.SendSmsUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信提醒发送定时器
 * @Author:sam
 */
//@Component
@Slf4j
public class SendSmsRemindTimer extends JobTimer {
    @Autowired
    private BscJobRemindMapper bscJobRemindMapper;
    @Autowired
    private AplanmisSmsConfigProperties smsConfigProperties;

    public void trigger(){
        //短信发送如果关闭则跳过
        if(!smsConfigProperties.isEnable()) {
//            log.info("短信发送关闭,不发送短信");
            return;
        }

        BscJobRemind jobRemind = new BscJobRemind();
        jobRemind.setRemindMode("3");//短信发送
        try {
            List<BscJobRemind> list = bscJobRemindMapper.listBscJobRemind(jobRemind);
            if(list!=null&&list.size()>0){
                for(BscJobRemind bscJobRemind:list){
                    String content = bscJobRemind.getRemindContent();

                    if(StringUtils.isNotBlank(content)){
                        Gson gson = new Gson();
                        SendMsmContent sendMsmContent = gson.fromJson(content,SendMsmContent.class);

                        if(sendMsmContent!=null){
                            ResultForm resultForm = SendSmsUtil.flowNodeSendSms(sendMsmContent.getPhoneNum(),sendMsmContent.getTemplateParamJson(),
                                    sendMsmContent.getTemplateId(),smsConfigProperties);
                            log.info("提醒短息信息发送成功，已发送到："+sendMsmContent.getPhoneNum()+";发送结果为："+resultForm.toString());

                            if(resultForm.isSuccess()){
                                log.info("-----正在删除发送成功的记录 start--------------");
                                bscJobRemindMapper.deleteBscJobRemind(bscJobRemind.getRemindId());
                                log.info("-----正在删除发送成功的记录 end----------------");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("短信定时发送失败，失败原因："+e.getMessage());
        }
    }
}
