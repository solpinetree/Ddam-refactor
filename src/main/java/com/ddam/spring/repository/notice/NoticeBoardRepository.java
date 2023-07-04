package com.ddam.spring.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.notice.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>{
	

}
