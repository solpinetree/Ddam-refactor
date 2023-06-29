package com.ddam.spring.controller;

import com.ddam.spring.domain.Notification;
import com.ddam.spring.repository.NotificationRepository;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
