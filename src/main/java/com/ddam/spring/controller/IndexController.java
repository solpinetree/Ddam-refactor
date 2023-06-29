package com.ddam.spring.controller;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.Crew;
import com.ddam.spring.service.CommunityBoardService;
import com.ddam.spring.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final CrewService crewService;
	private final CommunityBoardService communityBoardService;

	@GetMapping("/")
	public String crewList(Model model) {
		List<Crew> crews = crewService.findAll();
		model.addAttribute("crews",crews);
		return "index";
	}

	@GetMapping(value = {"/adminpage"})
	public void adminpage(@RequestParam(defaultValue = "0") int pageNum, String sType, String sKeyword, Model model) {
		Page<CommunityBoard> pagingList = communityBoardService.pagingList(pageNum, sType, sKeyword);
		List<Crew> crews = crewService.findAll();

		// 전체 페이지 수
		int totalPage = pagingList.getTotalPages();

		if (totalPage < 1) {
			totalPage = 1;
		}

		model.addAttribute("pagingList", pagingList);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("sType", sType);
		model.addAttribute("sKeyword", sKeyword);
		model.addAttribute("crews",crews);

	}

}
