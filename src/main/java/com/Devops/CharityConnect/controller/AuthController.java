package com.Devops.CharityConnect.controller;
import com.Devops.CharityConnect.service.AuthService;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController (AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        System.out.println("Inside AuthController register method");
        ResponseEntity<Map<String, String>> result = authService.registerUser(request);

        // Return the response as-is (success or failure)
        return result;
    }


    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return ResponseEntity.ok("Login successful for UID: " + decodedToken.getUid());
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        System.out.println("Server is Running live");
        return ResponseEntity.ok("Server is Live");
    }
}
