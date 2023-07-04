package com.ddam.spring.service;


import com.ddam.spring.domain.Crew;
import com.ddam.spring.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CrewService {

    private final CrewRepository crewRepository;
	private final FollowRequestService followRequestService;
	private final FollowService followService;
	private final LikesService likesService;
	private final MeetupService meetupService;
	
	@Transactional
	public void deleteById(long id) {
		followRequestService.deleteByToCrewId(id);
		followService.deleteByToCrewId(id);
		likesService.deleteByToCrewId(id);
		meetupService.deleteByCrewId(id);
		crewRepository.deleteById(id);
	}

	public List<Crew> findAll() {
		return crewRepository.findAll();
	}

	public Crew findById(long id) {
		return crewRepository.findById(id);
	}
	
	public Crew findCrews(Long id) {
		  return crewRepository.findById(id).orElse(null);
	}
	
	public void save(Crew crew) {
		crewRepository.save(crew);
	}
	
}
