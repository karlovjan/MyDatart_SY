package cz.datart.jboss.myDatart.chunks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.transaction.TransactionMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import cz.datart.jboss.myDatart.utils.ChunkUtils;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class, HornetQMixIn.class, TransactionMixIn.class })
public class SendChunkToEshopServiceTest {

	private SwitchYardTestKit testKit;
	
	@ServiceOperation("SendChunkToEshopService")
	private Invoker service;

	@BeforeDeploy
    public void setProperties() {
		
        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
//        System.setProperty("axa.ws.endpoint", "http://localhost:8082/axaptaws");
//        System.setProperty("eshop.update.endpoint", "http://localhost:28080/eShopUpdate");
        
//        _hqMixIn = testKit.getMixIn(HornetQMixIn.class);
        
    }
	
	@Test
	public void testSendProducerChunk() throws Exception {
		
		List<String> eList = new ArrayList<>();
		
		eList.add("Item1");
		eList.add("Item2");
		eList.add("Item3");
		
		ChunkUtils utils = new ChunkUtils();
		String oldVersion = "1";
		String newVersion = utils.getNewVersion(oldVersion, eList.size());
		
		String message = utils.createSoapMessage("Producers", utils.createListXML(eList), "CZ", "http://etnetera.com/projects/datart/bambino", oldVersion, newVersion);
		
		String replyMessage = "<updateProducersResponse/>"; //nesmi tam byt namespace, jinak test neprojde
		
		testKit.removeService("EshopWS");
		final MockHandler mockedEshopWSServiceHandler = testKit.registerInOutService("EshopWS");
		mockedEshopWSServiceHandler.replyWithOut(replyMessage); //<bam:updateProducersResponse/>
		
		testKit.removeService("AxaptaWS");
		final MockHandler mockedAxaptaWSServiceHandler = testKit.registerInOutService("AxaptaWS");
		mockedAxaptaWSServiceHandler.replyWithOut("<ConfirmTransResponse/>"); 
		
		String response = service.operation("send").sendInOut(message).getContent(String.class);

		// validate the results
		Assert.assertNotNull(response);
	}

}
