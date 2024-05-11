package com.alfheim.aflheim_community.security.filters;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminAuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("\n\nADMIN AUTHORIZATION FILTER HOOKED\n\n");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // User is not authenticated.
            System.out.println("NOT AUTHENTICATED. REDIRECTING TO LOGIN");
            response.sendRedirect("/login");
        } else {
            // Authenticated user.
            if (isAdmin(authentication)) {
                // Authorized.
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // Unauthorized.
                System.out.println("UNAUTHORIZED. REDIRECTING HOME");
                response.sendRedirect("/");
            }
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> "ADMIN".equals(authority));
    }
}
