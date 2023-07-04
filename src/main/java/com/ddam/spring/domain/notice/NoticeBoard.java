package com.ddam.spring.domain.notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.ddam.spring.domain.user.User;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DynamicInsert 
public class NoticeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nbid;   
	
	@NonNull
	@NotNull  // NOT NULL
	@Column(length=50)
	private String subject;
	@NotNull
	@Column(columnDefinition = "TEXT")
	
	private String content;
	
	@Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private LocalDateTime regdate;
	
	@Column(columnDefinition = "integer default 0")
	private int view_cnt;

	@ManyToOne
	@JoinColumn(name = "u_id")
	private User u_id;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "notice_id")
	@ToString.Exclude
	private List<NoticeFile> noticeFiles = new ArrayList<>();
	
	

}
