package com.ddam.spring.service.ask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.ask.AskBoard;
import com.ddam.spring.domain.ask.AskFile;
import com.ddam.spring.repository.ask.AskBoardRepository;
import com.ddam.spring.repository.ask.AskFileRepository;

@Service
public class AskFileService {

	private AskFileRepository repository;
	
	@Autowired
	public void setRepository(AskFileRepository repository) {
		this.repository = repository;
	}

	private AskBoardRepository boardrepository;

	@Autowired
	public void setBoardrepository(AskBoardRepository boardrepository) {
		this.boardrepository = boardrepository;
	}

	public AskFileService() {
		System.out.println("Ask_fileService() 생성");
	}

	//문의사항 작성페이지(파일첨부)
	@Transactional
	public AskFile saveAsk_file(AskFile dto) {
		repository.save(dto);
		return dto;
	}
	
	// 문의사항 뷰페이지(파일첨부)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<AskFile> viewByAfid(long abid){
		
		AskBoard board = boardrepository.findById(abid).orElse(null);
		
		List<AskFile> files = repository.findByBoard(board);
		
		return files;  // 읽어오기
	}
	
	//문의사항 읽어오기(파일첨부)
	public List<AskFile> selectByAfid(long abid){
		
		AskBoard board = boardrepository.findById(abid).orElse(null);
		List<AskFile> files = repository.findByBoard(board);
		return files;
	}
	
	//문의사항 수정(파일첨부)
	public int update(AskFile file) {
		int cnt = 0;
		AskFile data = repository.findById(file.getAfid()).orElse(null);
		if(data != null) {
			data.setFilename(file.getFilename());
			data.setOriginalname(file.getOriginalname());
			repository.saveAndFlush(data);  // UPDATE
			cnt = 1;
		}
		return cnt;
	}
	
	//문의사항 삭제
		public int deleteByUid(long abid) {
			AskBoard board = boardrepository.findById(abid).orElse(null);
			List<AskFile> files = repository.findByBoard(board);
			repository.deleteById(abid);
			repository.flush();		
			return 1;
		}
		
		//문의사항 답변페이지(파일)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<AskFile> selectByAfid_Admin(long abid){
		
		AskBoard board = boardrepository.findById(abid).orElse(null);
		List<AskFile> files = repository.findByBoard(board);
		files = repository.findByBoard(board);
		AskFile file = files.get(0);
		return files;
		
	}

}
