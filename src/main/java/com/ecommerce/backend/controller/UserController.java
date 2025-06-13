package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.UserDTO;
import com.ecommerce.backend.dto.UserRegisterDTO;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Kullanıcının karşılama mesajı", description = "Giriş yapan kullanıcının ismini döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil bilgisi getirildi"),
            @ApiResponse(responseCode = "401", description = "JWT token geçersiz")
    })
    @GetMapping("/profile")
    public String getProfile(Authentication authentication) {
        return "Hoş geldin, " + authentication.getName();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCustomerType()
        ));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Kullanıcı dashboard'a hoş geldin!";
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, Authentication authentication) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCustomerType()
        ));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody @Valid UserRegisterDTO dto) {
        User updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(new UserDTO(
                updated.getId(),
                updated.getEmail(),
                updated.getRole(),
                updated.getCustomerType()
        ));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Kullanıcı silindi.");
    }

    @PutMapping("/update-by-token")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateByToken(@RequestBody @Valid UserRegisterDTO dto,
                                                 Authentication authentication) {
        String email = authentication.getName();
        User updated = userService.updateUserByEmail(email, dto);
        return ResponseEntity.ok(new UserDTO(
                updated.getId(),
                updated.getEmail(),
                updated.getRole(),
                updated.getCustomerType()
        ));
    }
}
