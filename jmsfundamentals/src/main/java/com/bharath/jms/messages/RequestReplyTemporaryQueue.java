package com.bharath.jms.messages;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyTemporaryQueue implements MainDemo{
	InitialContext context;
	Queue request;
	
	public RequestReplyTemporaryQueue(InitialContext context, Queue request)  {
		this.context = context;
		this.request = request;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			//create a queue programmatically
			JMSProducer producer = jmsContext.createProducer();
			TemporaryQueue replyTemporaryQueue = jmsContext.createTemporaryQueue();
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			message.setJMSReplyTo(replyTemporaryQueue);
			producer.send(request, message);
			
			JMSConsumer consumer = jmsContext.createConsumer(request);
			TextMessage received = (TextMessage) consumer.receive();
			//String receiveBody = consumer.receiveBody(String.class);
			System.out.println("Message received : " + received.getText());
			
			
			JMSProducer replyProducer = jmsContext.createProducer();
			//AVOID HARCODING REPLY QUEUE
			replyProducer.send(received.getJMSReplyTo(), "You are awesome");
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyTemporaryQueue);
			String messageReceived = replyConsumer.receiveBody(String.class);
			System.out.println(messageReceived);
		}
	}
}
