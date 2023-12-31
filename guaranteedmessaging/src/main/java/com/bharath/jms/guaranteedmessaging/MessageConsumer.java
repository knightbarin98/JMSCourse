package com.bharath.jms.guaranteedmessaging;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageConsumer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue request = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); 
				JMSContext context = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {

			JMSConsumer consumer = context.createConsumer(request);
			TextMessage message = (TextMessage) consumer.receive();

			System.out.println(message.getText());
			message.acknowledge();
		}

	}

}
