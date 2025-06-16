package com.grepp.datenow.infra.error;

import com.grepp.datenow.infra.error.exception.course.BadWordsException;
import com.grepp.datenow.infra.error.exception.image.ImageUploadException;
import com.grepp.datenow.infra.error.exception.image.InvalidFileFormatException;
import com.grepp.datenow.infra.response.ApiResponse;
import com.grepp.datenow.infra.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice(basePackages = "com.grepp.datenow.app.controller.api")
@Slf4j
public class RestApiExceptionAdvice {

    // 여기부터 Image Upload 관련
    // Image 유효성 검사
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidFileFormatException(InvalidFileFormatException e) {
        return ResponseEntity
            .status(ResponseCode.BAD_REQUEST.status()) // 400 Bad Request
            .body(ApiResponse.fail(ResponseCode.BAD_REQUEST, e.getMessage()));
    }

    // Image 크기 제한
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity
            .status(ResponseCode.PAYLOAD_TOO_LARGE.status()) // 413 Payload Too Large
            .body(ApiResponse.fail(ResponseCode.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."));
    }

    // 업로드 중 발생한 서버 오류
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ApiResponse<?>> handleImageUploadException(ImageUploadException e) {
        return ResponseEntity
            .status(ResponseCode.INTERNAL_SERVER_ERROR.status()) // 500 Internal Server Error
            .body(ApiResponse.fail(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
    // 여기까지 Image Upload 관련

    // 여기부터 나쁜말 필터링
    @ExceptionHandler(BadWordsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadWordsException(BadWordsException e) {
        return ResponseEntity
            .status(ResponseCode.BAD_WORD.status())
            .body(ApiResponse.fail(ResponseCode.BAD_WORD, e.getMessage()));
    }


}
