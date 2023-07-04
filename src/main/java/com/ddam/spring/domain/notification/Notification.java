package com.ddam.spring.domain.notification;

import com.ddam.spring.domain.user.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Comparator;

@Data
@Entity
@ToString
public class Notification implements Comparator<Notification>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	Timestamp writetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	private String content;


@Override
public int compare(Notification c1, Notification c2) { // 알림 정렬 위해 compare method를 만들어놓음
	long l1 = c1.getWritetime().getTime();
	long l2 = c2.getWritetime().getTime();
	
	if(l1 < l2)
		return 1;
	else
		return -1;
}
}
