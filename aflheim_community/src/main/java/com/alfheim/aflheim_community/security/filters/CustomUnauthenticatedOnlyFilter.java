package com.alfheim.aflheim_community.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomUnauthenticatedOnlyFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(CustomUnauthenticatedOnlyFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("ONLY UNAUTHENTICATED FILTER CALLED!");
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Not authenticated. Allowing request
            System.out.println("NOT AUTHENTICATED. ALLOWING REQUEST");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // Authenticated. Redirecting
            System.out.println("AUTHENTICATED. REDIRECTING TO PROFILE");
            response.sendRedirect("/profile");
        }
    }

}
