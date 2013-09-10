package com.edi.test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientUtils {
    private static final Log log = LogFactory.getLog(HttpClientUtils.class);
    private static PoolingClientConnectionManager cm = null;
    //private static HttpClient client = null;
    
    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));

        cm = new PoolingClientConnectionManager(schemeRegistry);
        
        try {
            int maxTotal = 100;
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
            log.error(
                    "Key[httpclient.max_total] Not Found in systemConfig.properties",
                    e);
        }
        // 每条通道的并发连接数设置（连接池）
        try {
            int defaultMaxConnection = 20;
            cm.setDefaultMaxPerRoute(defaultMaxConnection);
        } catch (NumberFormatException e) {
            log.error(
                    "Key[httpclient.default_max_connection] Not Found in systemConfig.properties",
                    e);
        }
        
    }

    public static HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        //params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 3000ms

        HttpClient client = new DefaultHttpClient((ClientConnectionManager)cm, params);
        return client; 
    }

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            long l1 = System.currentTimeMillis();
            HttpClient client = getHttpClient();

            HttpGet get = new HttpGet("http://127.0.0.1:8080/Download/");
            //get.setHeader("Connection", "close");
            client.getParams().setBooleanParameter( "http.protocol.expect-continue" , false );
            HttpProtocolParams.setVersion(client.getParams(), HttpVersion.HTTP_1_1);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                long l = entity.getContentLength();
                System.out.println("回应结果长度:" + l);
            }
            //get.releaseConnection();
            System.out.println("查询耗时" + (System.currentTimeMillis() - l1));
        }
    }

}
