package com.ddam.spring.controller.notification;

import com.ddam.spring.domain.notification.Notification;
import com.ddam.spring.repository.notification.NotificationRepository;
import com.ddam.spring.repository.user.UserRepository;
import com.ddam.spring.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	
	@PostMapping("/{uid}")
	private List<Notification> notilist(@PathVariable long uid) throws Exception {
		
		List<Notification> res = notificationRepository.findAllByUserId(uid);
		Notification noti = new Notification();
		Collections.sort(res, noti);
		return res;
		
	}
	
	@PostMapping("/deleteNoti")
	private void deleteNoti(@RequestParam HashMap<String, Object> param) throws Exception {

		long notiId = Long.parseLong((String)param.get("notiId"));
		
		notificationRepository.deleteById(notiId);
	}
	
	@GetMapping("/count")
	public int count(@RequestParam Long userId) {
		int count = notificationRepository.countByUserId(userId);
		
		return count;
	}
}
