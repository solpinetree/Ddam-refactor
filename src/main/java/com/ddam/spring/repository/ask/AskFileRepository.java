package com.ddam.spring.repository.ask;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.ask.AskBoard;
import com.ddam.spring.domain.ask.AskFile;

public interface AskFileRepository extends JpaRepository<AskFile, Long>{
	
	List<AskFile> findByBoard(AskBoard board);
	
}
