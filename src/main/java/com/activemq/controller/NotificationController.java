package com.activemq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.activemq.model.NotificationDeatils;
import com.activemq.service.NotificationService;

@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	/**
	 * 
	 * @param notificationDeatils
	 * @return
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public ResponseEntity<Object> sendNotification(@RequestBody NotificationDeatils notificationDeatils) {
		notificationService.send(notificationDeatils);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	/**
	 * 
	 * @param applicationId
	 * @return
	 */
	@RequestMapping(value = "/receive/{applicationId}", method = RequestMethod.GET)
	public ResponseEntity<Object> receiveNotification(@PathVariable("applicationId") String applicationId) {
		return new ResponseEntity<>(notificationService.receive(applicationId), HttpStatus.OK);
	}

}
