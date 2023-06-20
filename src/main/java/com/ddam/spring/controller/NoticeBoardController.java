package com.ddam.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ddam.spring.domain.Ask_board;
import com.ddam.spring.domain.Notice_board;
import com.ddam.spring.domain.Notice_file;
import com.ddam.spring.service.Ask_boardService;
import com.ddam.spring.service.Ask_fileService;
import com.ddam.spring.service.Notice_boardService;
import com.ddam.spring.service.Notice_fileService;
import com.ddam.spring.util.Ask_Notice_UtilFile;

@Controller
public class NoticeBoardController {
	
	private Notice_boardService notice_boardService;

	private Notice_fileService notice_fileService;
	
	@Autowired
	private Ask_boardService ask_boardService;
	
	@Autowired
	private Ask_fileService ask_fileService;
	
	
	@Autowired
	public void setNotice_boardService(Notice_boardService notice_boardService) {
		this.notice_boardService = notice_boardService;
	}

	@Autowired
	public void setNotice_fileService(Notice_fileService notice_fileService) {
		this.notice_fileService = notice_fileService;
	}
	
	@Autowired
	public void setAsk_boardService(Ask_boardService ask_boardService) {
		this.ask_boardService = ask_boardService;
	}

	@Autowired
	public void setAsk_fileService(Ask_fileService ask_fileService) {
		this.ask_fileService = ask_fileService;
	}

	public NoticeBoardController() {
		System.out.println("Notice_boardController() 생성");
	}
	
	@RequestMapping("/admin/notice/write")
	public void write() {}


	@PostMapping("/admin/notice/writeOk")
	public void writeOk(Notice_board dto, Model model,
			MultipartHttpServletRequest request, @RequestPart MultipartFile files) {
		System.out.println("writeOk 진입");
		
		//제목,내용
		Long cnt = notice_boardService.saveNotice_board(dto);
		model.addAttribute("result", cnt);
		model.addAttribute("dto", dto);
		
		//파일첨부
		Notice_file file = new Notice_file();
		
		String sourceFileName = files.getOriginalFilename(); 

        //UtilFile 객체 생성
        Ask_Notice_UtilFile utilFile = new Ask_Notice_UtilFile();
		
		//파일 업로드 결과값을 path로 받아온다(이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝난다.)
        String uploadPath = utilFile.fileUpload(request, files, file);

		file.setFilename(uploadPath);
		file.setOriginalname(sourceFileName);
		file.setBoard(dto);
		notice_fileService.saveNotice_file(file);
		
		model.addAttribute("file", file);
	}

	@GetMapping("admin/notice/view")
	public void admin_view(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.viewByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.viewByNfid(nbid));
	}
	
	@GetMapping("/notice/view")
	public void view(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.viewByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.viewByNfid(nbid));
	}

	@RequestMapping("/admin/notice/list")
	public void admin_list(Model model) {
		List<Notice_board> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
		List<Ask_board> ask_boardlist = ask_boardService.list();
		model.addAttribute("ask_boardlist", ask_boardService.list());
	}

	@RequestMapping("/notice/list")
	public void list(Model model) {
		List<Notice_board> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
		List<Ask_board> ask_boardlist = ask_boardService.list();
		model.addAttribute("ask_boardlist", ask_boardService.list());
	}
	
	@GetMapping("/admin/notice/update")
	public void update(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.selectByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.selectByNfid(nbid));
	}
	
	@PostMapping("/admin/notice/updateOk")
	public void updateOk(@ModelAttribute("dto") Notice_board notice_board, Model model, @RequestPart MultipartFile files) {
		String originalFilename = files.getOriginalFilename();
		model.addAttribute("notice_boardresult", notice_boardService.update(notice_board, originalFilename));
	}
	
	@GetMapping("/admin/notice/deleteOk")
	public void deleteOk(int nbid, Model model) {
		model.addAttribute("notice_boardresult", notice_boardService.deleteByUid(nbid));
	}

}
