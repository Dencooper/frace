package vn.dencooper.fracejob.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // Uncategoried exception
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized exception"),
    INVALID_KEY(500, "Invalid message key"),

    // Exception in user module
    EMAIL_EXISTED(400, "Email đã tổn tại"),
    EMAIL_INVALID(400, "Email không hợp lệ"),
    PASSWORD_INVALID(400, "Mật khẩu phải có tối thiểu 6 kí tự"),
    NAME_INVALID(400, "Họ tên phải có tối thiếu 3 kí tự"),
    USER_NOTFOUND(400, "Không tìm thấy người dùng"),

    // Exception in auth module
    BAD_CREDENTIAL(400, "Email hoặc mật khẩu không đúng"),
    MISSING_COOKIE(400, "Bạn chưa đăng nhập"),
    REFRESH_TOKEN_INVALID(401, "Refresh Token không hợp lệ"),

    // Exception in company module
    COMPANY_NOTFOUND(400, "Không tìm thấy công ty"),

    // Exception in skill module
    SKILL_NOTFOUND(400, "Không tìm thấy kĩ năng"),
    SKILL_EXISTED(400, "Kĩ năng đã tổn tại"),

    // Exception in job module
    JOB_NOTFOUND(400, "Không tìm thấy công việc"),

    // Exception in file module
    EMPTY_FILE(400, "File không được để trống"),
    MAX_FILESIZE(400, "Đã vượt quá kích thước tải lên tối đa(50MB)"),
    EXTENSIONS_FILE(400, "File phải có phần mở rộng thuộc [pdf, jpg, jpeg, png, doc, docx]"),

    // Exception in resume module
    EMAIL_NOTBLANK(400, "Email không được để trống"),
    URL_NOTBLANK(400, "URL không được để trống"),
    USERJOB_NOTEXISTED(400, "Người dùng hoặc công việc không tồn tại"),
    RESUME_NOTEXISTED(400, "Resume không tồn tại"),
    RESUME_NOTFOUND(400, "Không tìm thấy resume"),

    // Exception in permission module
    PERMISSION_EXISTED(400, "Permission đã tốn tại"),
    PERMISSION_NOTFOUND(400, "Không tìm thấy Permission"),

    // Exception in role module
    NAME_NOTBLANK(400, "Tên không được để trống"),
    APIPATH_NOTBLANK(400, "Đường link API không được để trống"),
    METHOD_NOTBLANK(400, "Phương thức không được để trống"),
    MODULE_NOTBLANK(400, "Tính năng không được để trống"),
    ROLE_EXISTED(400, "Role đã tốn tại"),
    ROLE_NOTFOUND(400, "Không tìm thấy Role"),

    REQUEST_NOTALLOW(403, "Bạn không được phép truy cập trang này"),
    ;

    int code;
    String error;
}
