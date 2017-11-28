package com.activemq.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.activemq.model.NotificationDeatils;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public List<NotificationDeatils> receive(String applicationId) {
		logger.log(Level.INFO, () -> "Notification Before applicationId:>> " + applicationId);
		List<NotificationDeatils> list = new ArrayList<>();

		jmsTemplate.browse(new ActiveMQQueue(applicationId), new BrowserCallback<Integer>() {

			Integer count = 0;

			@Override
			public Integer doInJms(Session session, QueueBrowser browser) throws JMSException {

				@SuppressWarnings("unchecked")
				Enumeration<ActiveMQTextMessage> e = browser.getEnumeration();

				if (!e.hasMoreElements()) {
					return count;
				} else {
					while (e.hasMoreElements()) {
						count++;
						
						try {
							ObjectMapper mapper = new ObjectMapper();
							ActiveMQTextMessage activeMQTextMessage = e.nextElement();
							NotificationDeatils nd = mapper.readValue(activeMQTextMessage.getText(), NotificationDeatils.class);
							list.add(nd);
							logger.info("Message " + count + ":>> " + nd);
						} catch (IOException ex) {
							logger.log(Level.SEVERE, "ERROR", ex);
						}
					}
				}

				logger.info("Count Info:> " + count);
				return count;
			}

		});
		/**
		 * try { Connection connection =
		 * jmsTemplate.getConnectionFactory().createConnection(); Session session =
		 * connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		 * 
		 * Queue q = new ActiveMQQueue(applicationId);
		 * 
		 * session.createConsumer(q); connection.start();
		 * 
		 * QueueBrowser browser = session.createBrowser(q);
		 * 
		 * @SuppressWarnings("unchecked") Enumeration<ActiveMQTextMessage> e =
		 * browser.getEnumeration(); if (!e.hasMoreElements()) { return list; } else {
		 * while (e.hasMoreElements()) { ActiveMQTextMessage message = e.nextElement();
		 * ObjectMapper mapper = new ObjectMapper();
		 * list.add(mapper.readValue(message.getText(), NotificationDeatils.class));
		 * logger.info("Receive Message:>>> "+message.getText()); } } } catch
		 * (JMSException | IOException e) { logger.log(Level.SEVERE, "ERROR", e); }
		 */
		return list;
	}

}
