package com.bharath.jms.basics;

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.bharath.jms.messages.MessagePriority;

public class JmsMain {

	public static void main(String[] args) {
		
		try {
			System.out.println("Run Queue");
			FirstQueue firstQueue = new FirstQueue();
			firstQueue.runQueue();
			firstQueue.stop();
			System.out.println("Finished queue");
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("Run Topic");
			FirstTopic firstTopic = new FirstTopic();
			firstTopic.runTopic();
			firstTopic.stop();
			System.out.println("Finished topic");
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			System.out.println("Run Browser");
			QueueBrowserDemo browser = new QueueBrowserDemo();
			browser.runQueue();
			browser.stop();
			System.out.println("Finished browser");
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("Run JMSContext");
			JMSContextDemo jms = new JMSContextDemo();
			jms.run();
			System.out.println("Finished JMSContext");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
