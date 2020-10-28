package com.cn.ldd;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author liudongdong
 * @Date 2020/10/28
 */
public class HelloHttpClient {
    private static PoolingHttpClientConnectionManager conMgr = null;

    static {
        conMgr = new PoolingHttpClientConnectionManager();
        conMgr.setMaxTotal(2000);
        conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());
    }

    private final static int DEFAULT_SOCKET_TIMEOUT = 300000;
    private final static int DEFAULT_CONNECT_TIMEOUT = 300000;

    private static CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(conMgr).build();

    public static void main(String[] args) {
        Object obj = doGet("http://localhost:8801/");
        System.out.println(obj);
    }
    public static Object doGet(String url) {

        HttpGet httpGet = new HttpGet(url);
        //设置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        /*header中的通用参数*/
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");


        CloseableHttpResponse httpResponse = null;
        Object result = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();    //会自动释放连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(">>> HttpGet返回状态码 >>>" + httpResponse.getStatusLine().getStatusCode());
        return result;
    }
}
