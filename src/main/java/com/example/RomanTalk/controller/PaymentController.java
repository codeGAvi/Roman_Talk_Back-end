package com.example.RomanTalk.controller;

import com.example.RomanTalk.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Step-1 Create order — Razorpay pe
    @PostMapping("/create-order")
    public Map<String, String> createOrder(@RequestBody Map<String, String> body) throws Exception {
        String email = body.get("email");
        return paymentService.createOrder(email);
    }

    // Step-2 Verify payment — Razorpay se response aane pe
    @PostMapping("/verify")
    public Map<String, String> verifyPayment(@RequestBody Map<String, String> body) throws Exception {
        String orderId = body.get("orderId");
        String paymentId = body.get("paymentId");
        String signature = body.get("signature");
        String email = body.get("email");
        return paymentService.verifyPayment(orderId, paymentId, signature, email);
    }
}