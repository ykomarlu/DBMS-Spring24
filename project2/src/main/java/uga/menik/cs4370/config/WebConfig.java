/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uga.menik.cs4370.components.AuthInterceptor;

/**
 * This is a configuration class. See comments in AuthInterceptor.java
 * regarding dependency injection and inversion of control.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // This is an object that allows redirection if user is logged in.
    private final AuthInterceptor authInterceptor;

    /**
     * An AuthInterceptor will be initialized and provided
     * when a WebConfig is initalized by Spring Boot.
     */
    @Autowired
    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * This is where we register the interceptor to the URL
     * patterns we want the redirection in.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // Apply to all paths.
                .addPathPatterns("/**")
                // Exclude login and register paths.
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**");
    }

}
