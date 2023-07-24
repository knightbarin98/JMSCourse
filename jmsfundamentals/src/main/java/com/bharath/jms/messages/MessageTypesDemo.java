package com.bharath.jms.messages;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo implements MainDemo{
	InitialContext context;
	Queue queue;
	
	public MessageTypesDemo(InitialContext context, Queue queue)  {
		this.context = context;
		this.queue = queue;
	}
	
	public void run() throws Exception {
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
			JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached");
			
			//ByteMessage
			BytesMessage bytesMessage = jmsContext.createBytesMessage();
			bytesMessage.writeUTF("Joh");
			bytesMessage.writeLong(123);
			
			//StreamMessage
			StreamMessage stream = jmsContext.createStreamMessage();
			stream.writeBoolean(true);
			stream.writeLong(123);
			
			//MapMessage
			MapMessage map = jmsContext.createMapMessage();
			map.setBoolean("boolean", false);
			
			//ObjectMessage
			ObjectMessage object = jmsContext.createObjectMessage();
			object.setObject(new Patient(123,"John"));
			
			
			producer.send(queue, object);
			
			//BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println(messageReceived.readUTF());
			//System.out.println(messageReceived.readLong());
			
			//StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println(messageReceived.readBoolean());
			//System.out.println(messageReceived.readLong());
			
			//MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
			//System.out.println(messageReceived.getBoolean("boolean"));
			
			ObjectMessage messageReceived = (ObjectMessage) jmsContext.createConsumer(queue).receive(5000);
			System.out.println((Patient)messageReceived.getObject());
		}
	}
}
