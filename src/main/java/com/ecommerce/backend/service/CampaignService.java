package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Campaign;

import java.util.List;

public interface CampaignService {
    Campaign create(Campaign campaign);
    Campaign update(Long id, Campaign campaign);
    void delete(Long id);
    List<Campaign> getAll();
    List<Campaign> getActiveCampaigns();
}
