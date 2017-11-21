package com.activemq.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.activemq.model.NotificationDeatils;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void send(NotificationDeatils notificationDeatils) {
		logger.info("Notification Before getApplicationId:>> " + notificationDeatils.getApplicationId());
		logger.info("Notification Before getCustomerId:>> " + notificationDeatils.getCustomerId());
		jmsTemplate.convertAndSend(notificationDeatils.getApplicationId(), notificationDeatils);
		logger.info("Notification Send:>> " + notificationDeatils.getApplicationId());
	}

	@Override
	public NotificationDeatils receive(String applicationId) {
		logger.log(Level.INFO, "Notification Before applicationId:>> %s ", applicationId);
		
		Object obj = jmsTemplate.receiveAndConvert(applicationId);
		NotificationDeatils nd = null;
		if(obj!=null)
			nd = (NotificationDeatils)obj;
		logger.info("Notification Data:>> "+nd);
		return nd;
	}

}
