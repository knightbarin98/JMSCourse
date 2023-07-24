package com.barhat.jms.p2p.hm.clinicals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.barhat.jms.p2p.hm.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue request = (Queue) initialContext.lookup("queue/requestQueue");
		Queue reply = (Queue) initialContext.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext("clinicaluser","clinicalpass")){
			
			JMSProducer producer = context.createProducer();
			ObjectMessage objectMessage = context.createObjectMessage();
			Patient bob = new Patient("Bob","Blue Cross Blue Shield",30d,500d,1);
			objectMessage.setObject(bob);
			producer.send(request, objectMessage);
			
			JMSConsumer consumer = context.createConsumer(reply);
			MapMessage replyMessage = (MapMessage) consumer.receive(30000);
			System.out.println("Patient elegibility: " + replyMessage.getBoolean("eligible"));
		}

	}

}
