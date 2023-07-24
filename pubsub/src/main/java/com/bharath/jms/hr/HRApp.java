package com.bharath.jms.hr;

import java.util.Iterator;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class HRApp {

	public static void main(String[] args) throws NamingException {
		InitialContext initialContext = new InitialContext();
		Topic topic = (Topic) initialContext.lookup("topic/empTopic");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext()){
			Employee employee = new Employee(
					123,
					"Ivan",
					"Lopez",
					"lopezsotoivan01@gmail.com",
					"Software Engineer",
					"12345"
					);
			
			for(int i = 1;i<=10; i++) {
				context.createProducer().send(topic, employee);
			}
			
			System.out.println("Message sent");
		}

	}

}
