package com.bharath.jms.wellness;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.bharath.jms.hr.Employee;

public class WellnessApp {

	public static void main(String[] args) throws NamingException {
		InitialContext initialContext = new InitialContext();
		Topic topic = (Topic) initialContext.lookup("topic/empTopic");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext()){
			
			JMSConsumer consumer = context.createSharedConsumer(topic, "consumer-shared-1");
			JMSConsumer consumer2 = context.createSharedConsumer(topic, "consumer-shared-2");
			
			
			//Shared the message and consume them paralelly
			for(int i=1; i<=10; i+=2) {
				Message message = consumer.receive();
				Employee employee = message.getBody(Employee.class);
				System.out.println("Wellness-1: " + employee);
				
				Message message2 = consumer2.receive();
				Employee employee2 = message2.getBody(Employee.class);
				System.out.println("Wellness-2: " + employee2);
			}
			
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
