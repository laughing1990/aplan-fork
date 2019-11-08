package com.augurit.aplanmis.data.exchange.service;

import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.data.exchange.dto.HttpResponseEntity;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientService {

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public HttpResponseEntity get(String url) {
        return get(url, null, null);
    }

    public HttpResponseEntity get(String url, Map<String, String> params) {
        return get(url, params, null);
    }

    public HttpResponseEntity get(String url, Map<String, String> params, Map<String, String> headers) {
        return execute(() -> {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            setHeaderParams(httpGet, headers);
            return httpGet;
        });
    }

    public HttpResponseEntity postNotJson(String url) {
        return post(url, null, false);
    }

    public HttpResponseEntity postNotJson(String url, Map<String, String> params) {
        return post(url, params, false);
    }

    public HttpResponseEntity post(String url) {
        return post(url, null, true);
    }

    public HttpResponseEntity post(String url, Map<String, String> params) {
        return post(url, params, true);
    }

    public HttpResponseEntity post(String url, Map<String, String> params, Map<String, String> headerParams) {
        return post(url, params, true, headerParams);
    }

    public HttpResponseEntity post(String url, Map<String, String> params, boolean json) {
        return execute(() -> {
            HttpPost httpPost = new HttpPost(url);
            setParams(httpPost, params, json);
            return httpPost;
        });
    }

    public HttpResponseEntity post(String url, Map<String, String> params, boolean json, Map<String, String> headerParams) {
        return execute(() -> {
            HttpPost httpPost = new HttpPost(url);
            setParams(httpPost, params, json);
            setHeaderParams(httpPost, headerParams);
            return httpPost;
        });
    }

    private void setHeaderParams(HttpUriRequest request, Map<String, String> headerParams) {
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                request.addHeader(key, headerParams.get(key));
            }
        }
    }

    public HttpResponseEntity put(String url, Map<String, String> params) {
        return put(url, params, true);
    }

    public HttpResponseEntity put(String url, Map<String, String> params, boolean json) {
        return execute(() -> {
            HttpPut httpPut = new HttpPut(url);
            setParams(httpPut, params, json);
            return httpPut;
        });
    }

    public HttpResponseEntity delete(String url, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put("_method", "delete");
        return post(url, params, false);
    }

    /**
     * 设置请求参数
     */
    private void setParams(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, Map<String, String> params, boolean json) {
        if (json) {
            try {
                httpEntityEnclosingRequestBase.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpEntityEnclosingRequestBase.setEntity(new UrlEncodedFormEntity(parameters, Consts.UTF_8));
    }

    private HttpResponseEntity execute(HttpClientFunction function) {
        CloseableHttpResponse response = null;
        try {
            HttpUriRequest request = function.getRequsetMothod();
            response = httpClient.execute(request);
            String content = response.getEntity() == null ? null
                    : EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FunctionalInterface
    private interface HttpClientFunction {
        HttpUriRequest getRequsetMothod() throws URISyntaxException;
    }

}
