package vn.dencooper.fracejob.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import vn.dencooper.fracejob.domain.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingUncaseException(RuntimeException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        res.setError(ErrorCode.UNCATEGORIZED_EXCEPTION.getError());
        res.setMessage("Internal Server Error");
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { BadCredentialsException.class })
    public ResponseEntity<ApiResponse> handlingBadCredentialsException(
            BadCredentialsException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(ErrorCode.BAD_CREDENTIAL.getCode());
        res.setError(ErrorCode.BAD_CREDENTIAL.getError());
        res.setMessage("Đăng nhập không thành công");
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { InternalAuthenticationServiceException.class })
    public ResponseEntity<ApiResponse> handlingInternalAuthenticationServiceException(
            InternalAuthenticationServiceException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(ErrorCode.BAD_CREDENTIAL.getCode());
        res.setError(ErrorCode.BAD_CREDENTIAL.getError());
        res.setMessage("Đăng nhập không thành công");
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(errorCode.getCode());
        res.setError(errorCode.getError());
        res.setMessage(errorCode.getCode() >= 500 ? "Internal Server Error" : "Bad Request");
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
        }

        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(errorCode.getCode());
        res.setError(errorCode.getError());
        res.setMessage("Bad Request");
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { MissingRequestCookieException.class })
    public ResponseEntity<ApiResponse> handlingMissingRequestCookieException(
            MissingRequestCookieException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(ErrorCode.MISSING_COOKIE.getCode());
        res.setError(ErrorCode.MISSING_COOKIE.getError());
        res.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ResponseEntity<ApiResponse> handlingMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setStatusCode(ErrorCode.MAX_FILESIZE.getCode());
        res.setError(ErrorCode.MAX_FILESIZE.getError());
        res.setMessage("Bad Request");

        return ResponseEntity.badRequest().body(res);
    }

}