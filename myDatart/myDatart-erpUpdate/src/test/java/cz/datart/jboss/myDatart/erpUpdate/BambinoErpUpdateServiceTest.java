package cz.datart.jboss.myDatart.erpUpdate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;
import org.switchyard.Exchange;
import org.switchyard.common.codec.Base64;

import cz.datart.jboss.myDatart.utils.FileUtils;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { HTTPMixIn.class, CDIMixIn.class })
public class BambinoErpUpdateServiceTest {
//SwitchYardTestCaseConfig.SWITCHYARD_XML
	
	private static final String ERP_UPDATE_WEB_SERVICE = "http://localhost:8081/testcz/erpUpdate/BambinoErpUpdateService";
	
	private static final String ERP_UPDATE_WEB_SERVICE_SSL = "https://localhost:8443/testcz/erpUpdate/BambinoErpUpdateService";
	
	private HTTPMixIn httpMixIn;
//	protected CDIMixIn _cdimixin;
//	
	@ServiceOperation("BambinoErpUpdateService")
	private Invoker erpUpdateService;

	private SwitchYardTestKit testKit;
	
//	@ApplicationProperty(name="myDatart.erp.update.context.path")
//	private String contextPath;
	
	@BeforeDeploy
    public void setProperties() throws KeyManagementException, NoSuchAlgorithmException {
//        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
        System.setProperty("org.switchyard.component.soap.client.port", "8443");
		System.setProperty("org.switchyard.component.soap.standalone.port", "8443");
//        System.setProperty("javax.net.ssl.trustStore", "D:\\Projects\\Datart\\JBoss\\GIT_Repo\\MyDatart_SY\\myDatart\\myDatart-erpUpdate\\src\\test\\resources");
//        System.setProperty("javax.net.ssl.trustStorePassword", "test1234");
//        
		System.setProperty("cxf.config.file", "src/test/resources/cxf.xml");
		
        
//        try {
//			registerSSLConnection();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        System.setProperty("java.security.auth.login.config", "D:\\Projects\\Datart\\JBoss\\GIT_Repo\\MyDatart_SY\\myDatart\\myDatart-erpUpdate\\src\\test\\resources\\jaas.conf");
        System.setProperty("java.security.auth.login.config", "src/test/resources/jaas.conf");
    }
	
	protected static void registerSSLConnection() throws Exception {
		
    	/*
		KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("src/test/resources/keystore.jks"), "test1234".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, "test1234".toCharArray());

        sslcontext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
*/
//        SSLSocketFactory sf = context.getSocketFactory();
        
    	SSLContext sslcontext = SSLContext.getInstance("TLS");
//    	sslcontext.init(null, null, null);
        sslcontext.init(getKeyManagers(), getTrustManagers(), new SecureRandom());
        
//    	HttpClientBuilder builder = HttpClientBuilder.create();
//    	SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext);
//    	builder.setSSLSocketFactory(sslConnectionFactory);
        

    	RegistryBuilder.<SSLSocketFactory>create()
    	        .register("https", sslcontext.getSocketFactory())
    	        .build();

//    	HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
//
//    	builder.setConnectionManager(ccm);
//
//    	return builder.build();
    	
    	HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
               
                if(hostname.equals("localhost")){
                    return true;
                }
                return false;
            }
        });
    }

//	@Before
//  public void startAxaptaWebService() throws Exception {
//      _endpoint = Endpoint.publish(ERP_UPDATE_WEB_SERVICE_SSL, new ReverseService());
//  }

	public static KeyStore loadTrustStore() throws Exception {
	    KeyStore trustStore = KeyStore.getInstance("jks");
	    trustStore.load(new FileInputStream(new File("src/test/resources/keystore.jks")), "test1234".toCharArray());
	    return trustStore;
	}

	protected static TrustManager[] getTrustManagers() throws Exception {
	    KeyStore trustStore = loadTrustStore();
	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    tmf.init(trustStore);
	    return tmf.getTrustManagers();
	}

	// Key managers are only required for two-way SSL, or client certificate authentication.
	public static KeyStore loadKeyStore() throws Exception {
	    KeyStore keyStore = KeyStore.getInstance("jks");
	    keyStore.load(new FileInputStream(new File("src/test/resources/keystore.jks")), "test1234".toCharArray());
	    return keyStore;
	}

	// Key managers are only required for two-way SSL, or client certificate authentication.
	protected static KeyManager[] getKeyManagers() throws Exception {
	    KeyStore keyStore = loadKeyStore();
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	    kmf.init(keyStore, "test1234".toCharArray());
	    return kmf.getKeyManagers();
	}
	
	@Test
	public void testUpdateUserInvoker() throws IOException {
		 
		
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/receiveDetailCust-response.xml"));
		String axaRequest = FileUtils.readFile(testKit.getResourceAsStream("/soaps/updateUser-request.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		String response = erpUpdateService.operation("updateUser").sendInOut(axaRequest).getContent(String.class);
		
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateUser-request.xml", "/soaps/updateUser-response.xml");
//		Assert.assertNotNull(response);
	}
	
//	@Test
    public void sendTextMessageThroughTcp() throws Exception {
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/receiveDetailCust-response.xml"));
		String axaRequest = FileUtils.readFile(testKit.getResourceAsStream("/soaps/updateUser-request.xml"));
		
        // replace existing implementation for testing purposes
		testKit.removeService("AxaptaWS");
        final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
        mockHandler.replyWithOut(axaResponse);

        

//        String response = erpUpdateService.operation("updateUser").sendInOut(axaRequest).getContent(String.class);
		
        httpMixIn.setContentType("application/soap+xml");
        httpMixIn.setRequestHeader("Authorization", "Basic " + Base64.encodeFromString("baros" + ":" + "Micuda"));
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE_SSL, "/soaps/updateUser-request.xml", "/soaps/updateUser-response.xml");
		
        /*
        Socket clientSocket = sf.createSocket("localhost", 8181);
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
        // lets write payload directly as bytes to avoid encoding mismatches
        outputStream.write(axaRequest.getBytes());
        outputStream.flush();

        // sleep a bit to receive message on camel side
        Thread.sleep(50);
        clientSocket.close();

        mockHandler.waitForOKMessage();
        final LinkedBlockingQueue<Exchange> recievedMessages = mockHandler.getMessages();
//        assertThat(recievedMessages, is(notNullValue()));
        final Exchange recievedExchange = recievedMessages.iterator().next();
//        assertThat(PAYLOAD, is(equalTo(recievedExchange.getMessage().getContent(String.class))));
        */
      
    } 
	
}
