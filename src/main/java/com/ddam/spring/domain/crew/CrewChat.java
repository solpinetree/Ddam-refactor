package com.ddam.spring.domain.crew;

import com.ddam.spring.domain.AuditingFields;
import com.ddam.spring.domain.user.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.Data;

import javax.persistence.*;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@Data
@Entity
public class CrewChat extends AuditingFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String message;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	User sender;

}