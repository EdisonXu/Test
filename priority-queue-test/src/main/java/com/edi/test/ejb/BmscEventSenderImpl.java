package com.edi.test.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
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

import com.edi.test.BmscEventTO;
import com.edi.test.NbiEventCacheUtil;
import com.edi.test.ifc.BccConfigManagerRemote;
import com.edi.test.ifc.BmscEventHttpSender;
import com.edi.test.ifc.BmscEventHttpSenderRemote;
import com.edi.test.restful.BmscEventSendClient;
import com.edi.test.restful.BmscEventType;
import com.edi.test.restful.SSLSocketFactoryHelper;
import com.edi.test.restful.StatusReportType;

@Stateless(mappedName = BmscEventHttpSenderRemote.JNDI_NAME)
@Remote(BmscEventHttpSenderRemote.class)
@Local(BmscEventHttpSender.class)
public class BmscEventSenderImpl implements BmscEventHttpSender {

    // max session per route
    // should not be smaller than the NbiEventCacheUtil.SENDER_POOL_SIZE
    public static int MAX_PER_ROUTE = 500;
    
    public static int MAX_TOTAL = 2000;
    
    // shared by different threads
    private static PoolingClientConnectionManager cm = null;
    
    private BccConfigManagerRemote config = null;
    
    @PostConstruct
    public void init() {
        // this initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

    }
    
    @Override
    public boolean send(BmscEventTO event) {
        
        // prepare event
        BmscEventType bmscEvent = generateEvent(event);
        
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
        if(config==null)
            config = NbiEventCacheUtil.getBccConfig();
        
        if(cm==null)
        {
            synchronized (BmscEventSenderImpl.class) {
                if(cm==null)
                {
                    initConnectionManager();
                }
            }
        }
    }
    
    private void handleResponse(BmscEventType bmscEvent, ClientResponse<StatusReportType> response) 
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
    
    private ClientResponse<StatusReportType> doSend(BmscEventType bmscEvent) {
        
        BmscEventSendClient client = ProxyFactory.create(BmscEventSendClient.class, config.getEmbmEventUrl(),
                buildMutiThreadExecutor(isHttpsRequest(config.getEmbmEventUrl())));
        //String msgBody = JAXBHelper.marshallXmlElement(bmscEvent, false, false);
        String msgBody = "dummy";
        if (msgBody == null) {
            return null;
        }

        // send event....
        return client.sendEvent(config.getBmscName(), msgBody);
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
        SSLSocketFactory sslFactory = SSLSocketFactoryHelper.getDefaultSSLSocketFactory(config.getKeyStorePath(),
                config.getKeyStorePass(), config.getTrustStorePath(), config.getTrustStorePass());
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
        HttpConnectionParams.setConnectionTimeout(params, config.getRetryIntervalTime()*1000);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpClient httpClient = new DefaultHttpClient((ClientConnectionManager)cm, params);
        return new ApacheHttpClient4Executor(httpClient);
    }

    private BmscEventType generateEvent(BmscEventTO event){
        BmscEventType bmscEvent = new BmscEventType();
        bmscEvent.setFileURI(event.getContentURI());
        bmscEvent.setDeliveryInstanceId(event.getDeliveryInstanceId());
        bmscEvent.setDeliverySessionId(event.getDeliverySessionId());
        bmscEvent.setDescription(event.getDescription());
        bmscEvent.setEMBMSSessionId(event.getEMBMSSessionId());
        bmscEvent.setNotificationType(String.valueOf(event.getNotificationType().getValue()));
        bmscEvent.setVersion(event.getVersion());
        bmscEvent.setVersion(BmscEventTO.DEFAULT_VERSION); // always is 1.0
        bmscEvent.setContentId(event.getContentGroupId());
        return bmscEvent;
    }
}
