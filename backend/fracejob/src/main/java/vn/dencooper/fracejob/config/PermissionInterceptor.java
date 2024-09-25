package vn.dencooper.fracejob.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.dencooper.fracejob.domain.Permission;
import vn.dencooper.fracejob.domain.Role;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.JwtUtil;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        User user = userService.fetchUserByEmail(email);
        Role role = user.getRole();
        if (role != null) {
            List<Permission> permissions = role.getPermissions();
            boolean isAllow = permissions
                    .stream()
                    .anyMatch(permission -> permission.getApiPath().equals(path) &&
                            permission.getMethod().equals(httpMethod));
            if (!isAllow) {
                throw new AppException(ErrorCode.REQUEST_NOTALLOW);
            }
        } else {
            throw new AppException(ErrorCode.REQUEST_NOTALLOW);
        }

        return true;
    }
}
