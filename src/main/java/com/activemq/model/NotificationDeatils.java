package com.activemq.model;

import java.io.Serializable;

public class NotificationDeatils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4987390438277122579L;
	String applicationId;
	Long customerId;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	@Override
    public String toString() {
        return "NotificationDeatils{" +
                "applicationId='" + applicationId + '\'' +
                ", customerId=" + customerId +
                '}';
    }

}
