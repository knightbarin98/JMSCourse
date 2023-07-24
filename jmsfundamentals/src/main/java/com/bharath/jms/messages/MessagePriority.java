package com.bharath.jms.messages;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePriority implements MainDemo{
	InitialContext context;
	Queue queue;
	
	public MessagePriority(InitialContext context, Queue queue)  {
		this.context = context;
		this.queue = queue;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			
			String [] messages = new String[3];
			
			messages[0] = "Message One";
			messages[1] = "Message Two";
			messages[2] = "Message Three";
			
			producer.setPriority(3);
			producer.send(queue,messages[0]);
			
			producer.setPriority(1);
			producer.send(queue,messages[1]);
			
			producer.setPriority(9);
			producer.send(queue,messages[2]);
					
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			
			for(int i = 0; i < 3; i++) {
				Message received = consumer.receive();
				System.out.println(received.getJMSPriority());
				//System.out.println(consumer.receiveBody(String.class));
			}
		}
	}
}
