package com.cwtdigital.messagebroker;

public interface IMessageBroker {
	
	void register(String topic, ConsumerCallback consumer);
	void send(Message message);

}
