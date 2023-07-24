package com.ddam.spring.repository.crew;

import com.ddam.spring.domain.crew.CrewChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  CrewChatRepository extends JpaRepository<CrewChat, Long> {

	List<CrewChat> findAllByOrderByCreatedAtAsc();
}
