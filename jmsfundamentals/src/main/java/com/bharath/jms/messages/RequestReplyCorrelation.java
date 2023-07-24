package com.bharath.jms.messages;

import java.util.HashMap;
import java.util.Map;

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

public class RequestReplyCorrelation implements MainDemo{
	InitialContext context;
	Queue request;
	Queue reply;
	
	public RequestReplyCorrelation(InitialContext context, Queue request, Queue reply)  {
		this.context = context;
		this.request = request;
		this.reply = reply;
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
			System.out.println(message.getJMSMessageID());
			
			Map<String, TextMessage> requestMessage = new HashMap<>();
			requestMessage.put(message.getJMSMessageID(), message);
			
			
			JMSConsumer consumer = jmsContext.createConsumer(request);
			TextMessage received = (TextMessage) consumer.receive();
			//String receiveBody = consumer.receiveBody(String.class);
			System.out.println("Message received : " + received.getText());
			
			
			JMSProducer replyProducer = jmsContext.createProducer();
			//AVOID HARCODING REPLY QUEUE
			TextMessage replyMessage = jmsContext.createTextMessage("You are awesome");
			replyMessage.setJMSCorrelationID(received.getJMSMessageID());
			replyProducer.send(received.getJMSReplyTo(), replyMessage);
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyTemporaryQueue);
			//String messageReceived = consumer.receiveBody(String.class);
			TextMessage receivedReply = (TextMessage) replyConsumer.receive();
			//String receiveBody = consumer.receiveBody(String.class);
			System.out.println("Message received : " + receivedReply.getText());
			System.out.println("Message correlation ID : " + receivedReply.getJMSCorrelationID());
			System.out.println("Message request correlation ID: " + requestMessage.get(receivedReply.getJMSCorrelationID()).getJMSMessageID());
		}
	}
}
