package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByActiveTrue();
}
