package com.ddam.spring.service.notification;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.notification.Notification;
import com.ddam.spring.repository.notification.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	NotificationRepository notificationRepository;

	public void save(Notification notification) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		notification.setWritetime(timestamp);
		
		notificationRepository.save(notification);
	}
	

	public List<Notification> findAllByUserId(long uid){
		return notificationRepository.findAllByUserId(uid);
	}
	

}
