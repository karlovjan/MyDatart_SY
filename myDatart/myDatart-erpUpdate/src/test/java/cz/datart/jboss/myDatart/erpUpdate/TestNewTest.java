package cz.datart.jboss.myDatart.erpUpdate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import cz.datart.jboss.myDatart.utils.ApplicationProperty;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class,
		HTTPMixIn.class })
public class TestNewTest {

	private SwitchYardTestKit testKit;
	private CDIMixIn cdiMixIn;
	private HTTPMixIn httpMixIn;
	@ServiceOperation("BambinoErpUpdateService")
	private Invoker service;


//	@ApplicationProperty(name="myDatart.erp.update.context.path")
//	private String contextPath;
//	
	@BeforeDeploy
    public void setProperties() {
        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
    }
	
//	@Test
//	public void testUpdateOrder() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("updateOrder").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
	
	@Test
	public void testUpdateUser() throws Exception {
		
		String message = "<bam:updateUser  xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"><segment>CZ</segment><user deleted=\"false\"><id>ESH-478478</id><businessRelationship>B2C</businessRelationship></user></bam:updateUser>";
		String result = service.property("org.switchyard.soap.messageName", "updateUserRequest").operation("updateUser").sendInOut(message)
				.getContent(String.class);

		// validate the results <bam:updateUserResponse/>
		Assert.assertTrue("<bam:updateUserResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"/>".equals(result));
	}

//	@Test
//	public void testUpdateUser() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("updateUser").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
//
//	@Test
//	public void testChangeComplaintState() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("changeComplaintState").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
//
//	@Test
//	public void testUpdateExpressOrder() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("updateExpressOrder").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
//
//	@Test
//	public void testUpdateComplaint() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("updateComplaint").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
//
//	@Test
//	public void testChangeOrderOwner() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("changeOrderOwner").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}
//
//	@Test
//	public void testUpdateNeeded() throws Exception {
//		// TODO Auto-generated method stub
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("updateNeeded").sendInOut(message).getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}

}
