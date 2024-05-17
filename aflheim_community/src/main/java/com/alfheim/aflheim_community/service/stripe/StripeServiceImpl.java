package com.alfheim.aflheim_community.service.stripe;

import com.alfheim.aflheim_community.dto.stripe.PaymentOperationDto;
import com.alfheim.aflheim_community.dto.stripe.StripeChargeDto;
import com.alfheim.aflheim_community.dto.stripe.StripeTokenDto;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.exception.stripe.InvalidChargeAmountException;
import com.alfheim.aflheim_community.model.payment.PaymentOperation;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.PaymentOperationRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${api.stripe.publicKey}")
    private String stripeApiKey;

    @Value("${api.stripe.secretKey}")
    private String stripeApiSecretKey;

    @Autowired
    private PaymentOperationRepo paymentOperationRepo;

    @PostConstruct
    @Override
    public void init() {

        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public StripeTokenDto createCardToken(StripeTokenDto stripeTokenDto) {

        Stripe.apiKey = stripeApiKey;

        // Generating a card charge token "Once usage"
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", stripeTokenDto.getCardNumber());
            card.put("exp_month", Integer.parseInt(stripeTokenDto.getExpMonth()));
            card.put("exp_year", Integer.parseInt(stripeTokenDto.getExpYear()));
            card.put("cvc", stripeTokenDto.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);

            if (token != null && token.getId() != null) {
                stripeTokenDto.setSuccess(true);
                stripeTokenDto.setToken(token.getId());
            }

            return stripeTokenDto;

        } catch (StripeException e) {
            log.error("StripeException Service (createdToken)", e);
            // TODO : HANDLE THE EXCEPTION
            return null;
        }
    }

    @Override
    public StripeChargeDto charge(StripeChargeDto chargeReq) {

        Stripe.apiKey = stripeApiSecretKey;

        try {
            chargeReq.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeReq.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for id " + chargeReq.getAdditionalInfo().getOrDefault("ID_TAG", ""));
            chargeParams.put("source", chargeReq.getStripeToken());

            // Meta data
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeReq.getChargeId());
            metaData.putAll(chargeReq.getAdditionalInfo());
            chargeParams.put("metadata", metaData);

            Charge charge = Charge.create(chargeParams);
            chargeReq.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                // Successful charge
                chargeReq.setChargeId(charge.getId());
                chargeReq.setSuccess(true);
            }

            return chargeReq;

        } catch (StripeException e) {
            log.error("StripeException Service (charge)", e);
            // TODO : HANDLE THE EXCEPTION
            return null;
        }
    }

    @Override
    public StripeChargeDto doTransaction(String tier, StripeTokenDto tokenDto, UserDto user, Double chargeAmount) {

        // Generate transaction token
        StripeTokenDto token = createCardToken(tokenDto);

        System.out.println("1111111111");
        Double amount = 0.0;
        if (tier.equals("MEMBER")) {
            amount = 2.0;
        } else if (tier.equals("SUPPORT")) {
            System.out.println("33333333333333");
            amount = chargeAmount;
        }

        if (tier.equals("MEMBER") && amount == null) {
            // Invalid amount
            throw new InvalidChargeAmountException("Invalid payment charge amount");
        }

        if (amount <= 0) {
            // Invalid amount "0 or less"
            throw new InvalidChargeAmountException("Payment must be greater than $0.00");
        }

        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("ID_TAG", user.getId());
        additionalInfo.put("Email", user.getEmail());

        // Build charge request
        StripeChargeDto chargeDto = StripeChargeDto.builder()
                .stripeToken(token.getToken())
                .username(user.getUsername())
                .amount(amount)
                .additionalInfo(additionalInfo)
                .build();

        // Create charge req
        StripeChargeDto chargeResp = charge(chargeDto);

        // Saving transaction details
        PaymentOperation paymentOperation = PaymentOperation.builder()
                .stripeToken(chargeResp.getStripeToken())
                .accountUsername(chargeResp.getUsername())
                .email(chargeResp.getAdditionalInfo().get("Email").toString())
                .amount(chargeResp.getAmount())
                .message(chargeResp.getMessage())
                .chargeId(chargeResp.getChargeId())
                .success(chargeResp.isSuccess())
                .build();

        paymentOperationRepo.save(paymentOperation);

        // Returning resp
        return chargeResp;
    }

    @Override
    public PaymentOperationDto getOperationDetails(String chargeId) {
        Optional<PaymentOperation> paymentOperationOptional = paymentOperationRepo.findByChargeId(chargeId);

        // TODO: CATCH THE ERROR
        return PaymentOperationDto.from(paymentOperationOptional.get());
    }

}
