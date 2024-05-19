package com.alfheim.aflheim_community.controller.site_pages;

import com.alfheim.aflheim_community.converter.payment.StringToStripeChargeDto;
import com.alfheim.aflheim_community.dto.stripe.PaymentOperationDto;
import com.alfheim.aflheim_community.dto.stripe.StripeChargeDto;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.stripe.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class HomeController {

    @Autowired
    private StripeService stripeService;

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal) {

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "/site_static_pages/home_page";
    }

    @GetMapping("/about")
    public String getAboutPage(Model model, Principal principal) {

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "site_static_pages/about_page";
    }

    @GetMapping("/privacy")
    public String getPrivacyAndPolicyPage(Model model, Principal principal) {

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "site_static_pages/privacy_and_policy_page";
    }

    @GetMapping("/terms")
    public String getTermsAndConditionsPage(Model model, Principal principal) {

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "site_static_pages/terms_and_conditions_page";
    }

    @GetMapping("/pricing")
    public String getPricingPage() {

        return "payment/pricing_page";
    }

    @GetMapping("/checkout/memberTier")
    public String getMemberCheckoutPage(Model model,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("userEmail", userDetails.getUserEmail());
        model.addAttribute("type", "MEMBER");

        return "payment/checkout_page";
    }

    @GetMapping("/checkout/support")
    public String getSupportCheckoutPage(Model model,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("userEmail", userDetails.getUserEmail());
        model.addAttribute("type", "SUPPORT");

        return "payment/checkout_page";
    }

    @GetMapping("/payment/success")
    public String getThanksPage(@RequestParam("chargeId") String chargeId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                Model model) {

        PaymentOperationDto chargeDetails = stripeService.getOperationDetails(chargeId);

        System.out.println("\n\nHOME CONTROLLER : DATA -> " + chargeDetails+"\n\n");
        model.addAttribute("chargeResp", chargeDetails);
        model.addAttribute("createdAt", LocalDateTime.now());

        return "payment/payment_submitted_thanks_page";
    }

    @GetMapping("/support/contactUs")
    public String getSupportContactUsPage() {

        return "site_static_pages/support_page";
    }

}
