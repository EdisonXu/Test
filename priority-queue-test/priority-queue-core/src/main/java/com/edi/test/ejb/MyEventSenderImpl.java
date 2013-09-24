package com.edi.test.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.edi.test.bean.MyEventTO;
import com.edi.test.ifc.MyEventHttpSender;
import com.edi.test.ifc.MyEventSendClient;
import com.edi.test.ifc.MyConfig;
import com.edi.test.restful.bean.MyEventType;
import com.edi.test.restful.bean.StatusReportType;
import com.edi.test.restful.util.SSLSocketFactoryHelper;

@Stateless()
public class MyEventSenderImpl implements MyEventHttpSender {

    // max session per route
    // should not be smaller than the NbiEventCacheUtil.SENDER_POOL_SIZE
    public static int MAX_PER_ROUTE = 500;
    
    public static int MAX_TOTAL = 2000;
    
    // shared by different threads
    private static PoolingClientConnectionManager cm = null;
    
    @PostConstruct
    public void init() {
        // this initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

    }
    
    @Override
    public boolean send(MyEventTO event) {
        
        // prepare event
        MyEventType bmscEvent = generateEvent(event);
        
        lazyInit();
        
        try
        {
            ClientResponse<StatusReportType> response =  doSend(bmscEvent);
            handleResponse(bmscEvent, response) ;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            
            return false;
        }
        
        return true;
    }
    
    private void lazyInit()
    {
        // lazy initialization
        
        if(cm==null)
        {
            synchronized (MyEventSenderImpl.class) {
                if(cm==null)
                {
                    initConnectionManager();
                }
            }
        }
    }
    
    private void handleResponse(MyEventType bmscEvent, ClientResponse<StatusReportType> response) 
    {
        Response.Status status = null;

        try {
            status = response.getResponseStatus();
                
            System.out.println("Get response status code "
                    + status.getStatusCode() + " for event '" + bmscEvent.getNotificationType() 
                    + ":" + bmscEvent.getDescription() + "'");
            
            if (status.getFamily() != Response.Status.Family.SUCCESSFUL) {
                try {
                    
                    StatusReportType report = response.getEntity();
                    System.out.println("Status_Report_Info: ID is " + report.getErrorId() 
                            + ", Description is " + report.getDescription() + ", Version is " + report.getVersion());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }finally
        {
            // From the RestEasy comments:
            // Apache Client 4 is stupid,  You have to get the InputStream and close it if there is an entity
            // otherwise the connection is never released.  There is, of course, no close() method on response
            // to make this easier.
            if(response!=null)
                response.releaseConnection();
        }
    }
    
    private ClientResponse<StatusReportType> doSend(MyEventType bmscEvent) {
        
        MyEventSendClient client = ProxyFactory.create(MyEventSendClient.class, MyConfig.CONSUMER_URL,
                buildMutiThreadExecutor(isHttpsRequest(MyConfig.CONSUMER_URL)));
        //String msgBody = JAXBHelper.marshallXmlElement(bmscEvent, false, false);
        String msgBody = "dummy";
        if (msgBody == null) {
            return null;
        }

        // send event....
        return client.sendEvent(MyConfig.CONSUMER_NAME, msgBody);
    }
    
    private boolean isHttpsRequest(String url) {
        if (url == null) {
            return false;
        }

        return url.toLowerCase().startsWith("https");
    }
    
    private void initConnectionManager() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        
        // default HTTP port as 80
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        
        // default HTTPS port as 8443
        SSLSocketFactory sslFactory = SSLSocketFactoryHelper.getDefaultSSLSocketFactory(MyConfig.KEYSTORE_PATH,
                MyConfig.KEYSTORE_PASS, MyConfig.TRUSTSTORE_PATH, MyConfig.TRUSTSTORE_PASS);
        if (sslFactory != null) {
            schemeRegistry.register(new Scheme("https", 8443, sslFactory));
        } else {
            System.out.println("Using default SSLSocketFactory because failed to initiate SSLSocketFactory with configured keystore and password.");
            schemeRegistry.register(new Scheme("https", 8443, SSLSocketFactory.getSocketFactory()));
        }
        cm = new PoolingClientConnectionManager(schemeRegistry);
        
        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        cm.setMaxTotal(MAX_TOTAL);
    }
    
    private ClientExecutor buildMutiThreadExecutor(boolean isHttps) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, MyConfig.RETRY_INTERVAL_TIME_IN_SEC*1000);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpClient httpClient = new DefaultHttpClient((ClientConnectionManager)cm, params);
        return new ApacheHttpClient4Executor(httpClient);
    }

    private MyEventType generateEvent(MyEventTO event){
        MyEventType bmscEvent = new MyEventType();
        bmscEvent.setDescription(event.getDescription());
        bmscEvent.setSessionId(event.getSessionId());
        bmscEvent.setNotificationType(String.valueOf(event.getNotificationType().getValue()));
        bmscEvent.setVersion(event.getVersion());
        bmscEvent.setVersion(MyEventTO.DEFAULT_VERSION); // always is 1.0
        return bmscEvent;
    }
}
