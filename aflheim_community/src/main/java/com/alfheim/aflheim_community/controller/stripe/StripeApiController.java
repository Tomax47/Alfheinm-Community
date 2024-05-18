package com.alfheim.aflheim_community.controller.stripe;

import com.alfheim.aflheim_community.dto.stripe.StripeChargeDto;
import com.alfheim.aflheim_community.dto.stripe.StripeTokenDto;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.exception.profile.RoleAlreadyExistException;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.stripe.StripeService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@AllArgsConstructor
public class StripeApiController {

    @Autowired
    private final StripeService stripeService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/checkout/memberTier/charge")
    @ResponseBody
    public ResponseEntity<StripeChargeDto> doChargeCheckout(@RequestBody StripeTokenDto tokenDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserDto user = profileService.getProfileDetailsByUsername(userDetails.getUsername());

        if (user.getRole().equals("MEMBER") || user.getRole().equals("ADMIN")) {
            // Prevent another member tier payment
            throw new RoleAlreadyExistException("User is already a Member or an Admin");
        }

        // Perform payment
        StripeChargeDto chargeOperationResp = stripeService.doTransaction("MEMBER", tokenDto, user, null);

        if (chargeOperationResp.isSuccess()) {
            // Update user balance etc
            profileService.updateAccountRole(chargeOperationResp.getUsername());
        }

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(chargeOperationResp);
    }

    @PostMapping("/checkout/support/charge")
    @ResponseBody
    public ResponseEntity<StripeChargeDto> doFundChargeCheckout(@RequestBody StripeTokenDto tokenDto,
                                                                @RequestParam(value = "amount", required = false) Double amount,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserDto user = profileService.getProfileDetailsByUsername(userDetails.getUsername());

        StripeChargeDto chargeOperationResp = stripeService.doTransaction("SUPPORT", tokenDto, user, amount);

        if (chargeOperationResp.isSuccess() && user.getRole().equals("VISITOR")) {
            // Update user to member "If visitor"
            profileService.updateAccountRole(chargeOperationResp.getUsername());
        }

        // Adding the reputation pts
        profileService.addReputationPoints(chargeOperationResp.getUsername(), chargeOperationResp.getAmount() * 2);

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(chargeOperationResp);
    }
}
