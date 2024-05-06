package com.alfheim.aflheim_community.controller.site_pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

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

}
