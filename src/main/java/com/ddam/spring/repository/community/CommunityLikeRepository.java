package com.ddam.spring.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.community.CommunityBoard;
import com.ddam.spring.domain.community.CommunityLike;
import com.ddam.spring.domain.user.User;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

	CommunityLike findByUserAndCommunityBoard(User user, CommunityBoard communityBoard);

}
