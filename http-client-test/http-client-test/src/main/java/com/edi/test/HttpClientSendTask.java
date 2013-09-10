package com.edi.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientSendTask implements Runnable{

    private static PoolingClientConnectionManager cm = null;
    private static  HttpClient client = null;
    private static final int MAX_TOTAL = 500;
    private static final int MAX_PER_ROUTE = 125;
    
    private void lazyInit()
    {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));
        cm = new PoolingClientConnectionManager(schemeRegistry);
        /*cm = new BasicClientConnectionManager();*/
        
        try {
            cm.setMaxTotal(MAX_TOTAL);
         // 每条通道的并发连接数设置（连接池）
            cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        //params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 3000ms
        HttpConnectionParams.setConnectionTimeout(params, 12*1000);
        client = new DefaultHttpClient((ClientConnectionManager)cm, params);
        
    }
    
    public void run() {
        
        if(cm==null)
        {
            synchronized (HttpClientSendTask.class) {
                if(cm==null)
                {
                    lazyInit();
                }
            }
        }
        
        
        HttpGet get = new HttpGet("http://127.0.0.1:8080/Download/");
        //get.setHeader("Connection", "close");
        //client.getParams().setBooleanParameter( "http.protocol.expect-continue" , false );
        //HttpProtocolParams.setVersion(client.getParams(), HttpVersion.HTTP_1_1);
        HttpResponse response = null;
        try {
            response = client.execute(get);
            handleResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response!=null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            long l = entity.getContentLength();
            System.out.println("回应结果长度:" + l);
        }
    }
    
    private void handleResponse(HttpResponse response)
    {
        InputStream stream;
        HttpEntity entity = response.getEntity();
        if (entity == null) 
            return;
        try {
            stream = new BufferedInputStream(entity.getContent());
            stream.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        ExecutorService pool = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 100000; i++) {
            pool.execute(new HttpClientSendTask());
        }
        
        pool.shutdown();
        
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

}
