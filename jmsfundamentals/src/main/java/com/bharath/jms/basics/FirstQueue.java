package com.bharath.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {

	private InitialContext initialContext;
	private ConnectionFactory cfy;
	private Connection connection;
	
	public FirstQueue() throws NamingException, JMSException {
		initialContext = new InitialContext();
		cfy = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		connection = cfy.createConnection();
	}
	
	public void runQueue() throws JMSException, NamingException {
		Session session = connection.createSession();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		MessageProducer producer = session.createProducer(queue);
		TextMessage message = session.createTextMessage("I am the creator of my destiny");
		producer.send(message);
		System.out.println("Message sent: "+message.getText());
		
		MessageConsumer consumer = session.createConsumer(queue);
		connection.start();
		TextMessage messageReceived = (TextMessage) consumer.receive(5000);
		System.out.println("Message Received: "+messageReceived.getText());
		
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
