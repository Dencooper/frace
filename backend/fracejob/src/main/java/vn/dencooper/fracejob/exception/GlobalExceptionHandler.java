package vn.dencooper.fracejob.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import vn.dencooper.fracejob.domain.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingUncaseException(RuntimeException exception) {
        ApiResponse res = new ApiResponse<>();
        res.setError("true");
        res.setStatusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        res.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ApiResponse res = new ApiResponse<>();
        ErrorCode errorCode = exception.getErrorCode();
        res.setError("true");
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
        res.setError("true");
        res.setStatusCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

}