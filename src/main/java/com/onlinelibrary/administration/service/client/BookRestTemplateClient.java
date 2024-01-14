package com.onlinelibrary.administration.service.client;

import com.onlinelibrary.administration.entity.Book;
import com.onlinelibrary.administration.repository.redis.BookRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BookRestTemplateClient {
    private static final Logger logger = LoggerFactory.getLogger(BookRestTemplateClient.class);
    private final RestTemplate restTemplate;
    private final BookRedisRepository redisRepository;

    public BookRestTemplateClient(RestTemplate restTemplate, BookRedisRepository redisRepository) {
        this.restTemplate = restTemplate;
        this.redisRepository = redisRepository;
    }

    public Book getBookById(Long bookId, String token) {
        logger.debug("Getting book with id: {}", bookId);

        Book book = checkRedisCache(bookId);

        if (book != null) {
            logger.debug("I have successfully retrieved an book {} from the redis cache: {}", bookId, book);
            return book;
        }

        logger.debug("Unable to locate book from the redis cache: {}.", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        "http://book-service/api/books/{bookId}",
                        HttpMethod.GET,
                        httpEntity,
                        Book.class,
                        bookId);

        /*Save the record from cache*/
        book = restExchange.getBody();

        if (book != null) {
            cacheBookObject(book);
        }

        return book;
    }

    public void saveBook(Book book, String token) {
        logger.debug("Saving book: {}.", book);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Book> httpEntity = new HttpEntity<>(book, httpHeaders);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        "http://book-service/api/books",
                        HttpMethod.POST,
                        httpEntity,
                        Book.class);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while saving book: {}", book);
        }

        logger.debug("Book saved: {}.", book);
    }

    public void deleteBookReview(Long bookId, String token) {
        logger.debug("Deleting reviews for book with id: {}.", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        "http://book-service/api/reviews/secure/{bookId}",
                        HttpMethod.DELETE,
                        httpEntity,
                        Book.class,
                        bookId);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while deleting book with id: {}!", bookId);
        }

        logger.debug("Successfully deleted all reviews for book: {}.", bookId);
    }

    public void deleteBook(Long bookId, String token) {
        logger.debug("Deleting book with id: {}.", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        "http://book-service/api/books/secure/{bookId}",
                        HttpMethod.DELETE,
                        httpEntity,
                        Book.class,
                        bookId);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while deleting book with id {}!", bookId);
        }

        logger.debug("Successfully deleted all reviews for book: {}.", bookId);
    }

    private Book checkRedisCache(Long bookId) {
        try {
            return redisRepository.findById(bookId).orElse(null);
        } catch (Exception ex) {
            logger.error("Error encountered while trying to retrieve book {} check Redis Cache.  Exception {}", bookId, ex.getMessage());
            return null;
        }
    }

    private void cacheBookObject(Book book) {
        try {
            redisRepository.save(book);
        } catch (Exception ex) {
            logger.error("Unable to cache book {} in Redis. Exception {}", book.getId(), ex.getMessage());
        }
    }
}
