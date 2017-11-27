package com.activemq.service;

import java.util.List;

import com.activemq.model.NotificationDeatils;

public interface NotificationService {

	/**
	 * 
	 * @param notificationDeatils
	 */
	public void send(NotificationDeatils notificationDeatils);

	/**
	 * 
	 * @param applicationId
	 * @return
	 */
	public List<NotificationDeatils> receive(String applicationId);
}
