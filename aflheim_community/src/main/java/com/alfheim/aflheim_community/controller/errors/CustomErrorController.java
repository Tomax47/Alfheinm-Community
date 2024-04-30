package com.alfheim.aflheim_community.controller.errors;

import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String getNotFound(HttpServletRequest request,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.NOT_FOUND.value()) {
            // Not Found 404
            return "error_pages/not_found_page";

        } else if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            // Internal Server Error 500
            return "error_pages/internal_server_error_page";

        } else if (request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).equals("/profile/update")) {
            // Profile Update Data Input Error

            // TODO: FIND A WAY TO SHOW THE MESSAGE & PREVENT THE FORM FROM RESENDING
            model.addAttribute("user", userDetails.getUserDto());
            return "user/profile_page";
        }

        return "error_pages/internal_server_error_page";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
