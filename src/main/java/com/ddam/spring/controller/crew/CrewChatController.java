package com.ddam.spring.controller.crew;

import com.ddam.spring.domain.crew.CrewChat;
import com.ddam.spring.service.crew.CrewChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/crews/chats")
public class CrewChatController {
	
	private final CrewChatService crewChatService;

	@GetMapping
	@ResponseBody
	private List<CrewChat> chatlist() {
		return crewChatService.findAll();
	}
}
