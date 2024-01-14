package com.onlinelibrary.administration.controller;

import com.onlinelibrary.administration.entity.Checkout;
import com.onlinelibrary.administration.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/secure/{userEmail}/{bookId}")
    public Checkout findByUserEmailAndBookId(@PathVariable String userEmail, @PathVariable Long bookId) {
        return checkoutService.findByUserEmailAndBookId(userEmail, bookId);
    }

    @GetMapping("/secure/{userEmail}")
    public List<Checkout> findBooksByUserEmail(@PathVariable String userEmail) {
        return checkoutService.findBooksByUserEmail(userEmail);
    }

    @PostMapping(value = {"/secure"})
    public void saveCheckout(@RequestBody Checkout checkout) {
        checkoutService.createCheckout(checkout);
    }

    @DeleteMapping("/secure/{checkoutId}")
    public void deleteCheckout(@PathVariable Long checkoutId) {
        checkoutService.deleteCheckoutById(checkoutId);
    }

}
