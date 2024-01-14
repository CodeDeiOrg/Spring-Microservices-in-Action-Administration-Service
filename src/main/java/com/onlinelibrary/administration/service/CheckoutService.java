package com.onlinelibrary.administration.service;

import com.onlinelibrary.administration.entity.Checkout;
import com.onlinelibrary.administration.repository.CheckoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CheckoutService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final CheckoutRepository checkoutRepository;

    public CheckoutService(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    public Checkout findByUserEmailAndBookId(String userEmail, Long bookId) {
        logger.debug("Finding Checkout by User Email and Book ID");
        return checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
    }

    public List<Checkout> findBooksByUserEmail(String userEmail) {
        logger.debug("Finding Checkout List by User Email");
        return checkoutRepository.findBooksByUserEmail(userEmail);
    }

    public void createCheckout(Checkout checkout) {
        logger.debug("Creating Checkout");
        checkoutRepository.save(checkout);
    }

    public void deleteCheckoutById(Long checkoutId) {
        logger.debug("Deleting Checkout by ID");
        checkoutRepository.deleteById(checkoutId);
    }
}
