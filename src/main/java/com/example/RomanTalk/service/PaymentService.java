package com.example.RomanTalk.service;

import com.example.RomanTalk.entity.User;
import com.example.RomanTalk.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final UserRepository userRepository;

    public PaymentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> createOrder(String email) throws Exception {



        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", 9900); // ₹99 → paise mein (99 * 100)
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + email);

        Order order = client.orders.create(orderRequest);

        Map<String, String> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", "9900");
        response.put("currency", "INR");
        response.put("keyId", keyId);
        return response;
    }

    // Signature verify karo — fraud check
    public Map<String, String> verifyPayment(
            String orderId, String paymentId,
            String signature, String email) throws Exception {

        // HMAC SHA256 calculation
        String payload = orderId + "|" + paymentId;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(
                keySecret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(payload.getBytes());

        // Hex convert
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        Map<String, String> response = new HashMap<>();

        if (hexString.toString().equals(signature)) {
            // Payment genuine — mark user as Pro
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPro(true);
            user.setProActivatedAt(LocalDateTime.now());
            userRepository.save(user);

            response.put("status", "success");
            response.put("message", "Payment verified! You are now Pro!");
        } else {
            response.put("status", "failed");
            response.put("message", "Payment verification failed!");
        }

        return response;
    }
}