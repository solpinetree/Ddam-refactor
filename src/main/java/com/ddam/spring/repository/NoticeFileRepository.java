package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.NoticeBoard;
import com.ddam.spring.domain.NoticeFile;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long>{
	
	List<NoticeFile> findByBoard(NoticeBoard board);
}
