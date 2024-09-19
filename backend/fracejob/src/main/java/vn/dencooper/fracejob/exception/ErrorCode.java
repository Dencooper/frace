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
    NAME_INVALID(400, "Họ tên phải có tối thiếu 3 kí tự"),
    USER_NOTFOUND(400, "Không tìm thấy người dùng"),
    BAD_CREDENTIAL(400, "Email hoặc mật khẩu không đúng"),
    MISSING_COOKIE(400, "Cookie không được truyền"),
    REFRESH_TOKEN_INVALID(400, "Refresh Token không hợp lệ"),

    COMPANY_NOTFOUND(400, "Không tìm thấy công ty"),

    SKILL_NOTFOUND(400, "Không tìm thấy kĩ năng"),
    SKILL_EXISTED(400, "Kĩ năng đã tổn tại"),

    JOB_NOTFOUND(400, "Không tìm thấy công việc"),

    EMPTY_FILE(400, "File không được để trống"),
    MAX_FILESIZE(400, "Đã vượt quá kích thước tải lên tối đa(50MB)"),
    EXTENSIONS_FILE(400, "File phải có phần mở rộng thuộc [pdf, jpg, jpeg, png, doc, docx]"),

    EMAIL_NOTBLANK(400, "Email không được để trống"),
    URL_NOTBLANK(400, "URL không được để trống"),
    USERJOB_NOTEXISTED(400, "Người dùng hoặc công việc không tồn tại"),
    RESUME_NOTEXISTED(400, "Resume không tồn tại"),
    RESUME_NOTFOUND(400, "Không tìm thấy resume"),
    ;

    int code;
    String error;

}
