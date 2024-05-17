package com.alfheim.aflheim_community.service.stripe;

import com.alfheim.aflheim_community.dto.stripe.PaymentOperationDto;
import com.alfheim.aflheim_community.dto.stripe.StripeChargeDto;
import com.alfheim.aflheim_community.dto.stripe.StripeTokenDto;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;

import javax.annotation.PostConstruct;

public interface StripeService {

    @PostConstruct
    public void init();
    StripeTokenDto createCardToken(StripeTokenDto stripeTokenDto);
    StripeChargeDto charge(StripeChargeDto chargeReq);

    StripeChargeDto doTransaction(String Tier, StripeTokenDto tokenDto, UserDto user, Double amount);

    PaymentOperationDto getOperationDetails(String OperationId);
}
