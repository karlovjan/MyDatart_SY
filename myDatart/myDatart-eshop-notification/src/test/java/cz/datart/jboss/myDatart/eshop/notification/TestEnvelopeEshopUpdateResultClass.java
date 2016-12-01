package cz.datart.jboss.myDatart.eshop.notification;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import cz.datart.jboss.myDatart.eshop.notification.EnvelopeEshopUpdateResult;

public class TestEnvelopeEshopUpdateResultClass {

	private static EnvelopeEshopUpdateResult testingClass;
	
	@BeforeClass
	public static void setUp(){
		testingClass = new EnvelopeEshopUpdateResult();
		
	}
	
	@Test
	public void testGetResult() {
		String expected = "Ahoj";
		String body = "<test><SendDetailOrderResponse><SendDetailOrderResult>"+expected+"</SendDetailOrderResult></SendDetailOrderResponse></test>";
		
		String result = testingClass.getResult(body);
		
		assertEquals(expected, result);
	}

}
