package com.example.groceries.auth.security;

import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
//        this.userRepository = userRepository;
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//        if (path.startsWith("/api/auth/") || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            chain.doFilter(request, response); // пропускаем фильтр
//            return;
//        }
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                String username = jwtUtil.extractUsername(token);
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    User user = userRepository.findByEmail(username)
//                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//                    CustomUserDetails userDetails = new CustomUserDetails(user);
//                    if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails))) {
//                        UsernamePasswordAuthenticationToken authenticationToken =
//                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    }
//                }
//            } catch (JwtException | UsernameNotFoundException e) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//        }
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") ||
                "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {

//            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            List<GrantedAuthority> authorities =
                    jwtUtil.extractAuthorities(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        } catch (JwtException e) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid JWT token"
            );
            return;
        }
        chain.doFilter(request, response);
    }
}
