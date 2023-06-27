package com.ddam.spring.controller;

import com.ddam.spring.domain.User;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController {

	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping(value = "/loginOk")
	public String loginOk(@Validated @ModelAttribute User user, String username, String password,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "expetion", required = false) String exception, BindingResult bindingResult,
			HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		System.out.println("loginOk 확인");

		// 로그인 직전의 url 을 Session 에 기록
		String referer = request.getHeader("Referer");
		if (referer != null)
			request.getSession().setAttribute("url_prior_login", referer);

		session.setAttribute("sessionedUser", user.getUsername());

		// 현재 로그인한 user 정보 Authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = (User) authentication.getPrincipal();
		if (loggedUser != null) {
			System.out.println("현재로그인 username : " + loggedUser.getUsername());
		}

		System.out.println("POST: /login");
		return "redirect:/";
	}

	// 마이페이지
	@RequestMapping("/mypage")
	public void mypage(Long username, User user) {

	}

	// 크루장페이지
	@RequestMapping("/manager")
	public void manager(Long username, User user) {

	}

	@RequestMapping("/auth")
	@ResponseBody
	public Authentication auth(HttpSession session) {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response,
					SecurityContextHolder.getContext().getAuthentication());
		}
		return "redirect:/";
	}
}