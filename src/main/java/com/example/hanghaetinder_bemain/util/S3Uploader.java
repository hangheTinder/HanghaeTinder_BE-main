package com.example.hanghaetinder_bemain.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

// 이미지 파일업로드를 위한 s3 설정 파일입니다.
@RequiredArgsConstructor
@Service
public class S3Uploader {

	private final AmazonS3Client amazonS3Client;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// 멀티파트 파일을 받아와서 s3에 파일을 업로드 합니다. 업로드된 파일의 url을 반환합니다.
	public String upload(MultipartFile multipartFile) throws IOException {
		String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

		ObjectMetadata objMeta = new ObjectMetadata();
		objMeta.setContentLength(multipartFile.getSize());

		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objMeta)
			.withCannedAcl(CannedAccessControlList.PublicRead));

		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	// 해당 파일의 url을 받아와서 해당 파일을 s3에서 삭제합니다, 성공 여부를 반환합니다.
	public boolean delete(String fileUrl) {
		try {
			String[] temp = fileUrl.split("/");
			String fileKey = temp[temp.length-1];
			amazonS3Client.deleteObject(bucket, fileKey);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
