package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.model.AppSettings;
import com.ecommerce.backend.repository.AppSettingsRepository;
import com.ecommerce.backend.service.AppSettingsService;
import org.springframework.stereotype.Service;

@Service
public class AppSettingsServiceImpl implements AppSettingsService {

    private final AppSettingsRepository repository;

    public AppSettingsServiceImpl(AppSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public AppSettings getSettings() {
        return repository.findAll().stream().findFirst()
                .orElseGet(() -> repository.save(AppSettings.builder().discountRate(0.10).build()));
    }

    @Override
    public AppSettings updateDiscountRate(double newRate) {
        AppSettings settings = getSettings();
        settings.setDiscountRate(newRate);
        return repository.save(settings);
    }
}
