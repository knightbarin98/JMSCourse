package com.bharath.jms.messages;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpDemo implements MainDemo {
	InitialContext context;
	Queue queue;
	
	public MessageExpDemo(InitialContext context, Queue queue)  {
		this.context = context;
		this.queue = queue;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			//producer.setTimeToLive(2000); //milliseconds
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			producer.send(queue, message);
			Thread.sleep(5000);
			
			String messageReceived = jmsContext.createConsumer(queue).receive(5000).getBody(String.class);
			System.out.println(messageReceived);
		}
	}
}
