package vn.dencooper.fracejob.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.login.LoginRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.response.login.LoginResponse;
import vn.dencooper.fracejob.domain.dto.response.login.LoginResponse.UserLoginReponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.JwtUtil;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
        PasswordEncoder passwordEncoder;
        AuthenticationManagerBuilder authenticationManagerBuilder;
        JwtUtil jwtUtil;
        UserService userService;

        @NonFinal
        @Value("${frace.jwt.refresh-token-validity-in-seconds}")
        long refreshTokenExpiration;

        @PostMapping("/register")
        @ApiMessage("Register")
        public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreationRequest req) {
                String hashPassword = passwordEncoder.encode(req.getPassword());
                req.setPassword(hashPassword);
                UserResponse res = userService.handleCreateUser(req);
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        @PostMapping("/login")
        @ApiMessage("Login")
        public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword());

                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                User currrentUser = userService.fetchUserByEmail(request.getUsername());
                LoginResponse res = new LoginResponse();
                UserLoginReponse userLogin = new UserLoginReponse(
                                currrentUser.getId(),
                                currrentUser.getEmail(),
                                currrentUser.getName(),
                                currrentUser.getRole());
                res.setUser(userLogin);

                String access_token = jwtUtil.createAccessToken(authentication.getName(), res);

                res.setAccessToken(access_token);

                String refresh_token = jwtUtil.createRefreshToken(request.getUsername(), res);
                userService.updateRefreshToken(request.getUsername(), refresh_token);

                ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                                .body(res);
        }

        @GetMapping("/account")
        @ApiMessage("Get account")
        public ResponseEntity<LoginResponse> fetchAccount() {
                String email = JwtUtil.getCurrentUserLogin().isPresent() ? JwtUtil.getCurrentUserLogin().get() : "";
                User currrentUser = userService.fetchUserByEmail(email);
                LoginResponse res = new LoginResponse();
                UserLoginReponse userLogin = new UserLoginReponse(
                                currrentUser.getId(),
                                currrentUser.getEmail(),
                                currrentUser.getName(),
                                currrentUser.getRole());
                res.setUser(userLogin);
                return ResponseEntity.ok().body(res);

        }

        @GetMapping("/refresh")
        @ApiMessage("Get account by refresh")
        public ResponseEntity<LoginResponse> fetchAccountByRefresh(
                        @CookieValue(name = "refresh_token") String refresh_token) {
                Jwt decodedToken = jwtUtil.checkRefreshToken(refresh_token);
                String email = decodedToken.getSubject();
                User currrentUser = userService.fetchUserByRefreshAndEmail(refresh_token, email);
                LoginResponse res = new LoginResponse();
                UserLoginReponse userLogin = new UserLoginReponse(
                                currrentUser.getId(),
                                currrentUser.getEmail(),
                                currrentUser.getName(),
                                currrentUser.getRole());
                res.setUser(userLogin);

                String access_token = jwtUtil.createAccessToken(email, res);

                res.setAccessToken(access_token);

                String new_refresh_token = jwtUtil.createRefreshToken(email, res);
                userService.updateRefreshToken(email, new_refresh_token);

                ResponseCookie resCookie = ResponseCookie.from("refresh_token", new_refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                                .body(res);

        }

        @PostMapping("/logout")
        @ApiMessage("Logout")
        public ResponseEntity<Void> logout() {
                String email = JwtUtil.getCurrentUserLogin().get();
                User currrentUser = userService.fetchUserByEmail(email);
                currrentUser.setRefreshToken("");
                userService.updateRefreshToken(email, null);
                ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                                .body(null);
        }
}
