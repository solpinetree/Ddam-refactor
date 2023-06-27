package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>{
	

}
