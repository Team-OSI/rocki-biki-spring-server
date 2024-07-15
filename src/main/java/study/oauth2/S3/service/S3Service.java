package study.oauth2.S3.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.exception.Error.ErrorCode;
import study.oauth2.exception.Error.S3Exception;
import study.oauth2.user.domain.entity.FileType;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;

	public String upload(MultipartFile file, FileType fileType) {
		if(file.isEmpty() || Objects.isNull(file.getOriginalFilename())){
			throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
		}
		return this.uploadFile(file, fileType);
	}

	private String uploadFile(MultipartFile file, FileType fileType) {
		this.validateFileExtension(file.getOriginalFilename(), fileType);
		try {
			return this.uploadFileToS3(file, fileType);
		} catch (IOException e) {
			throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_FILE_UPLOAD);
		}
	}

	private void validateFileExtension(String filename, FileType fileType) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new S3Exception(ErrorCode.NO_FILE_EXTENSION);
		}

		String extension = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtensionList;

		switch (fileType) {
			case IMAGE:
				allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");
				break;
			case SOUND:
				allowedExtensionList = Arrays.asList("mp3", "wav", "ogg", "webm");
				break;
			default:
				throw new S3Exception(ErrorCode.INVALID_FILE_TYPE);
		}

		if (!allowedExtensionList.contains(extension)) {
			throw new S3Exception(ErrorCode.INVALID_FILE_EXTENSION);
		}
	}

	private String uploadFileToS3(MultipartFile file, FileType fileType) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String s3FileName = fileType.getDirectory() + "/" + UUID.randomUUID().toString().substring(0, 10) + originalFilename;

		InputStream is = file.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead);
			amazonS3.putObject(putObjectRequest);
		} catch (Exception e) {
			throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
		} finally {
			byteArrayInputStream.close();
			is.close();
		}

		return amazonS3.getUrl(bucketName, s3FileName).toString();
	}

	public void deleteImageFromS3(String address){
		String key = getKeyFromImageAddress(address);
		try{
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		}catch (Exception e){
			throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	private String getKeyFromImageAddress(String imageAddress){
		try{
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
			return decodingKey.substring(1); // 맨 앞의 '/' 제거
		}catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	public String update(String oldFileUrl, MultipartFile newFile, FileType fileType) {
		deleteImageFromS3(oldFileUrl);
		return upload(newFile, fileType);
	}
}
