package com.alfheim.aflheim_community.controller.errors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String getNotFound(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.NOT_FOUND.value()) {
            return "error_pages/not_found_page";
        }

        return "error_pages/internal_server_error_page";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
