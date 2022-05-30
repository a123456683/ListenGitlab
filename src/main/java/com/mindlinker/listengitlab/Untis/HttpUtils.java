package com.mindlinker.listengitlab.Untis;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private HttpUtils() {
    }

    public static String sendPostRequest(String url, Map<String, Object> map, String tokenKey, String tokenValue) {
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
                httpPost.addHeader(tokenKey, tokenValue);
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
            e.printStackTrace();
        }
        return result;
    }

    public static String sendGetRequest(String url, String tokenKey, String tokenValue) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (StringUtils.hasLength(tokenKey)){
            httpGet.addHeader(tokenKey, tokenValue);
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return resp;
    }
}
