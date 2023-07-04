package com.ddam.spring.domain.crew;

import com.ddam.spring.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	@JsonIgnoreProperties({"crews",  "createDate", "updateDate"})
	private User fromUser; // id, username, name
	
	@ManyToOne
	@JoinColumn(name = "toCrewId")
	@JsonIgnoreProperties({ "crewAdmin", "likes"})
	private Crew toCrew; // 기본 :  id
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}


