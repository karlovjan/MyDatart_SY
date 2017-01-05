package cz.datart.jboss.myDatart.chunks.queueing;

public class QueueItem {

	private String queueName;
	private String textMessage;
	
	public QueueItem(String queueName, String message){
		this.queueName = queueName;
		this.textMessage = message;
	}

	public String getQueueName() {
		return queueName;
	}

//	public void setQueueName(String queueName) {
//		this.queueName = queueName;
//	}

	public String getTextMessage() {
		return textMessage;
	}

//	public void setTextMessage(String textMessage) {
//		this.textMessage = textMessage;
//	}
}
