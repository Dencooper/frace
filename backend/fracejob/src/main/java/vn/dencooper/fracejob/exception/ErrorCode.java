package vn.dencooper.fracejob.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized exception"),
    INVALID_KEY(500, "Invalid message key"),

    EMAIL_EXISTED(400, "Email đã tổn tại"),
    EMAIL_INVALID(400, "Email không hợp lệ"),
    PASSWORD_INVALID(400, "Mật khẩu phải có tối thiểu 6 kí tự"),
    FULLNAME_INVALID(400, "Họ tên phải có tối thiếu 3 kí tự"),
    USER_NOTFOUND(400, "Không tìm thấy người dùng"),

    COMPANY_NOTFOUND(400, "Không tìm thấy công ty"),
    ;

    private int code;
    private String message;

}
