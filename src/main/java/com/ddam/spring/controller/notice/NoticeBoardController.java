package com.ddam.spring.controller.notice;

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

import com.ddam.spring.domain.ask.AskBoard;
import com.ddam.spring.domain.notice.NoticeBoard;
import com.ddam.spring.domain.notice.NoticeFile;
import com.ddam.spring.service.ask.AskBoardService;
import com.ddam.spring.service.ask.AskFileService;
import com.ddam.spring.service.notice.NoticeBoardService;
import com.ddam.spring.service.notice.NoticeFileService;
import com.ddam.spring.util.Ask_Notice_UtilFile;

@Controller
public class NoticeBoardController {
	
	private NoticeBoardService notice_boardService;

	private NoticeFileService notice_fileService;
	
	@Autowired
	private AskBoardService ask_boardService;
	
	@Autowired
	private AskFileService ask_fileService;
	
	
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

	public NoticeBoardController() {
		System.out.println("Notice_boardController() 생성");
	}
	
	@RequestMapping("/admin/notice/write")
	public void write() {}


	@PostMapping("/admin/notice/writeOk")
	public void writeOk(NoticeBoard dto, Model model,
						MultipartHttpServletRequest request, @RequestPart MultipartFile files) {
		System.out.println("writeOk 진입");
		
		//제목,내용
		Long cnt = notice_boardService.saveNotice_board(dto);
		model.addAttribute("result", cnt);
		model.addAttribute("dto", dto);
		
		//파일첨부
		NoticeFile file = new NoticeFile();
		
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
		List<NoticeBoard> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
		List<AskBoard> ask_boardlist = ask_boardService.list();
		model.addAttribute("ask_boardlist", ask_boardService.list());
	}

	@RequestMapping("/notice/list")
	public void list(Model model) {
		List<NoticeBoard> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
		List<AskBoard> ask_boardlist = ask_boardService.list();
		model.addAttribute("ask_boardlist", ask_boardService.list());
	}
	
	@GetMapping("/admin/notice/update")
	public void update(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.selectByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.selectByNfid(nbid));
	}
	
	@PostMapping("/admin/notice/updateOk")
	public void updateOk(@ModelAttribute("dto") NoticeBoard notice_board, Model model, @RequestPart MultipartFile files) {
		String originalFilename = files.getOriginalFilename();
		model.addAttribute("notice_boardresult", notice_boardService.update(notice_board, originalFilename));
	}
	
	@GetMapping("/admin/notice/deleteOk")
	public void deleteOk(int nbid, Model model) {
		model.addAttribute("notice_boardresult", notice_boardService.deleteByUid(nbid));
	}

}
