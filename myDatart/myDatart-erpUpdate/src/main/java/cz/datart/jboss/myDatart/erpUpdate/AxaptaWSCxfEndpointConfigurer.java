package cz.datart.jboss.myDatart.erpUpdate;

import javax.inject.Named;

import org.apache.camel.component.cxf.CxfEndpointConfigurer;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.log4j.Logger;


@Named("myAxaptaWSCxfEndpointConfigurer")
public class AxaptaWSCxfEndpointConfigurer implements CxfEndpointConfigurer {

	private static final Logger log = Logger.getLogger(AxaptaWSCxfEndpointConfigurer.class);
	
	@Override
	public void configure(AbstractWSDLBasedEndpointFactory factoryBean) {
		
		factoryBean.getInInterceptors().add(new GZIPInInterceptor()); //in from server side, transform server response!
        for (Interceptor<? extends Message> interceptor : factoryBean.getInInterceptors()) {
            log.info(String.format(">>>>> InInterceptor [%s]", interceptor.getClass().getName()));
        }
        
//		factoryBean.getOutInterceptors().add(new GZIPOutInterceptor());// out is from outgoing from client side
//        for (Interceptor<? extends Message> interceptor : factoryBean.getOutInterceptors()) {
//            log.info(String.format(">>>>> OutInterceptor [%s]", interceptor.getClass().getName()));
//        }
	}

	@Override
	public void configureClient(Client arg0) {
		log.info("Configure cxf client...");
	}

	@Override
	public void configureServer(Server arg0) {
		log.info("Configure cxf server...");
	}

}
