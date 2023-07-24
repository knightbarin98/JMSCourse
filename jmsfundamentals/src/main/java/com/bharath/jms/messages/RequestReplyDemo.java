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

public class RequestReplyDemo implements MainDemo{
	InitialContext context;
	Queue request;
	Queue reply;
	
	public RequestReplyDemo(InitialContext context, Queue request, Queue reply)  {
		this.context = context;
		this.request = request;
		this.reply = reply;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			message.setJMSReplyTo(reply);
			producer.send(request, message);
			
			JMSConsumer consumer = jmsContext.createConsumer(request);
			TextMessage received = (TextMessage) consumer.receive();
			//String receiveBody = consumer.receiveBody(String.class);
			System.out.println("Message received : " + received.getText());
			
			
			JMSProducer replyProducer = jmsContext.createProducer();
			//AVOID HARCODING REPLY QUEUE
			replyProducer.send(received.getJMSReplyTo(), "You are awesome");
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(reply);
			String messageReceived = replyConsumer.receiveBody(String.class);
			System.out.println(messageReceived);
		}
	}
}
