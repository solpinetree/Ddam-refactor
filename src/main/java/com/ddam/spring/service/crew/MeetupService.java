package com.ddam.spring.service.crew;

import com.ddam.spring.domain.crew.Crew;
import com.ddam.spring.domain.crew.Meetup;
import com.ddam.spring.repository.crew.CrewRepository;
import com.ddam.spring.repository.crew.MeetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MeetupService {
	
	private final MeetupRepository meetupRepository;
	private final MeetupUserService meetupUserService;
	private final CrewRepository crewRepository;
	
	public Meetup save(long cid, Meetup meetup) {
		
		Crew crew = crewRepository.findById(cid);
		Meetup res = meetupRepository.save(meetup);
//		crew.getMeetupLists().add(res);
		return res;
	}
	
	@Transactional
	public void deleteByCrewId(long crewId) {
		meetupRepository.deleteByCrewId(crewId);
	}
	
	public void deleteById(long meetupId) {
		meetupRepository.deleteById(meetupId);
	}
}
