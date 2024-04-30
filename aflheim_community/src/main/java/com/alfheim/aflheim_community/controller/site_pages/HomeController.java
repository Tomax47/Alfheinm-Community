package com.alfheim.aflheim_community.controller.site_pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomePage() {
        return "/site_static_pages/home_page";
    }

    @GetMapping("/about")
    public String getAboutPage() { return "site_static_pages/about_page"; }

    @GetMapping("/privacy")
    public String getPrivacyAndPolicyPage() { return "site_static_pages/privacy_and_policy_page"; }

    @GetMapping("/terms")
    public String getTermsAndConditionsPage() { return "site_static_pages/terms_and_conditions_page"; }

}
