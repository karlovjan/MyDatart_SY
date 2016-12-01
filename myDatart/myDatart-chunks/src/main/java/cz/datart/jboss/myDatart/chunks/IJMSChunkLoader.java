package cz.datart.jboss.myDatart.chunks;

import java.util.List;

public interface IJMSChunkLoader {

	/**
     * Load messages from destination within specified session.
     *
     * @param count Count of messages to be loaded from destination. When
     *              is less then zero then one message is loaded from destination.
	 * @param thisJobName The name of the chunk group
	 * @param queueName The name of the queue
     * @return Array of loaded messages. In array could be an null elements. All elements
     *         after first null element will be null too.
     */
	public List<String> loadMessages(int count, String thisJobName, String queueName);
}
