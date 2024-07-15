package com.example.db_calendar.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
    {
        System.out.println("register"+request.toString());
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request)
    {
        System.out.println("authenticate"+request);
        AuthenticationResponse response=service.authenticate(request);
        System.out.println("response"+response);
        if (Objects.equals(response.getStatus(), "OK")){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.ok(response);
        }
    }
}
