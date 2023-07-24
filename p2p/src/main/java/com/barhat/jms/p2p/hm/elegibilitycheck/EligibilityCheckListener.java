package com.barhat.jms.p2p.hm.elegibilitycheck;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.barhat.jms.p2p.hm.model.Patient;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext context = cf.createContext()) {
			
			InitialContext initialContext = new InitialContext();
			Queue reply = (Queue) initialContext.lookup("queue/replyQueue");
			MapMessage replyMessage = context.createMapMessage();
			replyMessage.setBoolean("eligible", false);
			
			Patient patient = (Patient) objectMessage.getObject();
			String insurance = patient.getInsuranceProvider();
			if(insurance.equals("Blue Cross Blue Shield") || insurance.equals("United Health")) {
				if(patient.getCopay() < 40 && patient.getAmountToBePayed() < 10000) {
					replyMessage.setBoolean("eligible", true);
				}
			}
			
			JMSProducer producer = context.createProducer();
			producer.send(reply, replyMessage);
		} catch (JMSException | NamingException e) {
			e.printStackTrace();
		} 
	}

}
