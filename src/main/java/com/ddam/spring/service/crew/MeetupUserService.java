package com.ddam.spring.service.crew;

import com.ddam.spring.domain.crew.Meetup;
import com.ddam.spring.domain.crew.MeetupUser;
import com.ddam.spring.repository.crew.MeetupRepository;
import com.ddam.spring.repository.crew.MeetupUserRepository;
import com.ddam.spring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MeetupUserService {

	private final MeetupUserRepository meetupUserRepository;
	private final UserRepository userRepository;
	private final MeetupRepository meetupRepository;
	
	public int find(long uid, long mid) {
		if(meetupUserRepository.countByUserIdAndMeetupId(uid, mid) != 0) {	//이미 참가 신청한 경우
			return 1;
		}else {		// 참가 신청 안한 상태
			return 0;
		}
	}
	
	@Transactional
	public void save(long uid, long mid) {
		Meetup meetup = meetupRepository.findById(mid);
		MeetupUser meetupUser = new MeetupUser();
		meetupUser.setUser(userRepository.findById(uid));
		meetupUser.setMeetup(meetupRepository.findById(mid));
		meetupUserRepository.save(meetupUser);
		meetup.getParticipantList().add(meetupUserRepository.findByUserIdAndMeetupId(uid, mid));
	}
	
	@Transactional
	public void delete(long uid, long mid) { // 참가취소
		Meetup meetup = meetupRepository.findById(mid);
		meetup.getParticipantList().remove(meetupUserRepository.findByUserIdAndMeetupId(uid, mid));
		meetupUserRepository.deleteByUserIdAndMeetupId(uid, mid);
	}

	public void deleteByMeetup(Meetup m) {
		meetupUserRepository.deleteByMeetup(m);
	}
}
