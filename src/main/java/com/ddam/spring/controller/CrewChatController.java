package com.ddam.spring.controller;

import com.ddam.spring.domain.CrewChat;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.CrewChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CrewChatController {
	
	private final CrewChatService crewChatService;
	private final UserRepository userRepository;
	
	@RequestMapping("/crew/message/{id}")
	public String message(@PathVariable("id") int id, Model model) throws Exception {
		model.addAttribute("page_id", id);

		return "/crew/message";
	}

	@PostMapping("/chat/insert")
	@ResponseBody
	private int chat_insert(@RequestParam long writerId, @RequestParam String message) throws Exception {
		crewChatService.save(writerId, message);
		return 1;
	}

	@PostMapping("/chat/list/{id}")
	@ResponseBody
	private List<CrewChat> chatlist(@PathVariable int id, HttpServletRequest request) throws Exception {

		
		List<CrewChat> chatting = crewChatService.findAll();
		CrewChat ccc = new CrewChat();
		Collections.sort(chatting, ccc); // 역순으로 정렬하기

		return chatting;
	}
}
