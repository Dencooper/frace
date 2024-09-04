package vn.dencooper.fracejob.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.LoginRequest;
import vn.dencooper.fracejob.domain.dto.response.LoginResponse;
import vn.dencooper.fracejob.domain.dto.response.LoginResponse.UserLogin;
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.JwtUtil;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthenticationManagerBuilder authenticationManagerBuilder;
    JwtUtil jwtUtil;
    UserService userService;

    @PostMapping("/login")
    @ApiMessage("Login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String access_token = jwtUtil.createJWT(authentication);

        User currrentUser = userService.fetchUserByEmail(request.getUsername());
        LoginResponse res = new LoginResponse();
        LoginResponse.UserLogin userLogin = res.new UserLogin(currrentUser.getId(), currrentUser.getEmail(),
                currrentUser.getFullName());

        res.setAccess_token(access_token);
        res.setUser(userLogin);
        return ResponseEntity.ok().body(res);
    }
}
