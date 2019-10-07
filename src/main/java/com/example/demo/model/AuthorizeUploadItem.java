package com.example.demo.model;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class AuthorizeUploadItem{
	
	 private String identifier;  
	@JsonInclude(JsonInclude.Include.NON_NULL)
	 private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	 private String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	 private Date date;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	 private String operation;

	 private String uploadEnpoint;
	 private String data;
}
