package com.bharath.jms.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JMSContextDemo {
	InitialContext context;
	Queue queue;
	
	public JMSContextDemo() throws NamingException {
		context = new InitialContext();
		queue = (Queue) context.lookup("queue/myQueue");
	}
	
	public void run() {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()){
			JMSContext jmsContext = cf.createContext();
			
			jmsContext.createProducer().send(queue, "Arise awake and stop not till the goal is reached");
			
			String receiveBody = jmsContext.createConsumer(queue).receiveBody(String.class);
			
			System.out.println("Message received : " + receiveBody);
		}
	}
}
