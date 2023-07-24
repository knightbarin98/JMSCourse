package com.bharath.jms.claimmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ClaimManagementApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue claim = (Queue) initialContext.lookup("queue/claimQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext()){
			
			JMSProducer producer = context.createProducer();
			//JMSConsumer consumer = context.createConsumer(claim, "claimAmount BETWEEN 1000 AND 5000");
			//JMSConsumer consumer = context.createConsumer(claim, "doctorName LIKE 'J%'");
			
			//FALSE BECAUSE DEFAULT PRIORITY IS 4
			JMSConsumer consumer = context.createConsumer(claim, "doctorType in ('general','neuro') OR JMSPriority BETWEEN 5 AND 9 ");
			
			ObjectMessage objectMessage = context.createObjectMessage();
			//PROPERTIES TO USE IN FILTERS
			
			//objectMessage.setIntProperty("hospitalId", 1);
			//objectMessage.setDoubleProperty("claimAmount", 1000);
			//objectMessage.setStringProperty("doctorName", "John");
			objectMessage.setStringProperty("doctorType", "oncologist");
			Claim claimMessage = new Claim(1,"John","oncologyst","Blue cross",1000);
			objectMessage.setObject(claimMessage);
			
			producer.send(claim, objectMessage);
			
			Message receive = consumer.receive();
			Claim receiveClaim = receive.getBody(Claim.class);
			System.out.println("Consuming message: " + receiveClaim);
		}

	}

}
