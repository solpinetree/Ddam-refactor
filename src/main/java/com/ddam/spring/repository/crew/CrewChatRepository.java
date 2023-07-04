package com.ddam.spring.repository.crew;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.crew.CrewChat;

public interface  CrewChatRepository extends JpaRepository<CrewChat, Long> {
	List<CrewChat> findByWriterId(long writerId);
}
