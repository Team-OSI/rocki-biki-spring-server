package study.oauth2.exception.Error;

public enum ErrorCode {
	EMPTY_FILE_EXCEPTION("파일이 비었습니다."),
	IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 오류가 발생했습니다."),
	NO_FILE_EXTENSION("파일 확장자가 없습니다."),
	INVALID_FILE_EXTENSION("허용되지 않는 파일 확장자입니다."),
	PUT_OBJECT_EXCEPTION("S3에 파일을 업로드하는 중 오류가 발생했습니다."),
	DELETE_OBJECT_EXCEPTION("S3에 파일을 삭제하는 중 오류가 발생했습니다."),
	GET_OBJECT_EXCEPTION("S3에 파일을 가져오는 중 오류가 발생했습니다."),
	NO_SUCH_BUCKET_EXCEPTION("존재하지 않는 버킷입니다."),
	IO_EXCEPTION_ON_IMAGE_DELETE("이미지 삭제 중 오류가 발생했습니다."),;

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
