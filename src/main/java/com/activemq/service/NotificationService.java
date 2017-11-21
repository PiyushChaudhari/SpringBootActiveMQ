package com.activemq.service;

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
	public NotificationDeatils receive(String applicationId);
}
