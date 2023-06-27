package com.ddam.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.NoticeBoard;
import com.ddam.spring.domain.NoticeFile;
import com.ddam.spring.repository.NoticeBoardRepository;
import com.ddam.spring.repository.NoticeFileRepository;

@Service
public class NoticeFileService {

	private NoticeFileRepository repository;
	
	@Autowired
	public void setRepository(NoticeFileRepository repository) {
		this.repository = repository;
	}

	private NoticeBoardRepository boardrepository;

	@Autowired
	public void setBoardrepository(NoticeBoardRepository boardrepository) {
		this.boardrepository = boardrepository;
	}

	public NoticeFileService() {
		System.out.println("Notice_fileService() 생성");
	}

	//공지사항 작성페이지(파일첨부)
	@Transactional
	public NoticeFile saveNotice_file(NoticeFile dto) {
		repository.save(dto);
		return dto;
	}
	
	//공지사항 뷰페이지(파일첨부)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<NoticeFile> viewByNfid(long nbid){
		
		NoticeBoard board = boardrepository.findById(nbid).orElse(null);
		
		List<NoticeFile> files = repository.findByBoard(board);
		
		return files;  // 읽어오기
	}
	
	//공지사항 읽어오기(파일첨부)
	public List<NoticeFile> selectByNfid(long nbid){
		
		NoticeBoard board = boardrepository.findById(nbid).orElse(null);
		List<NoticeFile> files = repository.findByBoard(board);
		files = repository.findByBoard(board);
		NoticeFile file = files.get(0);
		return files;
	}
	
	//공지사항 수정(파일첨부)
	public int update(NoticeFile file) {
		int cnt = 0;
		NoticeFile data = repository.findById(file.getNfid()).orElse(null);
		if(data != null) {
			data.setFilename(file.getFilename());
			data.setOriginalname(file.getOriginalname());
			repository.saveAndFlush(data);  // UPDATE
//			repository.save(file);
			cnt = 1;
		}
		return cnt;
	}
	
	//공지사항 삭제
		public int deleteByUid(long abid) {
			NoticeBoard board = boardrepository.findById(abid).orElse(null);
			List<NoticeFile> files = repository.findByBoard(board);
			repository.deleteById(abid);
			repository.flush();		
			return 1;
		}

}
