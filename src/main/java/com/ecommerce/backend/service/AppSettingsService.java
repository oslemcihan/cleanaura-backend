package com.ecommerce.backend.service;

import com.ecommerce.backend.model.AppSettings;

public interface AppSettingsService {
    AppSettings getSettings();
    AppSettings updateDiscountRate(double newRate);
}
