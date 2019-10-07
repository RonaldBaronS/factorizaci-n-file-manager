package com.example.demo.model;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizeUploadResponse {

	private List<AuthorizeUploadItem> items;

}
