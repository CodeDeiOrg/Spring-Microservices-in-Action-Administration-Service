package com.onlinelibrary.administration.service.client;

import com.onlinelibrary.administration.entity.Book;
import com.onlinelibrary.administration.exception.ServiceDiscoveryNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookDiscoveryClient {
    private static final Logger logger = LoggerFactory.getLogger(BookDiscoveryClient.class);
    public static final String BOOK_SERVICE = "book-service";
    public static final String SERVICE_NOT_FOUND = "Service not found";
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    public BookDiscoveryClient(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public Book getBookById(Long bookId, String token) {
        logger.debug("Getting book with id: {} using service discovery", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        List<ServiceInstance> instances = discoveryClient.getInstances(BOOK_SERVICE);
        if (instances.isEmpty()) throw new ServiceDiscoveryNotFound(SERVICE_NOT_FOUND);
        
        String serviceUri = String.format("%s/api/books/%s", instances.get(0).getUri().toString(), bookId);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        httpEntity,
                        Book.class,
                        bookId);

        return restExchange.getBody();
    }

    public void saveBook(Book book, String token) {
        logger.debug("Saving book: {} using service discovery", book);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Book> httpEntity = new HttpEntity<>(book, httpHeaders);

        List<ServiceInstance> instances = discoveryClient.getInstances(BOOK_SERVICE);
        if (instances.isEmpty()) throw new ServiceDiscoveryNotFound(SERVICE_NOT_FOUND);

        String serviceUri = String.format("%s/api/books", instances.get(0).getUri().toString());

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.POST,
                        httpEntity,
                        Book.class);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while saving book: {}", book);
        }

        logger.debug("Book saved: {}.", book);
    }

    public void deleteBookReview(Long bookId, String authorization) {
        logger.debug("Deleting reviews for book with id: {} using service discovery", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);

        List<ServiceInstance> instances = discoveryClient.getInstances(BOOK_SERVICE);
        if (instances.isEmpty()) throw new ServiceDiscoveryNotFound(SERVICE_NOT_FOUND);

        String serviceUri = String.format("%s/api/books/%s", instances.get(0).getUri().toString(), bookId);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.DELETE,
                        httpEntity,
                        Book.class,
                        bookId);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while deleting book with id: {}!", bookId);
        }

        logger.debug("Successfully deleted all reviews for book: {}.", bookId);

    }

    public void deleteBook(Long bookId, String authorization) {
        logger.debug("Deleting book with id: {} using service discovery", bookId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        List<ServiceInstance> instances = discoveryClient.getInstances(BOOK_SERVICE);
        if (instances.isEmpty()) throw new ServiceDiscoveryNotFound(SERVICE_NOT_FOUND);

        String serviceUri = String.format("%s/api/books/%s", instances.get(0).getUri().toString(), bookId);

        ResponseEntity<Book> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.DELETE,
                        httpEntity,
                        Book.class,
                        bookId);

        if (!restExchange.getStatusCode().is2xxSuccessful()) {
            logger.debug("Something went wrong while deleting book with id {}!", bookId);
        }

        logger.debug("Successfully deleted all reviews for book: {}.", bookId);
    }
}
