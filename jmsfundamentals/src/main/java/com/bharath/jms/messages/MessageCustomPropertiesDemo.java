package com.bharath.jms.messages;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageCustomPropertiesDemo implements MainDemo{
	InitialContext context;
	Queue queue;
	
	public MessageCustomPropertiesDemo(InitialContext context, Queue queue)  {
		this.context = context;
		this.queue = queue;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			message.setBooleanProperty("loggedin", true);
			message.setStringProperty("token", "abc123");
			producer.send(queue, message);
			
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println(messageReceived);
			System.out.println(message.getBooleanProperty("loggedin"));
			System.out.println(message.getStringProperty("token"));
		}
	}
}
