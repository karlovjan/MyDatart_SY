package cz.datart.jboss.myDatart.eshop.notification;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.switchyard.Exchange;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.HandlerException;

/**
 * This is an example of an exchange interceptor which can be used to inject code
 * around targets during a message exchange.  This example updates the content of 
 * OrderAck after the provider has generated a response.
 * Vola se automaticky
 */
//@Named("AxaptaWSInterceptor") 
public class AxaptaWSInterceptor implements ExchangeInterceptor {

//	@Inject
	private Logger log;
	
	@Override
	public void after(String target, Exchange exchange) throws HandlerException {
		// Not interested in doing anything before the provider is invoked 
	}

	@Override
	public void before(String target, Exchange exchange) throws HandlerException {
		
		// We only want to intercept successful replies from Axapta ws
        if (exchange.getProvider().getName().getLocalPart().equals("AxaptaWS")) {
        	
        	//&& ExchangeState.OK.equals(exchange.getState())
        	
//            OrderAck orderAck = exchange.getMessage().getContent(OrderAck.class);
           
        	log.info(String.format("AxaptaWS intercepted... serviceResponseState: %s, Accept-Encoding: %s, Content-Encoding: %s", exchange.getState().toString(), exchange.getContext().getPropertyValue("Accept-Encoding"), exchange.getContext().getPropertyValue("Content-Encoding")));
        	
        } 
	}

	@Override
	public List<String> getTargets() {
		
		return Arrays.asList(PROVIDER);
	}

}
