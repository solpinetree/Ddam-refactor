package com.ddam.spring.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.community.CommunityFile;

public interface CommunityFileRepository extends JpaRepository<CommunityFile, Long> {

}
