package com.augurit.aplanmis.mall.login;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.CommonLoginService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.login.jwx.AccessToken;
import com.augurit.aplanmis.mall.login.jwx.JwtHelper;
import com.augurit.aplanmis.mall.login.jwx.Jwx;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("rest/mall")
@Api(value = "登录",tags = "登录 --> 相关接口")
public class LoginController {
    Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private CommonLoginService commonLoginService;
    @Value("${aplanmis.mall.skin:skin_v4.1/}")
    private String skin;

    Gson gson = new Gson();

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({@ApiImplicitParam(value = "用户名",name = "userName",required = true,dataType = "string"),
            @ApiImplicitParam(value = "密码(md5加密)",name = "password",required = true,dataType = "string")} )
    public ContentResultForm login(String userName, String password, HttpServletRequest request, HttpServletResponse response){
        try {
            ContentResultForm login = commonLoginService.login(userName, password, "1", request);
            if (login.isSuccess()) {
                LoginInfoVo loginInfoVo=(LoginInfoVo)login.getContent();
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("loginName",loginInfoVo.getUserId()==null?loginInfoVo.getUnitName():loginInfoVo.getPersonName());
                String accessToken = JwtHelper.createJWT(gson.toJson(map));
                AccessToken accessTokenEntity = new AccessToken();
                accessTokenEntity.setAccessToken(accessToken);
                accessTokenEntity.setExpiresIn(Jwx.expiration);
                accessTokenEntity.setTokenType(Jwx.CLAIM_TOKEN_TYPE);
                Cookie cookie = new Cookie(URLEncoder.encode(Jwx.CLAIM_KEY_NAME, "utf-8"),
                        URLEncoder.encode(accessToken, "utf-8"));
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }

            return login;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ContentResultForm<>(false,"登录出错");
        }
    }


    @GetMapping("/loginIndex")
    @ApiOperation(value = "登录页面")
    public ModelAndView toIndexPage(){
        return new ModelAndView("mall/"+skin+"login/login");
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出登录")
    public ResultForm logout(HttpServletRequest request,HttpServletResponse response){
        try{
            jwtHelper.reMoveToken(response);
            return commonLoginService.logout(request);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"退出登录");
        }
    }


    @GetMapping("/user/changeApplicant/{unitInfoId}")
    @ApiOperation(value = "委托人切换单位接口")
    public ResultForm changeApplicant(HttpServletRequest request, @PathVariable String unitInfoId){
        try{
            return commonLoginService.changeApplicant(request,unitInfoId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"委托人切换单位接口出错");
        }
    }

    @GetMapping("/checkVerifyCode")
    @ApiOperation(value = "校验验证码")
    public ResultForm checkVerifyCode(HttpServletRequest request,String verifyCode){
        try{
            HttpSession httpSession = request.getSession();
            String sessionNum = (String)httpSession.getAttribute("opusSsoServerVerifyCode");
            if (StringUtils.isBlank(verifyCode))  return new ResultForm(false,"验证码为空");
            if (verifyCode.toLowerCase().equals(sessionNum.toLowerCase()))  return new ResultForm(true,"校验成功");
            return new ResultForm(false,"验证码错误");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"校验验证码失败");
        }
    }
}
