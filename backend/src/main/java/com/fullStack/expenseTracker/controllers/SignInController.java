package com.fullStack.expenseTracker.controllers;


import com.fullStack.expenseTracker.dto.reponses.JwtResponseDto;
import com.fullStack.expenseTracker.dto.requests.SignInRequestDto;
import com.fullStack.expenseTracker.security.UserDetailsImpl;
import com.fullStack.expenseTracker.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(originPatterns = {"http://localhost:3000", "https://*.vercel.app"})
@RestController
@RequestMapping("/mywallet/auth")
@Slf4j
public class SignInController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.user.profile.upload.dir}")
    private String userProfileUploadDir;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) throws IOException {
        log.info("Signin requested for email={}", signInRequestDto.getEmail());
        log.info("AuthenticationManager.authenticate start email={}", signInRequestDto.getEmail());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword()));
        } catch (RuntimeException ex) {
            log.error("AuthenticationManager.authenticate failed email={} error={}",
                    signInRequestDto.getEmail(), ex.getMessage());
            throw ex;
        }
        log.info("AuthenticationManager.authenticate success email={}", signInRequestDto.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("JWT generation start email={}", signInRequestDto.getEmail());
        String jwt;
        try {
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch (RuntimeException ex) {
            log.error("JWT generation failed email={} error={}", signInRequestDto.getEmail(), ex.getMessage());
            throw ex;
        }
        log.info("JWT generation success email={}", signInRequestDto.getEmail());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("Signin principal email={} enabled={} rolesCount={}",
                userDetails.getEmail(), userDetails.isEnabled(), roles.size());

        return ResponseEntity.ok(JwtResponseDto.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .token(jwt)
                .roles(roles)
                .build());
    }
}
