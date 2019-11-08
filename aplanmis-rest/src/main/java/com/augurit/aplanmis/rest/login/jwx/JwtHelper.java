package com.augurit.aplanmis.rest.login.jwx;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * @description 解析JWT
 * @author mohaoqi
 * @date 2019/9/3 0003
 ***/
public class JwtHelper {

    /****
     * 生成token
     * @param object
     * @return String
     * ***/
    public static String createJWT(Object object) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(Jwx.CLAIM_KEY_USER, object);
        claims.put(Jwx.CLAIM_KEY_CREATED, new Date());
        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + Jwx.expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, Jwx.secret)
                .compact();
    }

    /**
     * 通过token 获取用户对象.
     *
     * @param authToken 用戶token
     * @return String
     */
    public static Map<String, Object> getObjectFormToken(String authToken) {
        Map<String, Object> map = null;
        Claims claims = Jwts.parser().setSigningKey(Jwx.secret).parseClaimsJws(authToken).getBody();
        Gson gson = new Gson();
        map = gson.fromJson(claims.getSubject(), Map.class);
        return map;
    }

    /**
     * 获取设置的token 失效时间.
     *
     * @param authToken 用户token
     * @return date
     */
    public Date getExpirationDateFromToken(String authToken) throws Exception {
        final Claims claims = Jwts.parser().setSigningKey(Jwx.secret).parseClaimsJws(authToken).getBody();
        ;
        return claims.getExpiration();
    }

    /**
     * Token失效校验.
     *
     * @param authToken token字符串
     * @param m         用户对象
     * @return
     */
    public Boolean ValidateToken(String authToken, Map<String, Object> m) throws Exception {
        final Map<String, Object> map = this.getObjectFormToken(authToken);
        if (map != null && map.get("loginName") != null &&
                map.get("loginName").equals(m.get("loginName"))) {
            final Date expiration = getExpirationDateFromToken(authToken);
            return expiration.before(new Date());
        } else {
            return false;

        }
    }

    /**
     * 删除Token
     *
     * @param response 请求响应
     */
    public static void reMoveToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
