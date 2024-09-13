package vn.dencooper.fracejob.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import vn.dencooper.fracejob.domain.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingUncaseException(RuntimeException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setError("true 1");
        res.setStatusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        res.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { InternalAuthenticationServiceException.class })
    public ResponseEntity<ApiResponse> handlingBadCredentialsException(
            InternalAuthenticationServiceException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setError("true 2");
        res.setStatusCode(ErrorCode.BAD_CREDENTIAL.getCode());
        res.setMessage(ErrorCode.BAD_CREDENTIAL.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse res = new ApiResponse<>();
        res.setError("true 3");
        res.setStatusCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());
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
        res.setError("true 4");
        res.setStatusCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = { MissingRequestCookieException.class })
    public ResponseEntity<ApiResponse> handlingMissingRequestCookieException(
            MissingRequestCookieException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setError("true 5");
        res.setStatusCode(ErrorCode.MISSING_COOKIE.getCode());
        res.setMessage(ErrorCode.MISSING_COOKIE.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

}