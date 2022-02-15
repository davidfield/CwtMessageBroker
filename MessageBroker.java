package com.cwtdigital.messagebroker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageBroker implements IMessageBroker {
	
	
	private Map<String, List<ConsumerCallback>> topics = new HashMap<>();
	private Queue<Message>[] queues = null;
	private int noOfQueues;
	private static int queueToUse;
	private ThreadPoolExecutor executor = 
			  (ThreadPoolExecutor) Executors.newCachedThreadPool();
	
	@SuppressWarnings("unchecked")
	public MessageBroker(int noOfQueues) {
		this.noOfQueues = noOfQueues;
		queues = new Queue[noOfQueues];
		for (int i=0; i<noOfQueues; i++) {
			Queue<Message> queue = new LinkedList<Message>(); 
			queues[i] = queue;
			
			// get a thread from the pool for each queue
			executor.submit(() -> {
				while (true) {
					Message message = queue.poll();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (message!=null) {
						String content = message.getContent();
						for (ConsumerCallback callback : topics.get(message.getTopic())) {
							callback.alertConsumer(content, Thread.currentThread().getName());
						}					
					}
				}
			});
		}	
		
	}
	

	@Override
	public void register(String topic, ConsumerCallback consumer) {
		List<ConsumerCallback> consumers = topics.get(topic);
		if (consumers==null) {
			consumers = new ArrayList<ConsumerCallback>();
			consumers.add(consumer);
			topics.put(topic, consumers);
		} else {
			consumers.add(consumer);
		}
	}

	@Override
	public void send(Message message) {
		// get one of the queues, put message on
		Queue<Message> queue = queues[queueToUse++];
		queueToUse = queueToUse<noOfQueues?queueToUse:0;
		queue.add(message);
	}
	

}
