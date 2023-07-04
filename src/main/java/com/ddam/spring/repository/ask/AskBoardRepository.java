package com.ddam.spring.repository.ask;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.ask.AskBoard;

public interface AskBoardRepository extends JpaRepository<AskBoard, Long>{
	

}
