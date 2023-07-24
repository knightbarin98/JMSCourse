package com.bharath.jms.grouping;

import java.util.ArrayList;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageGroupingDemo implements MainDemo{

	InitialContext context;
	Queue queue;
	
	public MessageGroupingDemo(InitialContext context, Queue queue)  {
		this.context = context;
		this.queue = queue;
	}

	@Override
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
				
				JMSProducer producer = jmsContext.createProducer();
				
				ArrayList<TextMessage> messages = new ArrayList<>();
				for(int i = 1; i<=10; i++) {
					TextMessage tmp = jmsContext.createTextMessage("Group-0 message"+i);
					tmp.setStringProperty("JMSXGroupID", "Group-0");
					messages.add(tmp);
					producer.send(queue, tmp);
				}
			}
		
	}

}
