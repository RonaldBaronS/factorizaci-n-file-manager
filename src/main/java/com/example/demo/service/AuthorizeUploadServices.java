package com.example.demo.service;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.AuthorizeUploadItem;
import com.example.demo.model.AuthorizeUploadRequest;
import com.example.demo.model.AuthorizeUploadResponse;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;




@Service
public class AuthorizeUploadServices {

	public static final String STORAGE_ACCOUNT_NAME = "recursocuenta";
	public static final String STORAGE_ACCOUNT_KEY = "9UXOgKy1Pmg6LysRLdmyOFBtmkSXMbZTTYmb/tGZwFvHBoAy6nCe9e/s/IS8jkUKl9Z5zfUlrQUtQr2XsBYkuw==";
	public static final String STORAGE_CONTAINER_NAME = "test";
	public static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;" + "AccountName="
			+ STORAGE_ACCOUNT_NAME + ";" + "AccountKey=" + STORAGE_ACCOUNT_KEY;
	
	public AuthorizeUploadResponse handle(AuthorizeUploadRequest request) throws InvalidKeyException, URISyntaxException, StorageException {
		
		AuthorizeUploadResponse response=new AuthorizeUploadResponse();
		
		List<AuthorizeUploadItem> itemsResponse=new ArrayList<AuthorizeUploadItem>();
		for (AuthorizeUploadItem item : request.getItems()){
			
			AuthorizeUploadItem itemAuthorized=new AuthorizeUploadItem();
			this.buildItemResponse(itemAuthorized, item);
		    this.generateJwt(itemAuthorized, item);
			this.generateSasEndpoint(itemAuthorized, item);
			itemsResponse.add(itemAuthorized);
			response.setItems(itemsResponse);
		}
		return response;
	}
	
	private void buildItemResponse(AuthorizeUploadItem itemResponse, AuthorizeUploadItem itemRequest) {
		
		itemResponse.setIdentifier(itemRequest.getIdentifier());
	}
	
	private void generateJwt(AuthorizeUploadItem itemResponse, AuthorizeUploadItem itemRequest) {
		
		 Algorithm algorithm = Algorithm.HMAC256("secret");
	        String token = JWT.create()
	        	.withClaim("userIdentifier", "javierperez")
	        	.withClaim("transactionIdentiifier","uuid111-ssd-222dd-sd")
	        	.withClaim("filename", itemRequest.getName())
	        	.withClaim("operation", itemRequest.getOperation())
	        	.withClaim("description",itemRequest.getDescription())
	        	.withClaim("date", itemRequest.getDate())
	            .withIssuer("javierperez")
	            .sign(algorithm);
		 
		itemResponse.setData(token);

	}
	
	private void generateSasEndpoint(AuthorizeUploadItem itemResponse, AuthorizeUploadItem itemRequest) throws InvalidKeyException, URISyntaxException, StorageException {
		CloudBlobContainer container = null;
		container = getBlobContainer();
        String blobKey = "sasfile-" + UUID.randomUUID().toString() + ".txt";
        CloudBlockBlob blob = container.getBlockBlobReference(blobKey);
        SharedAccessBlobPolicy policy = new SharedAccessBlobPolicy(); 
	    
		setBlobPolicy(policy);

		String uploadEndpoint = blob.getUri().toString() + "?" + blob.generateSharedAccessSignature(policy, null);
		itemResponse.setUploadEnpoint(uploadEndpoint);
	}
	
	public CloudBlobContainer getBlobContainer() throws InvalidKeyException, URISyntaxException, StorageException {
		
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		
		storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
		blobClient = storageAccount.createCloudBlobClient();
		container = blobClient.getContainerReference(STORAGE_CONTAINER_NAME);

		System.out.println("Creating container: " + container.getName());  //Creando al contenedor ejemplo :tes..
		container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
				new OperationContext());
		return container;
		
	}
	
	public void setBlobPolicy(SharedAccessBlobPolicy policy) {
		
		policy.setPermissionsFromString("rcw"); 
		Calendar date = Calendar.getInstance();
		Date expire = new Date(date.getTimeInMillis() + (30 * 60000)); 
		Date start = new Date(date.getTimeInMillis());
		policy.setSharedAccessExpiryTime(expire); 
		policy.setSharedAccessStartTime(start); 
			
	}
	
	
	
}
	
  

