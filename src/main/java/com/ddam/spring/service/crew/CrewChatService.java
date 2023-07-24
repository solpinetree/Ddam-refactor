package com.ddam.spring.service.crew;

import com.ddam.spring.domain.crew.CrewChat;
import com.ddam.spring.repository.crew.CrewChatRepository;
import com.ddam.spring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrewChatService {

	private final CrewChatRepository crewChatRepository;
	private final UserRepository userRepository;

	public void save(long senderId, String message) {
		CrewChat chat = new CrewChat();
		chat.setSender(userRepository.findById(senderId));
		chat.setMessage(message);

		crewChatRepository.save(chat);
	}

	public List<CrewChat> findAll(){
		return crewChatRepository.findAllByOrderByCreatedAtAsc();
	}
}
