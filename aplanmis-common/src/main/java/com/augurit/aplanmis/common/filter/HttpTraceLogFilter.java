package com.augurit.aplanmis.common.filter;

import com.augurit.agcloud.framework.util.JsonMapper;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author tiantian
 * @date 2019/8/9
 */
@Slf4j
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {

    private static final String[] IGNORE_URL_ARRAY = {"/ui-static/", "/auth/", "/druid/","/view/getViewDataCount","/getCountAoaMsgRangeForMyReceiveUnReaded"};

    private static final String[] IGNORE_CONTENT_TYPE_ARRAY ={"application/javascript","text/css","text/html","application/font-woff"};

    private final MeterRegistry registry;

    public HttpTraceLogFilter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }

        try {
            logRequest(request);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            String path = request.getRequestURI();
            if (checkPath(path) && checkContentType(request.getContentType()) && checkContentType(response.getContentType())) {

                JsonMapper jsonMapper = new JsonMapper();
                long latency = System.currentTimeMillis() - startTime;

                HttpTraceLog traceLog = new HttpTraceLog();
                traceLog.setPath(path);
                traceLog.setMethod(request.getMethod());
                traceLog.setTimeTaken(latency);
                traceLog.setTime(LocalDateTime.now().toString());
                traceLog.setParameterMap(jsonMapper.toJson(request.getParameterMap()));
                traceLog.setStatus(status);
                traceLog.setRequestBody(getRequestBody(request));
                traceLog.setResponseBody(getResponseBody(response));
                traceLog.setContentType(response.getContentType());

                log.info("Http trace log: {}", traceLog.toString());

            }
            updateResponse(response);
        }
    }


    private void logRequest(HttpServletRequest request){

        if (checkPath(request.getRequestURI()) && checkContentType(request.getContentType())) {

            StringBuffer sb = new StringBuffer("\n");

            sb.append("---------------请求数据-------------").append("\n")
                    .append("请求路径 ：" + request.getRequestURI()).append("\n")
                    .append("请求方式 ：" + request.getMethod()).append("\n")
                    .append("Content-Type ：" + request.getContentType()).append("\n")
                    .append("请求时间 ：" + LocalDateTime.now().toString()).append("\n")
                    .append("请求参数 ：" + new JsonMapper().toJson(request.getParameterMap())).append("\n")
                    .append("requestBody：" + getRequestBody(request)).append("\n")
                    .append("---------------请求数据-------------");

            log.info(sb.toString());
        }
    }

    private boolean checkContentType(String contentType){
        if(contentType==null){
            return false;
        }
        for(String s:IGNORE_CONTENT_TYPE_ARRAY){
            if(contentType.contains(s)){
                return false;
            }
        }

        return true;
    }

    private boolean checkPath(String path){
        if(path==null){
            return false;
        }
        for(String s:IGNORE_URL_ARRAY){
            if(path.contains(s)){
                return false;
            }
        }

        return true;
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                log.info(e.getMessage(),e);
            }
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                log.info(e.getMessage(),e);
            }
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }


    @Data
    private static class HttpTraceLog {

        private String path;
        private String parameterMap;
        private String method;
        private Long timeTaken;
        private String time;
        private Integer status;
        private String requestBody;
        private String responseBody;
        private String contentType;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("\n");

            sb.append("---------------返回数据--------------").append("\n")
                    .append("请求路径 ："+this.path).append("\n")
                    .append("请求方式 ："+this.method).append("\n")
                    .append("ContentType ："+this.contentType).append("\n")
                    .append("返回时间 ："+this.time).append("\n")
                    .append("请求参数 ："+this.parameterMap).append("\n")
                    .append("耗时："+this.timeTaken).append("ms\n")
                    .append("返回状态："+this.status).append("\n")
                    .append("requestBody："+this.requestBody).append("\n")
                    .append("responseBody："+this.responseBody).append("\n")
                    .append("---------------返回数据--------------");

            return sb.toString();
        }
    }
}