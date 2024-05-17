package com.alfheim.aflheim_community.security.filters;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VisitorsOnlyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authenticated user.
        if (!isAdminOrMember(authentication)) {
            // Authorized. only visitors
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // Unauthorized. member or admin
            System.out.println("UNAUTHORIZED. REDIRECTING TO Support");
            response.sendRedirect("/profile");
        }
    }

    private boolean isAdminOrMember(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> "ADMIN".equals(authority) || "MEMBER".equals(authority));
    }
}
