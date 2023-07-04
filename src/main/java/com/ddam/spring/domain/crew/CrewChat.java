package com.ddam.spring.domain.crew;

import com.ddam.spring.domain.user.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Comparator;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@Data
@Entity
@org.hibernate.annotations.DynamicUpdate
public class CrewChat implements Comparator<CrewChat> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "writerId")
	@JsonIgnoreProperties({"participantList"})
	User writer;
	
	
	Timestamp writetime;
	String content;
	
	@Override
	public int compare(CrewChat c1, CrewChat c2) { // 채팅 정렬 위해 compare method를 만들어놓음
		long l1 = c1.getWritetime().getTime();
		long l2 = c2.getWritetime().getTime();
		
		if(l1 > l2)
			return 1;
		else
			return -1;
	}
}