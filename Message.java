package com.cwtdigital.messagebroker;

public class Message {
	
	private String topic;
	private String content;
	
	public Message(String topic, String content) {
		super();
		this.topic = topic;
		this.content = content;
	}

	public String getTopic() {
		return topic;
	}

	public String getContent() {
		return content;
	}


}
