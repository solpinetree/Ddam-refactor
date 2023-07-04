package com.ddam.spring.service.crew;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.crew.Crew;
import com.ddam.spring.domain.crew.Likes;
import com.ddam.spring.repository.crew.CrewRepository;
import com.ddam.spring.repository.crew.LikesRepository;
import com.ddam.spring.repository.user.UserRepository;

@Service
public class LikesService {
	
	@Autowired
	LikesRepository likesRepository;
	
	@Autowired
	CrewRepository crewRepository;
	
	@Autowired
	UserRepository userRepository;

	@Transactional
	public void save(long fromUserId, long toCrewId) { // 팔로우
		Likes likes = new Likes();
		
		likes.setFromUser(userRepository.findById(fromUserId));
		likes.setToCrew(crewRepository.findById(toCrewId));
		
		Crew crew = crewRepository.findById(toCrewId);
		crew.setLikesCount(crew.getLikesCount()+1);
		
		likesRepository.save(likes);
	}
	
	@Transactional
	public void delete(long fromUserId, long toCrewId) { // 언팔로우
		likesRepository.deleteByFromUserIdAndToCrewId(fromUserId, toCrewId);
		Crew crew = crewRepository.findById(toCrewId);
		crew.setLikesCount(crew.getLikesCount()-1);
	}

	public int find(long fromUserId, long toCrewId) { // 좋아요를 누른 상태인지 알기위해
		if(likesRepository.countByFromUserIdAndToCrewId(fromUserId, toCrewId) != 0) {	// 좋아요 이미 누른 상태
			return 1;
		}else {		// 좋아요 안 눌러져있음
			return 0;
		}
	}
	
	public void deleteByToCrewId(long toCrewId) {
		likesRepository.deleteByToCrewId(toCrewId);
	}
}
