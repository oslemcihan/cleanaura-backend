package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AuthRequest;
import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.UserDTO;
import com.ecommerce.backend.dto.UserRegisterDTO;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.security.JwtUtil;
import com.ecommerce.backend.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Operation(summary = "Kullanıcı girişi (Login)", description = "Email ve şifre ile giriş yapar ve JWT token döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Giriş başarılı, JWT token döndü"),
            @ApiResponse(responseCode = "401", description = "Geçersiz giriş bilgileri")
    })
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token);
    }

    @Operation(summary = "Kayıt ol", description = "Yeni kullanıcıyı e-posta ve şifreyle kaydeder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kayıt başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi (validasyon hatası)")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserRegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        user.setCustomerType(dto.getCustomerType());

        User created = userService.registerUser(user);
        return ResponseEntity.ok(new UserDTO(created.getId(), created.getEmail(), created.getRole(),created.getCustomerType()));
    }

}
