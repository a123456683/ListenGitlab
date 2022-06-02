package com.mindlinker.listengitlab.untis;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public final class HttpUtils {
    private HttpUtils() {
    }

    public static String sendPostRequest(String url, Map<String, Object> map, Map<String, Object> httpHead) {
        String result = "";
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            // 创建一个提交数据的容器
            List<BasicNameValuePair> parames = new ArrayList<>();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                parames.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
            }
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(parames, "UTF-8"));
                if (httpHead != null && httpHead.size() > 0) {
                    Set<String> httpHeadKeys = httpHead.keySet();
                    for (String key : httpHeadKeys) {
                        httpPost.addHeader(key, String.valueOf(httpHead.get(key)));
                    }
                }
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            log.error("Exception", e);
            log.error("result = " + result);
        }
        return result;
    }

    public static String sendGetRequest(String url, Map<String, Object> httpHead) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (httpHead != null && httpHead.size() > 0) {
            Set<String> keys = httpHead.keySet();
            for (String key : keys) {
                httpGet.addHeader(key, String.valueOf(httpHead.get(key)));
            }
        }
        CloseableHttpResponse response = null;
        String resp = "";
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("Exception", e);
            log.error("result = " + resp);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("Exception", e);
                }
            }
        }
        return resp;
    }

    public static String sendDeleteRequest(String url, Map<String, Object> map, Map<String, Object> httpHead) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");

        if (httpHead != null && httpHead.size() > 0) {
            Set<String> keys = httpHead.keySet();
            for (String key : keys) {
                httpDelete.addHeader(key, String.valueOf(httpHead.get(key)));
            }
        }
        String resp = "";
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("Exception", e);
            log.error("resp = " + resp);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("Exception", e);
                }
            }
        }
        return resp;
    }
}
