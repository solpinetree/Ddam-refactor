package com.ddam.spring.controller.crew;

import com.ddam.spring.domain.crew.Follow;
import com.ddam.spring.domain.crew.Meetup;
import com.ddam.spring.dto.UserDto;
import com.ddam.spring.repository.crew.CrewRepository;
import com.ddam.spring.repository.crew.FollowRepository;
import com.ddam.spring.repository.crew.MeetupRepository;
import com.ddam.spring.repository.user.UserRepository;
import com.ddam.spring.service.crew.MeetupService;
import com.ddam.spring.service.crew.MeetupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/crew/meetup")
public class MeetupController {

	private final CrewRepository crewRepository;
	private final MeetupService meetupService;
	private final MeetupRepository meetupRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final MeetupUserService meetupUserService;
	
	
	/**
	 * 크루 모임 추가하는 페이지
	 * @param cid   크루 id
	 * @param meetup 	미팅 엔터티	
	 * @param model
	 * @return
	 */
	@RequestMapping("/insert/{cid}")
	public String insert(@PathVariable long cid, Meetup meetup, Model model) {
		meetup.setCrew(crewRepository.findById(cid));
		model.addAttribute(meetup);
		return "crew/meetupCreate";
	}
	
	
	/**
	 *	/crew/meetup/list 에서 크루장이 미팅 삭제하기 버튼을 누른 경우
	 * @param mid   - 삭제할 meetup id
	 * @return	- 삭제 후 사용자가 가게될 페이지 - 모임참가 페이지
	 */
	@RequestMapping("/delete/{mid}")
	public String delete(@PathVariable long mid) {
		meetupService.deleteById(mid);
		return "redirect:/crew/meetup/list";
	}
	
	/**
	 *	/crew/crew-detail/{cid} 에서 크루장이 미팅 삭제하기 버튼을 누른 경우
	 * @param mid   - 삭제할 meetup id
	 * @return	- 삭제 후 사용자가 가게될 페이지	- 크루의 상세 페이지
	 */
	@RequestMapping("/detaildelete/{mid}/{cid}")
	public String detaildelete(@PathVariable long mid, @PathVariable long cid) {
		meetupService.deleteById(mid);
		return "redirect:/crew/crew-detail/"+cid;
	}
	
	
	/**
	 *  크루 모임 추가 완료 처리하는 핸들러
	 * @param cid
	 * @param datetime2	모임 날짜
	 * @param meetup
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/insertOk")
	public String insertOk(@RequestParam("crewId") long cid, 
			@RequestParam("datetime2") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime datetime2,
			Meetup meetup) throws ParseException {
		meetup.setCrew(crewRepository.findById(cid));
		meetup.setDatetime(datetime2);
		Meetup res = meetupService.save(cid,meetup);
		
		return "redirect:/crew/crew-detail/"+cid;
	}
	
	/**
	 * '모임 참가' 페이지에 연결해주는 핸들러
	 * '모임 참가' 페이지: 지금보다 이후에 있는 모임들을 가까운 시일 순으로 보여준다.
	 */
	@RequestMapping("/list")
	public String meetupList(Model model, Authentication authentication) {
		List<Follow> followlist = followRepository.findByFromUserId(((UserDto)(authentication.getPrincipal())).id());
		model.addAttribute("followList", followlist);
		model.addAttribute("meetupLists", meetupRepository.findByDatetimeGreaterThanOrderByDatetimeAsc(LocalDateTime.now()));
		
		return "crew/allmeetup";
	}
	
	/**
	 * 유저의 모임 참가 or 참가 취소 버튼을 누르면 호출되는 ajax 에서 버튼 상태를 
	 * 처리하는 핸들러 메소드
	 * @param model
	 * @param paramMap
	 */
	@PostMapping("/partupdate")
	@ResponseBody
	public void update(Model model, @RequestParam Map<String, Object> paramMap) {

		long userId = Long.parseLong((String)paramMap.get("userId"));
		long meetupId = Long.parseLong((String)paramMap.get("meetupId"));
		
		System.out.println(userId + " " + meetupId + " 허전");
		
		// 동일 모임에 대한 참가 신청 여부 검색
		int result = meetupUserService.find(userId,meetupId);
		
		if(result ==0) {	//참가하기 안 누른 상태
			meetupUserService.save(userId,meetupId);
		}else {	// 이미 좋아요를 누른 상태라면
			meetupUserService.delete(userId,meetupId);
		}
	}
}
