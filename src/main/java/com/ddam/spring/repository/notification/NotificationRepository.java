package com.ddam.spring.repository.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.notification.Notification;
import com.ddam.spring.domain.user.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByUserId(long uid);
	
	Notification findById(long id);
	
	Notification save(Notification notification);

	List<Notification> findByUser(User user);

	int countByUserId(long userId);
}
