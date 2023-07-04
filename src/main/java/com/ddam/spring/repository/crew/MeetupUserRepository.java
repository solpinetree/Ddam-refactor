package com.ddam.spring.repository.crew;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.crew.Meetup;
import com.ddam.spring.domain.crew.MeetupUser;

public interface MeetupUserRepository extends JpaRepository<MeetupUser, Long>{

	int countByUserIdAndMeetupId(long uid, long mid);

	void deleteByUserIdAndMeetupId(long uid, long mid);
	
	MeetupUser findByUserIdAndMeetupId(long uid, long mid);

	void deleteByMeetup(Meetup m);
}
