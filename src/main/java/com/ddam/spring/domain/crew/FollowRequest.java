package com.ddam.spring.domain.crew;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ddam.spring.domain.AuditingFields;
import com.ddam.spring.domain.user.User;

import lombok.Data;

@Data
@Entity
@org.hibernate.annotations.DynamicUpdate
public class FollowRequest extends AuditingFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "fromUserId")
	User fromUser;

	@ManyToOne
	@JoinColumn(name = "toCrewId")
    Crew toCrew;
	
	@Column(nullable = false, length=1000000)
	String info;  // 지원동기
}
