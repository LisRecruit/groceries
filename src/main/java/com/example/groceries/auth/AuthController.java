package com.example.groceries.auth;

import com.example.groceries.auth.security.JwtUtil;
import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserService;
import com.example.groceries.auth.user.dtos.UserCreateRequest;
import com.example.groceries.auth.user.dtos.UserLoginRequest;
import com.example.groceries.auth.user.dtos.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login (@RequestBody UserLoginRequest request) {
        User user = userService.getUserByEmail(request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        Long userId = user.getId();
        String token = jwtUtil.generateToken(userDetails, userId);
        return ResponseEntity.ok(new UserLoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
