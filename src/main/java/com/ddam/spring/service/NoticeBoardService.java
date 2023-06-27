package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.NoticeBoard;
import com.ddam.spring.domain.NoticeFile;
import com.ddam.spring.repository.NoticeBoardRepository;
import com.ddam.spring.repository.NoticeFileRepository;

@Service
public class NoticeBoardService {

	@Autowired
	private NoticeBoardRepository repository;
	
	@Autowired
	private NoticeFileRepository filerepository;

	public NoticeBoardService() {
		System.out.println("Notice_boardService() 생성");
	}
	
	// 공지사항 게시판 목록페이지
	public List<NoticeBoard> list() {
		System.out.println("Notice_board list 메서드 진입");
		return repository.findAll(Sort.by(Direction.ASC, "nbid"));
	}

	// 공지사항 작성페이지(제목, 내용)
	@Transactional
	public Long saveNotice_board(NoticeBoard dto) {
		repository.save(dto);
		return (long) 1;
	}
	
	// 공지사항 뷰페이지(제목, 내용)
	@Transactional
	public List<NoticeBoard> viewByNbid(long nbid){
		
		List<NoticeBoard> list = new ArrayList<>();
		
		NoticeBoard data = repository.findById(nbid).orElse(null); // SELECT
		if(data != null) {
			data.setView_cnt(data.getView_cnt() + 1);
			repository.saveAndFlush(data);  // UPDATE
			list.add(data);
		}
		return list;  // 읽어오기
	}
	
	// 공지사항 읽어오기(제목, 내용)
	public List<NoticeBoard> selectByNbid(long nbid){
		List<NoticeBoard> list = new ArrayList<>();
		list.add(repository.findById(nbid).orElse(null));
		return list;
	}
	
	// 공지사항 수정(제목, 내용)
	public int update(NoticeBoard dto, String originalFileName) {
		int cnt = 0;
		NoticeBoard data = repository.findById(dto.getNbid()).orElse(null);
		List<NoticeFile> files = filerepository.findByBoard(data);
		NoticeFile file = files.get(0);
		
		if(data != null) {
				
			data.setContent(dto.getContent());
			data.setSubject(dto.getSubject());
			file.setOriginalname(originalFileName);
			file.setFilename(originalFileName);
			
			repository.saveAndFlush(data);  // UPDATE
			filerepository.saveAndFlush(file);
			cnt = 1;
		}
		return cnt;
	}
	
	// 공지사항 삭제
	public int deleteByUid(long nbid) {
		NoticeBoard board = repository.findById(nbid).orElse(null);
		List<NoticeFile> files = filerepository.findByBoard(board);
		files.forEach(i -> filerepository.delete(i));
		repository.deleteById(nbid);
		repository.flush();
		return 1;
	}


}
