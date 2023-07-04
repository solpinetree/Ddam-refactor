package com.ddam.spring.service.user;

import com.ddam.spring.domain.user.User;
import com.ddam.spring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService{
	
	private final UserRepository userRepository;

	@Transactional
	public User saveUser(User user){
        return userRepository.save(user);
    }

	public Optional<User> findByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username);
    }
}
