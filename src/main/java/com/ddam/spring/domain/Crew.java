package com.ddam.spring.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, unique = true, nullable = false)
	private String name; // 이름

	private String category; // 크루 카테고리

	private String area; // 크루 활동 지역
	
   	// 크루 썸네일
	private String fileOriginName; // 받아올 때 파일 이름
	private String fileName;	// 저장할 때 파일 이름
	private String filePath;	// 저장 및 불러올 경로

	@Column(columnDefinition = "LONGTEXT")
	private String description; // 크루 소개

	private Long memberLimit; // 크루 멤버 인원 제한수
	
	private Long memberCount; // 크루 멤버 수
	
	private Long likesCount; // 좋아요 수
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="admin_id")
	private User crewAdmin;	// 크루장

	@CreationTimestamp
	private Timestamp createDate;

	@CreationTimestamp
	private Timestamp updateDate;
}
