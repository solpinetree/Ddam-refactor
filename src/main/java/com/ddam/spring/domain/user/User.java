package com.ddam.spring.domain.user;

import com.ddam.spring.constant.Role;
import com.ddam.spring.domain.crew.MeetupUser;
import com.ddam.spring.domain.notification.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@Table(name = "user", indexes = {
		@Index(columnList = "username", unique = true)
})
@Entity
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String username;
	
	@NotNull
    private String email;

	@NotNull
	private String nickname;
	
	@Enumerated(EnumType.STRING)
	@NotNull
    private Role role;

	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ToString.Exclude
	@JsonIgnore
	private Set<MeetupUser> participantList = new HashSet<>();
	
	@OneToMany(mappedBy= "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ToString.Exclude
	@JsonIgnore
	private List<Notification> notifications = new ArrayList<Notification>();

	public void addNotification(Notification notification) {

		if(notifications ==null) {
			notifications = new ArrayList<Notification>();
		}

		notification.setUser(this);
		notifications.add(notification);
	}

	protected User() {}

	private User(String username, String email, String nickname, Role role) {
		this.username = username;
		this.email = email;
		this.nickname = nickname;
		this.role = role;
	}

	public static User of(String username, String email, String nickname, Role role) {
		return new User(
				username,
				email,
				nickname,
				role
		);
	}
}
