package com.ddam.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ddam.spring.domain.AskBoard;
import com.ddam.spring.domain.AskFile;
import com.ddam.spring.domain.NoticeBoard;
import com.ddam.spring.service.AskBoardService;
import com.ddam.spring.service.AskFileService;
import com.ddam.spring.service.NoticeBoardService;
import com.ddam.spring.service.NoticeFileService;
import com.ddam.spring.util.Ask_Notice_UtilFile;

@Controller
public class AskBoardController {
	
	@Autowired
	private AskBoardService ask_boardService;
	
	@Autowired
	private AskFileService ask_fileService;
	
	@Autowired
	private NoticeBoardService notice_boardService;

	@Autowired
	private NoticeFileService notice_fileService;
	
	@Autowired
	public void setNotice_boardService(NoticeBoardService notice_boardService) {
		this.notice_boardService = notice_boardService;
	}

	@Autowired
	public void setNotice_fileService(NoticeFileService notice_fileService) {
		this.notice_fileService = notice_fileService;
	}
		
	@Autowired
	public void setAsk_boardService(AskBoardService ask_boardService) {
		this.ask_boardService = ask_boardService;
	}

	@Autowired
	public void setAsk_fileService(AskFileService ask_fileService) {
		this.ask_fileService = ask_fileService;
	}

	public AskBoardController() {
		System.out.println("Ask_boardController() 생성");
	}
	
	@RequestMapping("/ask/write")
	public void write() {}


	@PostMapping("/ask/writeOk")
	public void writeOk(AskBoard dto, Model model,
                        MultipartHttpServletRequest request, @RequestPart MultipartFile files
			) {
		
		//제목,내용
		Long cnt = ask_boardService.saveAsk_board(dto);
		model.addAttribute("result", cnt);
		model.addAttribute("dto", dto);
		
		//파일첨부
		AskFile file = new AskFile();
		
		String sourceFileName = files.getOriginalFilename();
			
		System.out.println(sourceFileName);
		
        //UtilFile 객체 생성
        Ask_Notice_UtilFile utilFile = new Ask_Notice_UtilFile();
		
		//파일 업로드 결과값을 path로 받아온다(이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝난다.)
        String uploadPath = utilFile.fileUpload(request, files, file);
		
		file.setFilename(uploadPath);
		file.setOriginalname(sourceFileName);
		file.setBoard(dto);
		file.setAbid(dto.getAbid());
		ask_fileService.saveAsk_file(file);
		
		dto.getAskFiles().add(file);
		
		System.out.println(dto.getAskFiles().get(0).getFilename());
		
		model.addAttribute("file", file);
	}
	
	@GetMapping("/admin/ask/write")
	public void adminwrite(@RequestParam Long abid, Model model) {
		System.out.println(abid);
		model.addAttribute("ask_boardlist", ask_boardService.selectByAbid_Admin(abid));
		model.addAttribute("ask_filelist", ask_fileService.selectByAfid_Admin(abid));	
	}
	
	@PostMapping("/admin/ask/writeOk")
	public void writeOk(@ModelAttribute("dto") AskBoard ask_board, Model model) {
		model.addAttribute("ask_boardresult", ask_boardService.update_admin(ask_board));
	}
	
	@GetMapping("/ask/view")
	public void view(Long abid, Model model) {
		model.addAttribute("ask_boardlist", ask_boardService.viewByAbid(abid));
		model.addAttribute("ask_filelist", ask_fileService.viewByAfid(abid));
	}

	@RequestMapping("/ask/list")
	public void list(Model model) {
		System.out.println("list 진입");
		List<AskBoard> ask_boardlist = ask_boardService.list();
		model.addAttribute("ask_boardlist", ask_boardService.list());
		List<NoticeBoard> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
	}
	
	@GetMapping("/ask/update")
	public void update(int abid, Model model) {
		model.addAttribute("ask_boardlist", ask_boardService.selectByAbid(abid));
		model.addAttribute("ask_filelist", ask_fileService.selectByAfid(abid));
	}
	
	@PostMapping("/ask/updateOk")
	public void updateOk(@ModelAttribute("dto") AskBoard ask_board, Model model, @RequestPart MultipartFile files) {
		String originalFilename = files.getOriginalFilename();
		model.addAttribute("ask_boardresult", ask_boardService.update(ask_board, originalFilename));
	}
	
	@GetMapping("/ask/deleteOk")
	public void deleteOk(int abid, Model model) {
		model.addAttribute("ask_boardresult", ask_boardService.deleteByUid(abid));
	}

}
