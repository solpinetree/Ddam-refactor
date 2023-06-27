package com.ddam.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.service.AskFileService;

@Service
public class AskFileController {
	
	private AskFileService ask_fileService;
	
	@Autowired
	public void setAsk_fileService(AskFileService ask_fileService) {
		this.ask_fileService = ask_fileService;
	}

	public AskFileController() {
		System.out.println("Ask_fileController() 생성");
	}

}
