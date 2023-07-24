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

public class MessageExpiryQueueDemo implements MainDemo{
	InitialContext context;
	Queue queue;
	Queue exp;
	
	public MessageExpiryQueueDemo(InitialContext context, Queue queue, Queue exp)  {
		this.context = context;
		this.queue = queue;
		this.exp = exp;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			producer.setTimeToLive(2000); //milliseconds
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			producer.send(queue, message);
			Thread.sleep(5000);
			
			String messageReceived = jmsContext.createConsumer(queue).receive(5000).getBody(String.class);
			System.out.println(messageReceived);
			
			System.out.println(jmsContext.createConsumer(exp).receiveBody(String.class));
		}
	}
}
