package com.alfheim.aflheim_community.dto.stripe;

import com.alfheim.aflheim_community.model.payment.PaymentOperation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentOperationDto {

    private Long id;
    private String stripeToken;
    private String accountUsername;
    private String email;
    private Double amount;
    private boolean success;
    private String message;
    private String chargeId;

    public static PaymentOperationDto from(PaymentOperation paymentOperation) {

        return PaymentOperationDto.builder()
                .id(paymentOperation.getId())
                .stripeToken(paymentOperation.getStripeToken())
                .accountUsername(paymentOperation.getAccountUsername())
                .email(paymentOperation.getEmail())
                .amount(paymentOperation.getAmount())
                .success(paymentOperation.isSuccess())
                .message(paymentOperation.getMessage())
                .chargeId(paymentOperation.getChargeId())
                .build();
    }
}
