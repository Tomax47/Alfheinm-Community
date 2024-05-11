package com.alfheim.aflheim_community;

import com.alfheim.aflheim_community.security.filters.AdminAuthorizationFilter;
import com.alfheim.aflheim_community.security.filters.CustomUnauthenticatedOnlyFilter;
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
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
