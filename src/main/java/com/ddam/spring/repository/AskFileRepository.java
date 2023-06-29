package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.AskBoard;
import com.ddam.spring.domain.AskFile;

public interface AskFileRepository extends JpaRepository<AskFile, Long>{
	
	List<AskFile> findByBoard(AskBoard board);
	
}
