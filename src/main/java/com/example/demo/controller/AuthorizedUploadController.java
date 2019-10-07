package com.example.demo.controller;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.AuthorizeCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.AuthorizeUploadItem;
import com.example.demo.model.AuthorizeUploadRequest;
import com.example.demo.model.AuthorizeUploadResponse;
import com.example.demo.service.AuthorizeUploadServices;
import com.microsoft.azure.storage.StorageException;




@RestController
@RequestMapping("/AuthorizedUpload")
public class AuthorizedUploadController {

	@Autowired
	private AuthorizeUploadServices authorizeUploadService;
	
	@GetMapping
	String getDummy() {
		return "Hello World";
	}
 
   @RequestMapping(value = "/authorize-upload", method = RequestMethod.POST)
	public ResponseEntity<AuthorizeUploadResponse> generateUploadResponse(
			@RequestBody  AuthorizeUploadRequest authorizeUploadRequest) throws Exception {
	   
	     AuthorizeUploadResponse response=authorizeUploadService.handle(authorizeUploadRequest);
	  
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
	

