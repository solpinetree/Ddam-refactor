package com.ddam.spring.service;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.domain.Meetup;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.MeetupRepository;
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
