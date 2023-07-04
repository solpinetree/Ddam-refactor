package com.ddam.spring.domain.crew;

import com.ddam.spring.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MeetupUser {
	
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    @JsonIgnoreProperties({"participantList"})
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "meetup_id")
	    @JsonIgnoreProperties({"participantList"})
	    private Meetup meetup;
}

