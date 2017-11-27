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
		jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTemplate.setDeliveryPersistent(false);
		jmsTemplate.convertAndSend(notificationDeatils.getApplicationId(), notificationDeatils);
		logger.info("Notification Send:>> " + notificationDeatils.getApplicationId());
	}

	@Override
	public List<NotificationDeatils> receive(String applicationId) {
		logger.log(Level.INFO, "Notification Before applicationId:>> %s ", applicationId);
		List<NotificationDeatils> list = new ArrayList<>();
		jmsTemplate.browse(new ActiveMQQueue(applicationId), new BrowserCallback<Integer>() {

			Integer count = 0;

			@Override
			public Integer doInJms(Session session, QueueBrowser browser) throws JMSException {

				Enumeration<ActiveMQTextMessage> e = browser.getEnumeration();
				
				if ( !e.hasMoreElements() ) { 
				    
				} else { 
					while (e.hasMoreElements()) {
						count++;
						ObjectMapper mapper = new ObjectMapper();
						NotificationDeatils nd;
						try {

							ActiveMQTextMessage activeMQTextMessage = e.nextElement();
							nd = mapper.readValue(activeMQTextMessage.getText(), NotificationDeatils.class);
							activeMQTextMessage.acknowledge();
							list.add(nd);
							System.out.println("Message " + count + ":>> " + nd);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
				
				logger.info("Count Info:> " + count);
				return count;
			}

		});
		
		
		// QueueViewMBean bean

		// Connection connection;
		// Session session;
		// QueueBrowser browser;
		// try {
		// connection = jmsTemplate.getConnectionFactory().createConnection();
		// session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//
		// Queue q = new ActiveMQQueue(applicationId);
		//
		// MessageConsumer consumer = session.createConsumer(q);
		// connection.start();
		//
		// browser = session.createBrowser(q);
		// Enumeration e = browser.getEnumeration();
		//
		// while (e.hasMoreElements()) {
		// TextMessage message = (TextMessage) e.nextElement();
		//
		// System.out.println("Browse [" + e.nextElement() + "]");
		// }
		// } catch (JMSException e) {
		// e.printStackTrace();
		// }

		return list;
	}

}
