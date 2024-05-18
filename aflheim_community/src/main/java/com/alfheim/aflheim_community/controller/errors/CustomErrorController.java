package com.alfheim.aflheim_community.controller.errors;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("**/error/**")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.NOT_FOUND.value()) {
            // Not Found 404
            return "error_pages/not_found_page";

        } else if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            // Internal Server Error 500
            return "error_pages/internal_server_error_page";

        } else if (statusCode != null && Integer.valueOf(statusCode.toString()) == HttpStatus.BAD_REQUEST.value()) {
            // Internal Server Error 400
            return "error_pages/bad_request_page";

        }

        // TODO: HANDLE OTHER ERRORS

        return "error_pages/internal_server_error_page";
    }

    @RequestMapping("/login/error")
    public String handleIncorrectLoginError(Model model) {
        System.out.println("CUSTOM ERROR HANDLER FOR INCORRECT CREDENTIALS. SETTING UP INCORRECT CREDENTIALS FLAG");
        model.addAttribute("incorrectCredentials", true);
        return "users/auth/login_page";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
