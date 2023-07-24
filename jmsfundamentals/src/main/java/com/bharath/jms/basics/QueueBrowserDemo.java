package com.bharath.jms.basics;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueBrowserDemo {
	private InitialContext initialContext;
	private ConnectionFactory cfy;
	private Connection connection;
	
	public QueueBrowserDemo() throws NamingException, JMSException {
		initialContext = new InitialContext();
		cfy = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		connection = cfy.createConnection();
	}
	
	public void runQueue() throws JMSException, NamingException {
		Session session = connection.createSession();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		MessageProducer producer = session.createProducer(queue);
		
		ArrayList<TextMessage> messages = new ArrayList<>();
		for(int i = 0; i < 2; i++) {
			TextMessage message = session.createTextMessage("Message " + (i+1));
			producer.send(message);
			System.out.println("Message sent: "+message.getText());
		}
		
		QueueBrowser browser = session.createBrowser(queue);
		Enumeration enumeration = browser.getEnumeration();
		
		while(enumeration.hasMoreElements()) {
			TextMessage eachMessage = (TextMessage) enumeration.nextElement();
			System.out.println("Browsing : "+eachMessage.getText());
		}
		
		
		MessageConsumer consumer = session.createConsumer(queue);
		connection.start();
		
		for(int i=0;i < messages.size();i++) {
			TextMessage messageReceived = (TextMessage) consumer.receive(5000);
			System.out.println("Message Received: "+messageReceived.getText());
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
