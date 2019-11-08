package com.augurit.aplanmis.rest.common.utils;

import com.augurit.aplanmis.rest.common.vo.LoginInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author tiantian
 * @date 2019/6/4
 */
public class SessionUtil {
    public final static String SESSION_USER_KEY = "SESSION_USER_KEY";

    /**
     * 获取session下的登录信息
     *
     * @param request
     * @return
     */
    public static LoginInfoVo getLoginInfo(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        return (LoginInfoVo) httpSession.getAttribute(SESSION_USER_KEY);
    }

    /**
     * 清除session信息
     *
     * @param request
     */
    public static void clearSession(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_USER_KEY);
        request.getSession().invalidate();
    }

    public static LoginInfoVo saveLoginInfoVo(HttpServletRequest request, LoginInfoVo loginInfoVo) {
        HttpSession httpSession = request.getSession();
        String sid = httpSession.getId();
        httpSession.setAttribute(SESSION_USER_KEY, loginInfoVo);
        loginInfoVo.setSid(sid);
        return loginInfoVo;
    }

    public static String getSessionUserId(HttpServletRequest request) {
        LoginInfoVo loginInfoVo = getLoginInfo(request);
        if (loginInfoVo != null) {
            return loginInfoVo.getUserId();
        }
        return "";
    }

    public static String getSessionUnitId(HttpServletRequest request) {
        LoginInfoVo loginInfoVo = getLoginInfo(request);
        if (loginInfoVo != null) {
            return loginInfoVo.getUnitId();
        }
        return "";
    }

    public static Boolean isPersonAccount(HttpServletRequest request) {
        LoginInfoVo loginInfoVo = getLoginInfo(request);
        if (loginInfoVo != null && "1".equals(loginInfoVo.getIsPersonAccount())) {
            return true;
        }
        return false;
    }

    //待处理
    public static void updateLoginSession(HttpServletRequest request) {

    }
}
