package com.Devops.CharityConnect.service;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public ResponseEntity<Map<String, String>> registerUser(Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            // Extract user details
            System.out.println("inside registration of the user");
            String firstName = request.get("firstName");
            String lastName = request.get("lastName");
            String email = request.get("email");
            String username = request.get("username");
            String password = request.get("password");
            String confirmPassword = request.get("confirmPassword");
            String mobileNo = request.get("mobileNo");
            String address = request.get("address");

            // Validate input fields
            if (firstName == null || lastName == null || email == null || username == null ||
                    password == null || confirmPassword == null || mobileNo == null || address == null) {
                response.put("error", "All fields are required.");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Validate password match
            if (!password.equals(confirmPassword)) {
                response.put("error", "Passwords do not match.");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Register user in Firebase Authentication
            UserRecord.CreateRequest userRequest = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(firstName + " " + lastName)
                    .setPhoneNumber("+1" + mobileNo);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(userRequest);

            // ✅ Save additional user details in Firestore
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);
            userData.put("email", email);
            userData.put("username", username);
            userData.put("mobileNo", mobileNo);
            userData.put("address", address);
            userData.put("firebaseUid", userRecord.getUid());

            db.collection("users").document(userRecord.getUid()).set(userData);

            response.put("message", "User registered successfully.: " + userRecord.getUid());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", e.getMessage()); // ✅ Fixed JSON formatting issue
            return ResponseEntity.badRequest().body(response);
        }
    }

    public boolean loginUser(String email, String password) {
        // Firebase Authentication does not support password verification directly.
        // Instead, frontend should verify and send the ID token to backend.
        return true;
    }
}

