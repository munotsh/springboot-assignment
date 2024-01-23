package com.example.assignment.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class PersonServiceInterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    PersonServiceInterceptor personServiceInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(personServiceInterceptor);
    }
}
