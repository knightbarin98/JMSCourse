package com.bharath.jms.messages;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsMessagesMain {

	public static void main(String[] args) {
		
		InitialContext context = null;
		Queue queue = null;
		Queue exp = null;
		Queue request = null;
		Queue reply = null;
		
		try {
			context = new InitialContext();
			queue = (Queue) context.lookup("queue/myQueue");
			exp = (Queue) context.lookup("queue/expiryQueue");
			request = (Queue) context.lookup("queue/requestQueue");
			reply = (Queue) context.lookup("queue/replyQueue");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//runDemo(new MessagePriority(context, queue), "Message Priority");
		
		//runDemo(new RequestReplyDemo(context, reply, request), "Request reply");
		
		//runDemo(new RequestReplyTemporaryQueue(context, reply), "Request reply temporary queue");
		
		//runDemo(new RequestReplyCorrelation(context, reply, request), "Request reply correlation");
		
		//runDemo(new MessageExpDemo(context, queue), "Message exp");
		
		//runDemo(new MessageExpiryQueueDemo(context, queue, exp), "Message expiry queue");
		
		//runDemo(new MessageDelayDemo(context, queue), "Message delay");
		
		//runDemo(new MessageCustomPropertiesDemo(context, queue), "Message custom properties");
		
		runDemo(new MessageTypesDemo(context, queue), "Message types");
		
		
		
		
	}
	
	
	private static void runDemo(MainDemo demo, String type) {
		System.out.println("Start " + type);
		try {
			demo.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished " + type);
	}

}
