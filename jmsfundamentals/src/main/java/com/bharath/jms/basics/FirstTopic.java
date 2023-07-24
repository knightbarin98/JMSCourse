package com.bharath.jms.basics;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {
	private InitialContext initialContext;
	private ConnectionFactory cfy;
	private Connection connection;
	
	public FirstTopic() throws NamingException, JMSException {
		initialContext = new InitialContext();
		cfy = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		connection = cfy.createConnection();
	}
	
	public void runTopic() throws JMSException, NamingException {
		Topic topic = (Topic) initialContext.lookup("topic/myTopic");
		Session session = connection.createSession();
		MessageProducer producer = session.createProducer(topic);
		
		ArrayList<MessageConsumer> consumers = new ArrayList<>();
		for(int i = 0; i < 3; i++) {
			MessageConsumer consumer = session.createConsumer(topic);
			consumers.add(consumer);
		}
		TextMessage messageSend = (TextMessage) session.createTextMessage("All the power is with in me, I can do anythin and everything");
		producer.send(messageSend);
		
		connection.start();
		int cont = 1;
		for(MessageConsumer consumer: consumers) {
			TextMessage message = (TextMessage) consumer.receive();
			System.out.println("Consumer "+(cont++)+" message received : " + message.getText());
		}
		
	}
	
	public void stop() {
		if(initialContext != null) {
			try {
				initialContext.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
