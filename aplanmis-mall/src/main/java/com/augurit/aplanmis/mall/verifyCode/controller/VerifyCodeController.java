package com.augurit.aplanmis.mall.verifyCode.controller;

import com.augurit.aplanmis.common.utils.VerifyCodeUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("rest/verifycode")
@Api(value = "申报通用接口",tags = "申报 --> 申报通用接口")
public class VerifyCodeController {

    /**
     * 获取验证码
     * @param req
     * @param response
     */
    @RequestMapping(value = "/getCode", method = {RequestMethod.GET})
    @ResponseBody
    public void check(HttpServletRequest req, HttpServletResponse response, HttpSession session) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + "vcode.jpeg");

            VerifyCodeUtils validateCode = new VerifyCodeUtils();
            String number = validateCode.getNumber(4);
            validateCode.getImage(response.getOutputStream(), number);
            session.setAttribute("opusSsoServerVerifyCode",number);
        } catch (Exception e) {

        }
    }
}
