package com.augurit.aplanmis.rest.filter;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.auth.JwtToken;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class MobileTokenFilter implements Filter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("Mobile Token filter init!!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getRequestURI().contains("/rest/user")) {
            // 获取请求头
            String accessToken = getAccessToken(httpServletRequest);
            // 解析
            try {
                AuthUser userInfo = JwtToken.getUserInfo(accessToken);
                AuthContext.setUserInfo(userInfo);

                com.augurit.agcloud.framework.security.user.OpuOmUser loginUser = new com.augurit.agcloud.framework.security.user.OpuOmUser();
                loginUser.setUserName(userInfo.getLoginName());
                loginUser.setUserId(userInfo.getLinkmanInfoId() == null ? userInfo.getUnitInfoId() : userInfo.getLinkmanInfoId());
                loginUser.setLoginName(userInfo.getLoginName());
                OpusLoginUser opusLoginUser = new OpusLoginUser();
                opusLoginUser.setCurrentOrgId(userInfo.getCurrentOrgId());
                opusLoginUser.setUser(loginUser);
                opusLoginUser.setCurrentTmn("pc");
                SecurityContext.setCurrentLoginUser(opusLoginUser, null);

                chain.doFilter(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                AuthContext.clearContext();
                SecurityContext.setCurrentLoginUser(null, null);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (StringUtils.isBlank(header)) {
            throw new RuntimeException("缺少请求头部Authorization");
        }
        return header;
    }

    @Override
    public void destroy() {
        log.info("Mobile Token filter destroyed!!");
    }
}
