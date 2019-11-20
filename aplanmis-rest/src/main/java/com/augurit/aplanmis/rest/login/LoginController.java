package com.augurit.aplanmis.rest.login;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.auth.JwtToken;
import com.augurit.aplanmis.rest.common.service.CommonLoginService;
import com.augurit.aplanmis.rest.login.jwx.JwtHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/mall")
@Api(value = "登录", tags = "登录 --> 相关接口")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private CommonLoginService commonLoginService;

    @PostMapping("/mobile/login")
    @ApiOperation(value = "移动端登录")
    @ApiImplicitParams({@ApiImplicitParam(value = "用户名", name = "userName", required = true, dataType = "string"),
            @ApiImplicitParam(value = "密码(md5加密)", name = "password", required = true, dataType = "string")})
    public ResultForm mobileLogin(String userName, String password) {
        String accessToken;
        try {
            AuthUser authUser = commonLoginService.mobileLogin(userName, password);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put(JwtToken.USER_INFO, authUser);
            accessToken = JwtToken.createToken(userInfo);
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
        return new ContentResultForm<>(true, accessToken, "login success");
    }


    @GetMapping("/loginIndex")
    @ApiOperation(value = "登录页面")
    public ModelAndView toIndexPage() {
        return new ModelAndView("mall/login/login");
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出登录")
    public ResultForm logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            JwtHelper.reMoveToken(response);
            return commonLoginService.logout(request);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }


    @GetMapping("/user/changeApplicant/{unitInfoId}")
    @ApiOperation(value = "委托人切换单位接口")
    public ResultForm changeApplicant(HttpServletRequest request, @PathVariable String unitInfoId) {
        try {
            return commonLoginService.changeApplicant(request, unitInfoId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }
}
