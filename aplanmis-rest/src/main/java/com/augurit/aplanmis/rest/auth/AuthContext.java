package com.augurit.aplanmis.rest.auth;

import org.springframework.util.Assert;

public final class AuthContext {

    private static final ThreadLocal<AuthUser> currentUser = new ThreadLocal<>();

    public static void clearContext() {
        currentUser.set(null);
    }

    public static AuthUser getCurrentUser() {
        return currentUser.get();
    }

    public static String getCurrentLinkmanInfoId() {
        return getCurrentUser().getLinkmanInfoId();
    }

    public static String getCurrentLinkmanInfoName() {
        return getCurrentUser().getLinikmanName();
    }

    public static String getCurrentUnitInfoName() {
        return getCurrentUser().getUnitInfoName();
    }

    public static String getCurrentUnitInfoId() {
        return getCurrentUser().getUnitInfoId();
    }

    public static String getCurrentLoginName() {
        return getCurrentUser().getLoginName();
    }

    public static String getCurrentOrgId() {
        return getCurrentUser().getCurrentOrgId();
    }

    public static String getCurrentOrgName() {
        return getCurrentUser().getCurrentOrgName();
    }

    public static String getCurrentRootOrgId() {
        return getCurrentUser().getRootOrgId();
    }

    public static boolean isPersionAccount() {
        return getCurrentUser().isPersonalAccount();
    }

    public static void setUserInfo(AuthUser userInfo) {
        Assert.notNull(userInfo, "userInfo must not null.");
        currentUser.set(userInfo);
    }
}
