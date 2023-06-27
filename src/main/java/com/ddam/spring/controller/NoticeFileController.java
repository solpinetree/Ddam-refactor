package com.ddam.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.service.NoticeFileService;

@Service
public class NoticeFileController {
	
	private NoticeFileService notice_fileService;
	
	@Autowired
	public void setNotice_fileService(NoticeFileService notice_fileService) {
		this.notice_fileService = notice_fileService;
	}

	public NoticeFileController() {
		System.out.println("Notice_fileController() 생성");
	}

}
