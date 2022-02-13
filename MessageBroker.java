package com.cwtdigital.messagebroker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class MessageBroker implements IMessageBroker {
	
	
	private Map<String, List<ConsumerCallback>> topics = new HashMap<>();
	private Queue<Message>[] queues = null;
	private int noOfQueues;
	
	public MessageBroker(int noOfQueues) {
		this.noOfQueues = noOfQueues;
		queues = new Queue[noOfQueues];
		for (int i=0; i<noOfQueues; i++) {
			Queue<Message> queue = new LinkedList<Message>(); 
			queues[i] = queue;
			QueueListener listener = new QueueListener(queue, i);
			Thread t = new Thread(listener);
			t.start();
		}
		
		
	}
	
	// create task executor with each thread listening to a separate queue
	

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
		int randomNum = ThreadLocalRandom.current().nextInt(0, noOfQueues);
		Queue<Message> queue = queues[randomNum];
		queue.add(message);
		
	}
	
	private class QueueListener implements Runnable {
		
		private Queue<Message> queue;
		private int runnablenstance;
		
		public QueueListener(Queue<Message> queue, int runnablenstance) {
			this.queue = queue;
			this.runnablenstance = runnablenstance;
		}

		@Override
		public void run() {
			while (true) {
				Message message = queue.poll();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (message!=null) {
					String content = message.getContent();
					for (ConsumerCallback callback : topics.get(message.getTopic())) {
						callback.alertConsumer(content, runnablenstance);
					}					
				}
			}
			
		}
		
	}

}
