package com.onlinelibrary.administration.service.client;

import com.onlinelibrary.administration.entity.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "book-service", url = "http://book-service:8180")
public interface BookFeignClient {
    @GetMapping(value = "/api/books/secure/{bookId}", consumes = "application/json")
    Book getBookById(@PathVariable("bookId") Long bookId, @RequestHeader("Authorization") String token);

    @PostMapping(value = "/api/books", consumes = "application/json")
    void saveBook(Book book, @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/api/books/secure/{bookId}", consumes = "application/json")
    void deleteBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/api/reviews/secure/{bookId}", consumes = "application/json")
    void deleteBookReview(@PathVariable Long bookId, @RequestHeader("Authorization") String token);
}
