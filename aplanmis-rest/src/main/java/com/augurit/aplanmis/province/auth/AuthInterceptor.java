package com.augurit.aplanmis.province.auth;

import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.province.vo.GetAccessTokenTipVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 省平台接口权限校验拦截器
 *
 * @author:chendx
 * @date: 2019-06-12
 * @time: 15:54
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AuthConfig authConfig;

    String[] includeUrls = new String[]{"/getApproveItem", "/getProjectDoc"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //启用外部单点时需要注释，直接返回true.
        String uri = request.getRequestURI();
        if (isNeedIntercept(uri)) {
            log.info("拦截地址 : " + uri);
            boolean has_authority = false;
            String access_token = request.getParameter("access_token");
            if (StringUtils.isNotBlank(access_token)) {
                //token明文
                String tokenStr = stringRedisTemplate.opsForValue().get(access_token);
                //判断redis中是否存在token
                if (StringUtils.isNotBlank(tokenStr)) {
                    has_authority = true;
                    //如果需要进一步解密token，并比较结果是否和token明文一致，则获取系统账号密码进行解密操作
//                    String key = authConfig.getUser() + "-" + authConfig.getPassword();
//                    String decrypt = AesUtil.decrypt(access_token, key);
//                    if(tokenStr.equals(decrypt)){
//                        has_authority = true;
//                    }
                }
            }
            if (!has_authority) {
                String getTokenUrl = request.getContextPath() + "/authentication?user=XXX&password=XXX";
                GetAccessTokenTipVo result = new GetAccessTokenTipVo();
                result.setResult(false);
                result.setMessage("您没有权限访问，请先使用账号密码调用getAccessTokenUrl获取access_token！");
                result.setGetAccessTokenUrl(getTokenUrl);
                //浏览器用utf8来解析返回的数据
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                //servlet用UTF-8转码，而不是用默认的ISO8859
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }
        }
        return true;
    }

    private boolean isNeedIntercept(String uri) {
        if (StringUtils.isNotBlank(uri)) {
            for (String str : includeUrls) {
                if (uri.contains(str)) return true;
            }
        }
        return false;
    }
}
