package com.bharath.jms.security;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hr.Employee;

public class SecurityApp {

	public static void main(String[] args) throws NamingException {
		InitialContext initialContext = new InitialContext();
		Topic topic = (Topic) initialContext.lookup("topic/empTopic");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext()){
			
			context.setClientID("securityApp");
			JMSConsumer consumer = context.createDurableConsumer(topic, "subscription1");
			consumer.close();
			
			Thread.sleep(10000);
			
			consumer = context.createDurableConsumer(topic, "subscription1");
			Message message = consumer.receive();
			Employee employee = message.getBody(Employee.class);
			System.out.println("Security: " + employee);
			
			consumer.close();
			context.unsubscribe("subscription");
			
		} catch (JMSException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
