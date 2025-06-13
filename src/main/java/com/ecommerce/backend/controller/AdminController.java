package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.FavoriteDTO;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.dto.UserDTO;
import com.ecommerce.backend.model.AppSettings;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.ShippingStatus;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.AppSettingsService;
import com.ecommerce.backend.service.FavoriteService;
import com.ecommerce.backend.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final AppSettingsService appSettingsService;

    public AdminController(UserRepository userRepository,
                           FavoriteService favoriteService,
                           OrderService orderService,
                           AppSettingsService appSettingsService) {
        this.userRepository = userRepository;
        this.favoriteService = favoriteService;
        this.orderService = orderService;
        this.appSettingsService = appSettingsService;
    }

    @Operation(summary = "Admin profil bilgisi", description = "Giriş yapan adminin ismini döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin profil bilgisi getirildi"),
            @ApiResponse(responseCode = "401", description = "JWT token geçersiz veya eksik")
    })
    @GetMapping("/profile")
    public ResponseEntity<String> getAdminProfile(Authentication authentication) {
        return ResponseEntity.ok("Admin paneline hoş geldin, " + authentication.getName());
    }

    @Operation(summary = "Kargo durumunu güncelle", description = "Siparişin kargo durumunu değiştirir.")
    @PutMapping("/orders/{orderId}/shipping-status")
    public ResponseEntity<OrderResponse> updateShippingStatus(@PathVariable Long orderId,
                                                              @RequestParam String status) {
        ShippingStatus enumStatus = switch (status.toUpperCase()) {
            case "HAZIRLANIYOR" -> ShippingStatus.PENDING;
            case "KARGODA" -> ShippingStatus.SHIPPED;
            case "TESLIM_EDILDI" -> ShippingStatus.DELIVERED;
            default -> throw new IllegalArgumentException("Geçersiz durum: " + status);
        };

        Order updatedOrder = orderService.updateShippingStatus(orderId, enumStatus);
        return ResponseEntity.ok(orderService.mapToResponse(updatedOrder));
    }



    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites() {
        List<FavoriteDTO> dtos = favoriteService.getAllFavorites().stream()
                .map(fav -> new FavoriteDTO(
                        fav.getUser().getEmail(),
                        fav.getProduct().getName()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Tüm kullanıcıları getir", description = "Sistemdeki tüm kullanıcıları döner. Sadece admin erişebilir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı listesi getirildi"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole(),user.getCustomerType()))
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Admin dashboard", description = "Sistemin genel durumu hakkında örnek bilgi verir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard mesajı döndürüldü"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Admin dashboard: sistem çalışıyor!");
    }

    @Operation(summary = "Kullanıcı sayısı", description = "Toplam kayıtlı kullanıcı sayısını döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı sayısı başarıyla getirildi"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim")
    })
    @GetMapping("/user-count")
    public ResponseEntity<Long> getUserCount() {
        long count = userRepository.count();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Tüm siparişleri getir", description = "Sistemdeki tüm siparişleri admin olarak döner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sipariş listesi getirildi"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim")
    })
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForAdmin() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponse> responses = orders.stream()
                .map(orderService::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "İndirim oranını güncelle", description = "İndirim oranını günceller (örn: 0.15 = %15).")
    @PutMapping("/settings/discount")
    public ResponseEntity<AppSettings> updateDiscount(@RequestParam double rate) {
        return ResponseEntity.ok(appSettingsService.updateDiscountRate(rate));
    }

    @Operation(summary = "İndirim oranını getir", description = "Geçerli indirim oranını döner.")
    @GetMapping("/settings/discount")
    public ResponseEntity<Double> getDiscountRate() {
        return ResponseEntity.ok(appSettingsService.getSettings().getDiscountRate());
    }
}
