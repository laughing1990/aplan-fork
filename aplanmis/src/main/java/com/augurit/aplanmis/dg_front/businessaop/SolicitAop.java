package com.augurit.aplanmis.dg_front.businessaop;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.mapper.ExDockingAcountMapper;
import com.augurit.aplanmis.dg_front.DockingUtil;
import com.augurit.aplanmis.sysdocking.domain.ExDockingAcount;
import com.augurit.aplanmis.sysdocking.domain.ExDockingYczxzt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

//@Aspect
//@Component
public class SolicitAop {
    @Autowired
    private ExDockingAcountMapper exDockingAcountMapper;

    /**
     * 成功发起一次征询触发推送开始信息
     *
     * @param point
     * @return
     */
    @Around("execution(* com.augurit.aplanmis.front.solicit.service.impl.RestAeaHiSolicitServiceImpl.createSolicit(..))")
    public Object sendSolicitCreate(ProceedingJoinPoint point) {
        System.out.println("+++进入一次征询开始切面了+++");
        ResultForm result = new ContentResultForm<>(false);
        try {
            Object[] obj = point.getArgs();
            AeaHiSolicit dto = (AeaHiSolicit) obj[0];
            Object pointObj = point.proceed(obj);
            /*System.out.println(JSONObject.toJSONString(result));
            System.out.println(JSONObject.toJSONString(dto));*/
            if (dto.getBusType().equalsIgnoreCase("YJZQ")) {//调用接口推送一次征询信息
                doSendKaiShiYiCiZhengXun(dto);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }

    /**
     * 成功发起一次征询触发推送开始信息
     *
     * @param point
     * @return
     */
//    @Around("execution(* com.augurit.aplanmis.front.solicit.service.impl.RestAeaHiSolicitServiceImpl.createSolicitCollectOpinion(..))")
    public Object sendSolicitFinish(ProceedingJoinPoint point) {
        System.out.println("+++进入一次征询结束切面了+++");
        ResultForm result = new ContentResultForm<>(false);
        try {
            Object[] obj = point.getArgs();
            AeaHiSolicit dto = (AeaHiSolicit) obj[0];
            Object pointObj = point.proceed(obj);
            result = (ResultForm) pointObj;
            /*System.out.println(JSONObject.toJSONString(result));
            System.out.println(JSONObject.toJSONString(dto));*/
            if (result.isSuccess() && dto.getBusType().equalsIgnoreCase("YCZX")) {//调用接口推送一次征询信息
                doSendjieshuYiCiZhengXun(dto);
            }
        } catch (Throwable throwable) {
            result.setMessage(throwable.getMessage());
            throwable.printStackTrace();
        }

        return result;
    }

    private void doSendKaiShiYiCiZhengXun(AeaHiSolicit dto) throws Exception {
        ExDockingYczxzt ey = new ExDockingYczxzt();
        Date date = new Date();
        List<ExDockingAcount> dockingAcounts = exDockingAcountMapper.getDockingAcounts("0");
        if (dockingAcounts.size() != 1) {
            throw new Exception("获取外部地址权限信息查询失败！");
        }
        ExDockingAcount acount = dockingAcounts.get(0);
        String appKey = acount.getAppKey();
        String appSecret = acount.getAppSecret();
        String timestamp = date.toString();
        String src = timestamp + appKey + appSecret + timestamp; //合成验证参数
        String md5 = md5(src.getBytes()); //md5
        String captcha = md5.toUpperCase();
        ey.setAppKey(appKey);
        ey.setCaptcha(captcha);
        ey.setTimestamp(timestamp);
        ey.setSblsh(dto.getApplyinstId());
        ey.setKssj(date);
        String url = acount.getUrl() + "/yczxzt";
        DockingUtil.doPostByJson(url, JSONObject.toJSONString(ey));
    }

    private void doSendjieshuYiCiZhengXun(AeaHiSolicit dto) throws Exception {
        ExDockingYczxzt ey = new ExDockingYczxzt();
        Date date = new Date();
        List<ExDockingAcount> dockingAcounts = exDockingAcountMapper.getDockingAcounts("0");
        if (dockingAcounts.size() != 1) {
            throw new Exception("获取外部地址权限信息查询失败！");
        }
        ExDockingAcount acount = dockingAcounts.get(0);
        String appKey = acount.getAppKey();
        String appSecret = acount.getAppSecret();
        String timestamp = date.toString();
        String src = timestamp + appKey + appSecret + timestamp; //合成验证参数
        String md5 = md5(src.getBytes()); //md5
        String captcha = md5.toUpperCase();
        ey.setAppKey(appKey);
        ey.setCaptcha(captcha);
        ey.setTimestamp(timestamp);
        ey.setSblsh(dto.getApplyinstId());
        ey.setKssj(dto.getSolicitStartTime());
        ey.setJssj(date);
        ey.setYczxsjsx(3);
        ey.setYczxjg(dto.getConclusionFlag());
        ey.setZqyjzt("2");
        String url = acount.getUrl() + "/yczxzt";
        DockingUtil.doPostByJson(url, JSONObject.toJSONString(ey));
    }

    public static String md5(byte[] s) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
