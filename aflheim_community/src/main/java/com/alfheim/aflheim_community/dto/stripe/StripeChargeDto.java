package com.alfheim.aflheim_community.dto.stripe;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class StripeChargeDto {

    private String stripeToken;
    private String username;
    private Double amount;
    private boolean success;
    private String message;
    private String chargeId;
    private Map<String, Object> additionalInfo = new HashMap<>();
}
