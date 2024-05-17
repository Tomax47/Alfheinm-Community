package com.alfheim.aflheim_community;

import com.alfheim.aflheim_community.security.filters.AdminAuthorizationFilter;
import com.alfheim.aflheim_community.security.filters.CustomUnauthenticatedOnlyFilter;
import com.alfheim.aflheim_community.security.filters.MembersAdminsOnlyFilter;
import com.alfheim.aflheim_community.security.filters.VisitorsOnlyFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class AflheimCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AflheimCommunityApplication.class, args);
	}

	@Bean
	FilterRegistrationBean<CustomUnauthenticatedOnlyFilter> customAuthFilterFilterRegistration() {
		final FilterRegistrationBean<CustomUnauthenticatedOnlyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new CustomUnauthenticatedOnlyFilter());
		filterRegistrationBean.addUrlPatterns("/login", "/login/*", "/register", "/password/reset/*", "/confirm/*", "/account/confirm/*");

		return filterRegistrationBean;
	}

	@Bean
	FilterRegistrationBean<AdminAuthorizationFilter> customAdminAuthorizationFilter() {
		final FilterRegistrationBean<AdminAuthorizationFilter> adminAuthorizationFilterRegBean = new FilterRegistrationBean<>();
		adminAuthorizationFilterRegBean.setFilter(new AdminAuthorizationFilter());
		adminAuthorizationFilterRegBean.addUrlPatterns("/admin/*");

		return adminAuthorizationFilterRegBean;
	}

	@Bean
	FilterRegistrationBean<MembersAdminsOnlyFilter> membersAdminsOnlyFilterFilter() {
		final FilterRegistrationBean<MembersAdminsOnlyFilter> membersAdminsOnlyFilterFilterRegBean = new FilterRegistrationBean<>();
		membersAdminsOnlyFilterFilterRegBean.setFilter(new MembersAdminsOnlyFilter());
		membersAdminsOnlyFilterFilterRegBean.addUrlPatterns("/profile/publications/new", "/profile/publication/add");

		return membersAdminsOnlyFilterFilterRegBean;
	}

	@Bean
	FilterRegistrationBean<VisitorsOnlyFilter> visitorsOnlyFilterFilter() {
		final FilterRegistrationBean<VisitorsOnlyFilter> visitorsOnlyFilterFilterRegistrationBean = new FilterRegistrationBean<>();
		visitorsOnlyFilterFilterRegistrationBean.setFilter(new VisitorsOnlyFilter());
		// Preventing Admins and Members from submitting another membership tier payment
		visitorsOnlyFilterFilterRegistrationBean.addUrlPatterns("/checkout/memberTier", "/checkout/memberTier/charge");

		return visitorsOnlyFilterFilterRegistrationBean;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
