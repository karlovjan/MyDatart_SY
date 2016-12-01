package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.builder.RouteBuilder;

public class ProcessGetDetailResponse extends RouteBuilder {

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		// TODO Auto-generated method stub
		from("switchyard://ProcessGetDetailResponseService")
				.log("Received message for 'ProcessGetDetailResponseService' : ${body}");
	}

}
