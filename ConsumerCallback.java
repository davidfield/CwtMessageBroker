package com.cwtdigital.messagebroker;

import java.util.concurrent.ThreadLocalRandom;

public class ConsumerCallback {
	
	private String name;
	
	public ConsumerCallback(String name) {
		this.name = name;
	}
	
	public void alertConsumer(String message, int runnableIstance) {
		System.out.println(String.format("Consumer:%s, Message:%s, Runnable:%d", name, message, runnableIstance));
		try {
			int randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1) *300;
			Thread.sleep(randomNum);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
