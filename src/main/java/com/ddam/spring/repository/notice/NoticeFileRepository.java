package com.ddam.spring.repository.notice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.notice.NoticeBoard;
import com.ddam.spring.domain.notice.NoticeFile;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long>{
	
	List<NoticeFile> findByBoard(NoticeBoard board);
}
