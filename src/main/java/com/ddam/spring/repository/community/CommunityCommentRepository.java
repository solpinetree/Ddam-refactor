package com.ddam.spring.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.community.CommunityComment;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

}
