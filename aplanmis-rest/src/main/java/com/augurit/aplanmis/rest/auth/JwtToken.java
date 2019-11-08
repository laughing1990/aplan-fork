package com.augurit.aplanmis.rest.auth;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {

    /**
     * token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj
     */
    private static final String SECRET = "JKKLJOoasdlfj";
    /**
     * token 过期时间
     */
    private static final int calendarField = Calendar.DATE;
    private static final int calendarInterval = 5;

    private static final String ISSUER = "augurit";
    private static final String AUDIENCE = "aplanmis-rest";

    public static final String USER_INFO = "userInfo";

    /**
     * JWT生成Token.<br/>
     * <p>
     * JWT构成: header, payload, signature
     * <p>
     * 登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(Map<String, Object> userInfo) {
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        return Jwts.builder().setHeader(map) // header
                .setIssuer(ISSUER) // payload
                .setAudience(AUDIENCE)
                .setIssuedAt(new Date()) // sign time
                .setExpiration(expiresDate) // expire time
                .setClaims(userInfo)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 解密Token
     *
     * @param jwtToken token string
     * @throws Exception 验证不通过抛异常
     */
    private static Map<String, Object> verifyToken(String jwtToken) throws Exception {
        try {
            Object body = Jwts.parser().setSigningKey(SECRET)
//                    .requireAudience(AUDIENCE)
//                    .requireIssuer(ISSUER)
                    .parse(jwtToken)
                    .getBody();
            return (Map<String, Object>) body;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new Exception("Token校验失败");
        }
    }

    public static AuthUser getUserInfo(String token) throws Exception {
        Map<String, Object> claims = verifyToken(token);
        return new Gson().fromJson(claims.get(USER_INFO).toString(), AuthUser.class);
    }
}

