package com.augurit.aplanmis.rest.util;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.StringUtils;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUtil {
    private static final Long EXPIRE_MILLIS = 2 * 60 * 60 * 1000L;

    private static final Map<String, MallToken> tokenMap = new ConcurrentHashMap<>();
    public static final String CURRENT_ORG_ID = "0368948a-1cdf-4bf8-a828-71d796ba89f6";

    public static boolean validateToken(String token) {
        MallToken currentToken = tokenMap.get(token);
        // 验证是否有效
        if (currentToken != null) {
            if (notExpired(currentToken.getExpire())) {
                currentToken.refresh();
                return true;
            } else {
                currentToken.remove();
            }
        }
        return false;
    }

    private static boolean notExpired(Long tokenMillis) {
        return tokenMillis + EXPIRE_MILLIS >= System.currentTimeMillis();
    }

    public static String generateToken(String linkmanInfoId, String loginUserId, String loginUserName) {
        if (StringUtils.isBlank(loginUserId)) {
            throw new RuntimeException("登录失败，无法获取用户");
        }
        String tokenStr = UuidUtil.generateUuid();
        MallToken token = new MallToken(tokenStr, System.currentTimeMillis(), loginUserId, loginUserName, linkmanInfoId);
        tokenMap.put(tokenStr, token);

        return tokenStr;
    }

    public static String getCurrentLoginUserId(String token) {
        return tokenMap.get(token).getLoginUserId();
    }

    public static String getCurrentLoginUserName(String token) {
        return tokenMap.get(token).getLoginUserName();
    }

    public static String getCurrentLoginOrgId(String token) {
        return tokenMap.get(token).getCurrentOrgId();
    }

    public static String getLinkmanInfoId(String token) {
        return tokenMap.get(token).getLinkmanInfoId();
    }

    @Data
    private static class MallToken {
        private String tokenStr;
        private Long expire;
        private String loginUserId;
        private String loginUserName;
        private String currentOrgId;
        private String linkmanInfoId;

        MallToken(String tokenStr, Long expire, String loginUserId, String loginUserName, String linkmanInfoId) {
            this.tokenStr = tokenStr;
            this.expire = expire;
            this.loginUserId = loginUserId;
            this.linkmanInfoId = linkmanInfoId;
            this.loginUserName = loginUserName;
            this.currentOrgId = CURRENT_ORG_ID;
        }

        void refresh() {
            expire = System.currentTimeMillis();
        }

        void remove() {
            tokenMap.remove(tokenStr);
        }
    }

}
