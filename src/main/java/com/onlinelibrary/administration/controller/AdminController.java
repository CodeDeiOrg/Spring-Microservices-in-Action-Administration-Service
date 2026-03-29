package com.onlinelibrary.administration.controller;

import com.onlinelibrary.administration.exception.AdminControlException;
import com.onlinelibrary.administration.requestmodel.AddBookRequest;
import com.onlinelibrary.administration.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void increaseBookQuantity(@AuthenticationPrincipal Jwt jwt,
                                     @RequestParam Long bookId) {
        List<String> roles = jwt.getClaim(ROLES_CLAIM);
        if (roles == null || !roles.contains(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.increaseBookQuantity(bookId, "", "Bearer " + jwt.getTokenValue());
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@AuthenticationPrincipal Jwt jwt,
                                     @RequestParam Long bookId) {
        List<String> roles = jwt.getClaim(ROLES_CLAIM);
        if (roles == null || !roles.contains(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.decreaseBookQuantity(bookId, "", "Bearer " + jwt.getTokenValue());
    }

    @PostMapping("/secure/add/book")
    public void postBook(@AuthenticationPrincipal Jwt jwt,
                         @RequestBody AddBookRequest addBookRequest) {
        List<String> roles = jwt.getClaim(ROLES_CLAIM);
        if (roles == null || !roles.contains(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.postBook(addBookRequest, "", "Bearer " + jwt.getTokenValue());
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@AuthenticationPrincipal Jwt jwt,
                           @RequestParam Long bookId) {
        List<String> roles = jwt.getClaim(ROLES_CLAIM);
        if (roles == null || !roles.contains(ADMIN)) {
            throw new AdminControlException(ADMINISTRATION_PAGE_ONLY);
        }
        adminService.deleteBook(bookId, "", "Bearer " + jwt.getTokenValue());
    }

}