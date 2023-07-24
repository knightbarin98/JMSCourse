package com.bharath.jms.guaranteedmessaging.transactions;

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
				JMSContext context = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

			JMSConsumer consumer = context.createConsumer(request);
			TextMessage message = (TextMessage) consumer.receive();
			
			//If we do not commit the receive. server will keep sending them
			
			System.out.println(message.getText());
			context.commit();
			//context.rollback();
		}

	}

}
