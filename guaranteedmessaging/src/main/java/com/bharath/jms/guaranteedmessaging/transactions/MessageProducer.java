package com.bharath.jms.guaranteedmessaging.transactions;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageProducer {
	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue request = (Queue) initialContext.lookup("queue/requestQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext(JMSContext.SESSION_TRANSACTED)){
			
			JMSProducer producer = context.createProducer();
			producer.send(request, "Message 1");
			producer.send(request, "Message 2");
			context.commit();
			//Rollback next message and all until last commit
			producer.send(request, "Message 3");
			producer.send(request, "Message 4");
			context.rollback();
		}

	}
}
