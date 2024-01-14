package com.onlinelibrary.administration.service;

import com.onlinelibrary.administration.entity.Book;
import com.onlinelibrary.administration.exception.BookException;
import com.onlinelibrary.administration.repository.CheckoutRepository;
import com.onlinelibrary.administration.requestmodel.AddBookRequest;
import com.onlinelibrary.administration.service.client.BookDiscoveryClient;
import com.onlinelibrary.administration.service.client.BookFeignClient;
import com.onlinelibrary.administration.service.client.BookRestTemplateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    public static final String FEIGN = "feign";
    public static final String REST = "rest";
    public static final String DISCOVERY = "discovery";
    public static final String FEIGN_CLIENT = "I am using the feign client";
    public static final String REST_CLIENT = "I am using the rest client";
    public static final String DISCOVERY_CLIENT = "I am using the discovery client";
    private final BookFeignClient bookFeignClient;
    private final BookRestTemplateClient bookRestClient;
    private final BookDiscoveryClient bookDiscoveryClient;
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public AdminService(BookFeignClient bookFeignClient,
                        BookRestTemplateClient bookRestClient,
                        BookDiscoveryClient bookDiscoveryClient,
                        CheckoutRepository checkoutRepository) {
        this.bookFeignClient = bookFeignClient;
        this.bookRestClient = bookRestClient;
        this.bookDiscoveryClient = bookDiscoveryClient;
        this.checkoutRepository = checkoutRepository;
    }

    public void increaseBookQuantity(Long bookId, String clientType, String authorization) {
        logger.debug("Increasing Book Quantity");

        Book book = getBookInfo(bookId, clientType, authorization);

        if (book == null) {
            throw new BookException("Book not found");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        book.setCopies(book.getCopies() + 1);

        saveBookInfo(book, clientType, authorization);
    }

    public void decreaseBookQuantity(Long bookId, String clientType, String authorization) {
        logger.debug("Decreasing Book Quantity");

        Book book = getBookInfo(bookId, clientType, authorization);

        if (book == null || book.getCopiesAvailable() <= 0 || book.getCopies() <= 0) {
            throw new BookException("Book not found or quantity locked");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        book.setCopies(book.getCopies() - 1);

        saveBookInfo(book, clientType, authorization);
    }

    public void postBook(AddBookRequest addBookRequest, String clientType, String authorization) {
        logger.debug("Posting Book");

        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());

        saveBookInfo(book, clientType, authorization);
    }

    public void deleteBook(Long bookId, String clientType, String authorization) {
        logger.debug("Deleting Book");

        Book book = getBookInfo(bookId, clientType, authorization);

        if (book == null) {
            throw new BookException("Book not found");
        }

        deleteBookInfo(bookId, clientType, authorization);
        checkoutRepository.deleteAllByBookId(bookId);
        deleteAllReviewsByBookId(bookId, clientType, authorization);
    }

    private Book getBookInfo(Long bookId, String clientType, String token) {

        return switch (clientType) {
            case FEIGN -> {
                logger.debug(FEIGN_CLIENT);
                yield bookFeignClient.getBookById(bookId, token);
            }
            case REST -> {
                logger.debug(REST_CLIENT);
                yield bookRestClient.getBookById(bookId, token);
            }
            case DISCOVERY -> {
                logger.debug(DISCOVERY_CLIENT);
                yield bookDiscoveryClient.getBookById(bookId, token);
            }
            default -> bookRestClient.getBookById(bookId, token);
        };
    }


    private void saveBookInfo(Book book, String clientType, String authorization) {
        switch (clientType) {
            case FEIGN -> {
                logger.debug(FEIGN_CLIENT);
                bookFeignClient.saveBook(book, authorization);
            }
            case REST -> {
                logger.debug(REST_CLIENT);
                bookRestClient.saveBook(book, authorization);
            }
            case DISCOVERY -> {
                logger.debug(DISCOVERY_CLIENT);
                bookDiscoveryClient.saveBook(book, authorization);
            }
            default -> bookRestClient.saveBook(book, authorization);
        }
    }

    private void deleteBookInfo(Long book, String clientType, String authorization) {
        switch (clientType) {
            case FEIGN -> {
                logger.debug(FEIGN_CLIENT);
                bookFeignClient.deleteBook(book, authorization);
            }
            case REST -> {
                logger.debug(REST_CLIENT);
                bookRestClient.deleteBook(book, authorization);
            }
            case DISCOVERY -> {
                logger.debug(DISCOVERY_CLIENT);
                bookDiscoveryClient.deleteBook(book, authorization);
            }
            default -> bookRestClient.deleteBook(book, authorization);
        }
    }

    private void deleteAllReviewsByBookId(Long bookId, String clientType, String authorization) {
        switch (clientType) {
            case FEIGN -> {
                logger.debug(FEIGN_CLIENT);
                bookFeignClient.deleteBookReview(bookId, authorization);
            }
            case REST -> {
                logger.debug(REST_CLIENT);
                bookRestClient.deleteBookReview(bookId, authorization);
            }
            case DISCOVERY -> {
                logger.debug(DISCOVERY_CLIENT);
                bookDiscoveryClient.deleteBookReview(bookId, authorization);
            }
            default -> bookRestClient.deleteBookReview(bookId, authorization);
        }
    }

}
