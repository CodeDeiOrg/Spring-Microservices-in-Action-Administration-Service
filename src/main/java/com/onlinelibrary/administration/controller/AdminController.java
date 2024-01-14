package com.onlinelibrary.administration.controller;

import com.onlinelibrary.administration.exception.AdminControlException;
import com.onlinelibrary.administration.requestmodel.AddBookRequest;
import com.onlinelibrary.administration.service.AdminService;
import com.onlinelibrary.administration.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.onlinelibrary.administration.utils.Constants.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) {
        String admin = ExtractJWT.payloadJWTExtraction(token, USER_TYPE);
        if (admin == null || !admin.equals(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.increaseBookQuantity(bookId, "", token);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) {
        String admin = ExtractJWT.payloadJWTExtraction(token, USER_TYPE);
        if (admin == null || !admin.equals(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.decreaseBookQuantity(bookId, "", token);
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token,
                         @RequestBody AddBookRequest addBookRequest) {
        String admin = ExtractJWT.payloadJWTExtraction(token, USER_TYPE);
        if (admin == null || !admin.equals(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.postBook(addBookRequest, "", token);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) {
        String admin = ExtractJWT.payloadJWTExtraction(token, USER_TYPE);
        if (admin == null || !admin.equals(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.deleteBook(bookId, "", token);
    }

}
