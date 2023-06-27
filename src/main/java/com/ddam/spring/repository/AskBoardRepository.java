package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.AskBoard;

public interface AskBoardRepository extends JpaRepository<AskBoard, Long>{
	

}
