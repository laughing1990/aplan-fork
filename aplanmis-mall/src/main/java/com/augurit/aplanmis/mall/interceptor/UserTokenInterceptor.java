package com.augurit.aplanmis.mall.interceptor;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.login.jwx.JwtHelper;
import com.augurit.aplanmis.mall.login.jwx.Jwx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Map;

@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserTokenInterceptor.class);

    private static final String MALL_URL = "/rest/mall/loginIndex";

    private static final String SUPERMARKET_URL = "/supermarket/main/login.html";

    private static final String excludeUrlKey = "supermarket";

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Jwx.CLAIM_KEY_NAME.equals(URLDecoder.decode(cookie.getName(), "utf-8"))) {
                    authToken = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
            }
        }
        if (StringUtils.isBlank(authToken)||SessionUtil.getLoginInfo(request)==null) {
            redirect(request, response);
            return false;
        } else {
            try {
                Map<String, Object> map = jwtHelper.getObjectFormToken(authToken);
                if (jwtHelper.ValidateToken(authToken, map)) {
                    redirect(request, response);
                    return false;
                }
                SecurityContext.getOpusLoginUser();
            } catch (Exception e) {
                redirect(request, response);
                return false;
            } catch (Throwable err) {
                redirect(request, response);
                return false;
            }
        }
        return true;
    }

    public void redirect(HttpServletRequest request, HttpServletResponse response) {
        String url=request.getRequestURI().contains(excludeUrlKey)? SUPERMARKET_URL : MALL_URL;
        PrintWriter out=null;
        try {
            out = response.getWriter();
            out.write("<html>");
            out.write("<script>");
            out.write("top.location.href='" + request.getContextPath() + url + "'");
            out.write("</script>");
            out.write("</html>");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }

}
