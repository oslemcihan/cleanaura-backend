package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Campaign;
import com.ecommerce.backend.repository.CampaignRepository;
import com.ecommerce.backend.service.CampaignService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Campaign create(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public Campaign update(Long id, Campaign updatedCampaign) {
        Campaign existing = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kampanya bulunamadı: " + id));

        existing.setTitle(updatedCampaign.getTitle());
        existing.setDescription(updatedCampaign.getDescription());
        existing.setActive(updatedCampaign.isActive());
        existing.setStartDate(updatedCampaign.getStartDate());
        existing.setEndDate(updatedCampaign.getEndDate());
        existing.setDiscountType(updatedCampaign.getDiscountType());
        existing.setDiscountValue(updatedCampaign.getDiscountValue());
        existing.setCategory(updatedCampaign.getCategory());
        existing.setProduct(updatedCampaign.getProduct());

        return campaignRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }

    @Override
    public List<Campaign> getAll() {
        return campaignRepository.findAll();
    }

    @Override
    public List<Campaign> getActiveCampaigns() {
        return campaignRepository.findByActiveTrue();
    }
}
