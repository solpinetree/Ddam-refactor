package com.ddam.spring.repository.crew;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.crew.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long>{

	Crew findById(long id);

	List<Crew> findAll();
}
