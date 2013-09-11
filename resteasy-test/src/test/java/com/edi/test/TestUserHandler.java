package com.edi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class TestUserHandler {

    public static final String TARGET_POST_URL = "http://127.0.0.1:8080/resteasy-test/users/create";
    public static final String TARGET_GET_URL = "http://127.0.0.1:8080/resteasy-test/users/1";
    
    @Test
    public void testCreate() throws IOException {
        URL url = new java.net.URL(TARGET_POST_URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/xml");
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(1000);
        
        String userXML = "<user><id>1</id><name>test</name></user>";
        OutputStream os = conn.getOutputStream();
        os.write(userXML.getBytes());
        os.flush();
        
        Assert.assertEquals(HttpURLConnection.HTTP_CREATED, conn.getResponseCode());
        conn.disconnect();
    }
    
    @Test
    public void testGet() throws IOException {
        testCreate();
        URL url = new java.net.URL(TARGET_GET_URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        System.out.println("Result:");
        String out = null;
        while((out=reader.readLine())!=null){
            System.out.println(out);
        }
        Assert.assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
        conn.disconnect();
    }

}
