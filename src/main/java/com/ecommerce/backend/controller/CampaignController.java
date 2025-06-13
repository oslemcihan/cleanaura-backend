package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Campaign;
import com.ecommerce.backend.service.CampaignService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@SecurityRequirement(name = "bearerAuth")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Campaign> create(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.create(campaign));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Campaign> update(@PathVariable Long id, @RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.update(id, campaign));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        campaignService.delete(id);
        return ResponseEntity.ok("Kampanya silindi.");
    }

    @GetMapping
    public ResponseEntity<List<Campaign>> getAll() {
        return ResponseEntity.ok(campaignService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Campaign>> getActive() {
        return ResponseEntity.ok(campaignService.getActiveCampaigns());
    }
}
