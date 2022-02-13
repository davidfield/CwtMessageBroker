package com.cwtdigital.messagebroker;

public class MessageBrokerTest {
	
	public static void main(String[] args) {
		IMessageBroker broker = new MessageBroker(6);
		ConsumerCallback callback1 = new ConsumerCallback("one");
		ConsumerCallback callback2 = new ConsumerCallback("two");
		ConsumerCallback callback3 = new ConsumerCallback("three");
		
		broker.register("A",  callback1);
		
		broker.register("B",  callback2);
		broker.register("C",  callback2);
		
		broker.register("A",  callback3);
		broker.register("B",  callback3);
		broker.register("C",  callback3);

		for (int i = 0; i<5; i++) {
			broker.send(new Message("A", String.valueOf(i)));
			broker.send(new Message("B", String.valueOf(i)));
			broker.send(new Message("C", String.valueOf(i)));
		}


	}

}
