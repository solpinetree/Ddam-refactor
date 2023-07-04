package com.ddam.spring.service.crew;

import com.ddam.spring.domain.crew.CrewChat;
import com.ddam.spring.repository.crew.CrewChatRepository;
import com.ddam.spring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CrewChatService {

	private final CrewChatRepository crewChatRepository;
	private final UserRepository userRepository;

	public void save(long writerId, String content) {
		CrewChat chat = new CrewChat();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		chat.setWritetime(timestamp);
		chat.setWriter(userRepository.findById(writerId));
		chat.setContent(content);

		crewChatRepository.save(chat);
	}
	
	public List<CrewChat> findByWriterId(long writerId) {
		return crewChatRepository.findByWriterId(writerId);
	}
	
	public List<CrewChat> findAll(){
		return crewChatRepository.findAll();
	}
	
//	public List<CrewChat> findAllExceptThisUser(long userId){
//		List<CrewChat> all = crewChatRepository.findAll();
//		List<CrewChat> rmv = findByWriterId(userId);
//		for(CrewChat cc : rmv) {
//			all.remove(cc);
//		}
//		
//		return all;
//	}
}
