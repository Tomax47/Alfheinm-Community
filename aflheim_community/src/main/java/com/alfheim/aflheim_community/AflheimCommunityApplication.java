package com.alfheim.aflheim_community;

import com.alfheim.aflheim_community.security.filters.CustomUnauthenticatedOnlyFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

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
}
