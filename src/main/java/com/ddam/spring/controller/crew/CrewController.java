package com.ddam.spring.controller.crew;

import com.ddam.spring.domain.crew.*;
import com.ddam.spring.domain.notification.Notification;
import com.ddam.spring.domain.user.User;
import com.ddam.spring.dto.UserDto;
import com.ddam.spring.repository.crew.CrewRepository;
import com.ddam.spring.repository.crew.FollowRepository;
import com.ddam.spring.repository.crew.FollowRequestRepository;
import com.ddam.spring.repository.crew.MeetupRepository;
import com.ddam.spring.repository.user.UserRepository;
import com.ddam.spring.service.crew.CrewService;
import com.ddam.spring.service.crew.FollowRequestService;
import com.ddam.spring.service.crew.FollowService;
import com.ddam.spring.service.notification.NotificationService;
import com.ddam.spring.validation.CrewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/crew")
public class CrewController {

    private final CrewValidator crewValidator;
    private final CrewRepository crewRepository;
    private final CrewService crewService;
    private final FollowRequestService followRequestService;
    private final FollowRequestRepository followRequestRepository;
    private final FollowService followService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final MeetupRepository meetupRepository;
    private final FollowRepository followRepository;

    // 이 컨트롤러 의 handler 에서 폼 데이터를 바인딩할때 검증하는 객체 지정
    @InitBinder("crewValidator")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(crewValidator);
    }

    // area 지역
    List<String> areaList = Arrays.asList(
            new String[]{"서울", "인천", "대전", "세종", "경기", "충북", "충남", "강원", "광주", "부산",
                    "대구", "울산", "전북", "전남", "경북", "경남", "제주"});
    // + 글로벌 ..

    // 운동 종류
    List<String> category = Arrays.asList(
            new String[]{"러닝", "조깅", "자전거", "등산", "배드민턴", "테니스", "탁구", "축구", "농구", "스키", "요가",
                    "족구", "배구", "소프트볼"});

    /**
     * 크루 모집 페이지
     */
    @GetMapping("/crews")
    public String crews(Model model, HttpServletRequest request) {
        List<Crew> crewList = crewRepository.findAll(Sort.by(Sort.Direction.DESC, "likesCount"));
        long userCount = userRepository.count();
        long meetupCount = meetupRepository.count();
        model.addAttribute("userCount", userCount);
        model.addAttribute("meetupCount", meetupCount);
        model.addAttribute("crewList", crewList);

        return "crew/crews/main";
    }

    /**
     * 크루 싱글 페이지(한 크루의 상세 페이지)
     */
    @GetMapping("/crew-detail/{cid}")
    public String crew_single(@PathVariable("cid") long cid, Model model, Authentication authentication) {

        if (authentication.isAuthenticated()) {

            Long userId = ((UserDto) authentication.getPrincipal()).id();

            // 지금 로그인한 유저와 크루와의 관계
            String crewRole = followService.find(userId, cid);
            model.addAttribute("crewRole", crewRole);
            List<Follow> followlist = followRepository.findByFromUserId(userId);
            model.addAttribute("followList", followlist);
        }

        Crew crew = crewRepository.findById(cid);
        model.addAttribute("crew", crew);

        List<FollowRequest> requests = followRequestRepository.findByToCrewId(cid);
        model.addAttribute("requests", requests);

        List<User> members = followService.findMembers(cid);
        model.addAttribute("members", members);

        List<Meetup> meetupLists = meetupRepository.findByCrewIdAndDatetimeGreaterThanOrderByDatetimeAsc(cid, LocalDateTime.now().minusDays(1L));
        model.addAttribute("meetupLists", meetupLists);

        List<Meetup> allmeetupLists = meetupRepository.findByCrewId(cid);
        model.addAttribute("allmeetupLists", allmeetupLists);

        return "crew/crewdetail";
    }

    /**
     * 크루 개설 페이지
     */
    @GetMapping("/newcrew")
    public String crew_enroll(Crew crew, Model model) {
        model.addAttribute("areaList", areaList);
        model.addAttribute("category", category);
        return "crew/newcrew";
    }

    @PostMapping("/crew-writeOk")
    public String writeOk(
            @RequestParam("file") MultipartFile file,
            @Valid Crew crew,
            Authentication authentication,
            Model model,
            BindingResult result,
            RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {


        // 크루 썸네일을 입력 안 한 경우
        if (crew.getFileName() == null && file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errFile", "썸네일은 필수입니다.");
        } else if (!file.isEmpty()) {

            System.out.println("파일 입력 됨");

            // 크루 썸네일이 입력된 경우
            String path = System.getProperty("user.dir") + "/src/main/resources/static/crewphoto/";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(path + fileName);
            System.out.println("파일 저장 경로: " + saveFile);
            file.transferTo(saveFile);

            crew.setFileOriginName(file.getOriginalFilename());
            crew.setFileName(fileName);
            crew.setFilePath("/crewphoto/" + fileName);
        }

        // 크루 입력 내용 유효성 검사
        if (result.hasErrors() || crew.getFileName() == null) {
            redirectAttributes.addFlashAttribute("crew", crew);
            redirectAttributes.addFlashAttribute("fileName", crew.getFileName());
            redirectAttributes.addFlashAttribute("fileOriginName", crew.getFileOriginName());
            redirectAttributes.addFlashAttribute("filePath", crew.getFilePath());
            showErrors(result, redirectAttributes);
            return "redirect:/crew/newcrew";
        }

        // 유효성 검사 통과 후
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        crew.setMemberCount(1L);
        crew.setLikesCount(0L);
        crew.setCrewAdmin(user);

        crewRepository.save(crew);

        // 파일 업로드할 때 딜레이가 있어서....
        try {
            Thread.sleep((long) 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/crew/crew-detail/" + crew.getId();
    }

    /**
     * @param id : 크루 id
     *           크루 정보 수정
     */
    @RequestMapping("/update/{id}")
    public String crewUpdate(@PathVariable("id") long id, Crew crew, Model model) {
        model.addAttribute("crew", crewRepository.findById(id));
        model.addAttribute("cid", id);
        model.addAttribute("areaList", areaList);
        model.addAttribute("category", category);
        return "crew/crew-update";
    }

    @PostMapping("/crew-updateOk")
    public String updateOk(@RequestParam("file") MultipartFile file, @Valid Crew crew,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes, HttpServletRequest request) throws IllegalStateException, IOException {

        Crew precrew = crewService.findById(crew.getId());


        // 크루 썸네일을 수정한 경우
        if (!file.isEmpty()) {

            // 기존 파일 삭제
            File prefile = new File(System.getProperty("user.dir") + "/src/main/resources/static/crewphoto/" + crew.getFilePath());
            prefile.delete();


            String path = System.getProperty("user.dir") + "/src/main/resources/static/crewphoto/";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(path + fileName);
            System.out.println("파일 저장 경로: " + saveFile);
            file.transferTo(saveFile);

            precrew.setFileOriginName(file.getOriginalFilename());
            precrew.setFileName(fileName);
            precrew.setFilePath("/crewphoto/" + fileName);
        }

        precrew.setArea(crew.getArea());
        precrew.setCategory(crew.getCategory());
        precrew.setDescription(crew.getDescription());
        precrew.setName(crew.getName());

        crewService.save(precrew);

        // 파일 업로드할 때 딜레이가 있어서....
        try {
            Thread.sleep((long) 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/crew/crew-detail/" + crew.getId();
    }


    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        crewService.deleteById((long) id);
        return "redirect:/crew/crews";
    }


    /**
     * 크루 관리 페이지(크루장만 접근 가능)
     */
    @GetMapping("/manage/{id}")
    private String crewmanage(@PathVariable("id") long id, Model model) {
        System.out.println("crew manage 진입 ");
        List<User> crewMembers = followService.findMembers(id);
        model.addAttribute("crewMembers", crewMembers);
        return "crew/manage";
    }

    /**
     * 해당 크루의 request 수를 셈
     *
     * @param paramMap - crewId가 담긴 Map
     * @return    - 해당 크루의 request 수를 리턴
     */
    @PostMapping("/countrequest")
    public @ResponseBody Map<String, Integer> countRequest(@RequestParam Map<String, Object> paramMap) {

        long crewId = Long.parseLong((String) paramMap.get("crewId"));

        Map<String, Integer> res = new HashMap<>();

        // 해당 크루의 알림 수를 구함
        int count = followRequestRepository.countByToCrewId(crewId);

        res.put("count", count);

        return res;
    }

    /**
     * 해당 크루에게 온 팔로우 요청들을 구함
     *
     * @param cid 크루 id
     * @return 해당 크루의 FollowRequest 리스트를 리턴
     */
    @PostMapping("/requestlist/{cid}")
    public @ResponseBody List<FollowRequest> requestList(@PathVariable long cid) {

        List<FollowRequest> res = followRequestRepository.findAllByToCrewId(cid);
        return res;

    }

    /**
     * 팔로우 요청이 왔을 때
     */
    @RequestMapping("/follow/request/{cid}")
    public String request(@PathVariable("cid") long cid, Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("cid", cid);
        return "crew/follow-request";
    }

    /**
     * 팔로우 요청 완료
     */
    @PostMapping("/follow/request-ok")
    public String requestOk(@RequestParam String info, @RequestParam long cid, @RequestParam long uid,
                            Model model, RedirectAttributes redirectAttributes) {

        FollowRequest followRequest = new FollowRequest();
        followRequest.setInfo(info);


        if (followRequestService.check(uid, cid) == true) {
            User user = userRepository.findById(uid);
            Crew crew = crewRepository.findById(cid);
            followRequest.setFromUser(user);
            followRequest.setToCrew(crew);
            followRequestService.requestSave(followRequest);
//    		crew.getRequests().add(user);
        }

//    	redirectAttributes.addFlashAttribute("cid", cid);
        model.addAttribute("cid", cid);

        return "crew/request-ok";
    }


    /**
     * 언팔로우 요청이 왔을 때(크루 탈퇴)
     */
    @RequestMapping("/unfollow/{cid}")
    public String unfollow(@PathVariable("cid") long cid, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElseThrow();

//    	crewRepository.findById(cid).getMembers().remove(user);

        followService.deleteByFromUserIdAndToCrewId(user.getId(), cid);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(crewRepository.findById(cid).getName() + " 크루를 탈퇴했습니다.");
        notificationService.save(notification);

        return "redirect:/crew/crew-detail/" + cid;
    }

    /**
     * 크루 멤버 내보내기 (크루장이)
     */
    @PostMapping("/deletemember")
    @ResponseBody
    public void deletemember(@RequestParam Map<String, Object> paramMap) {

        long memberId = Long.parseLong((String) paramMap.get("memberId"));
        long crewId = Long.parseLong((String) paramMap.get("crewId"));


        Notification notification = new Notification();
        notification.setUser(userRepository.findById(memberId));
        notification.setContent(crewRepository.findById(crewId).getName() + " 크루에서 내보내졌습니다.");
        notificationService.save(notification);

        // Crew의 List<User> members에서 삭제
//		crewRepository.findById(crewId).getMembers().remove(userRepository.findById(memberId));

        followService.deleteByFromUserIdAndToCrewId(memberId, crewId);
    }

    /**
     * 팔로우 요청에 승인할 때(크루장이)
     */
    @RequestMapping("/follow")
    @ResponseBody
    public void follow(Model model, @RequestParam Map<String, Object> paramMap) {

        long requestId = Long.parseLong((String) paramMap.get("requestId"));
        long crewId = Long.parseLong((String) paramMap.get("crewId"));

        followService.save(requestId, crewId);

        List<User> requests = followRequestService.requests(crewId);
        model.addAttribute("requests", requests);

        List<User> members = followService.findMembers(crewId);
        model.addAttribute("members", members);
    }

    /**
     * 팔로우 요청에 거절할 때(크루장이)
     */
    @RequestMapping("/reject")
    @ResponseBody
    public void reject(@RequestParam Map<String, Object> paramMap) {

        long requestId = Long.parseLong((String) paramMap.get("requestId"));
        long crewId = Long.parseLong((String) paramMap.get("crewId"));
        Notification notification = new Notification();

        User user = userRepository.findById(requestId);
        Crew crew = crewRepository.findById(crewId);

        notification.setUser(user);
        notification.setContent(crew.getName() + " 크루 멤버 요청이 거절되었습니다.");
        notificationService.save(notification);

        followRequestRepository.deleteByFromUserIdAndToCrewId(requestId, crewId);
    }


    /**
     * 크루장이 크루 멤버 명단 확인할 때
     * cors 정책 : 같은 도메인 내에서는 안 막아도 됨.
     */
    @PostMapping("/member")
    @ResponseBody
    public Object memberList(HttpServletRequest request, HttpServletResponse response) {
        long cid = Long.parseLong(request.getParameter("cid"));
        List<User> crewMembers = followService.findMembers(cid);

        Map<String, List<User>> result = new HashMap<>();

        result.put("members", crewMembers);

        return result;
    }


    /**
     * crew/newcrew에서 validation 에러 발생시 전달할 에러 메시지 설정한느 메소드
     *
     * @param errors
     * @param redirectAttributes
     */
    public void showErrors(Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            List<FieldError> errList = errors.getFieldErrors();

            for (FieldError err : errList) {
                String code = err.getCode();
                switch (code) {
                    case "emptyName":
                        redirectAttributes.addFlashAttribute("errName", "크루 이름을 입력해주세요.");
                        break;
                    case "emptyArea":
                        redirectAttributes.addFlashAttribute("errArea", "크루가 주로 활동할 지역을 입력해주세요.");
                        break;
                    case "emptyCategory":
                        redirectAttributes.addFlashAttribute("errCategory", "크루의 운동 종목을 입력해주세요.");
                        break;
                    case "emptyMemberLimit":
                        redirectAttributes.addFlashAttribute("errMemberLimit", "멤버를 몇명까지 수용할지 입력해주세요.");
                        break;
                    case "emptyDescription":
                        redirectAttributes.addFlashAttribute("errDescription", "크루의 목표와 함께 소개 인사를 적어주세요.");
                        break;
                }
            }
        }
    }


}
