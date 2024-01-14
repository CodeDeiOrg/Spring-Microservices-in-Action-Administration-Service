package com.onlinelibrary.administration.controller;

import com.onlinelibrary.administration.entity.Payment;
import com.onlinelibrary.administration.exception.EmailMissingException;
import com.onlinelibrary.administration.requestmodel.PaymentInfoRequest;
import com.onlinelibrary.administration.service.PaymentService;
import com.onlinelibrary.administration.utils.ExtractJWT;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.onlinelibrary.administration.utils.Constants.EMAIL_MISSING;
import static com.onlinelibrary.administration.utils.Constants.SUB;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/secure/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
            throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentStr = paymentIntent.toJson();

        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }

    @PutMapping("/secure/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, SUB);
        if (userEmail == null) {
            throw new EmailMissingException(EMAIL_MISSING);
        }
        return paymentService.stripePayment(userEmail);
    }

    @PostMapping("/secure")
    public void createPaymentIntent(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
    }

    @GetMapping(value = "/secure/{userEmail}")
    public Payment findPaymentByUserEmail(@PathVariable String userEmail) {
        return paymentService.findPaymentByUserEmail(userEmail);
    }
}
