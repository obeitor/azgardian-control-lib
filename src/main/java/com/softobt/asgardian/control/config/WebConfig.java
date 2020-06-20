package com.softobt.asgardian.control.config;

import com.softobt.asgardian.control.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author aobeitor
 * @since 6/3/20
 */
@Configuration(value = "AsgardianWebConfig")
public class WebConfig implements WebMvcConfigurer {

    @Value("${control.client.key:abcd}")
    private String clientKey;

    @Value("${control.url.allowed:}")
    private String allowedUrls;

    @Autowired
    private JWTokenUtil jwTokenUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAllowedUrls(allowedUrls);
        authenticationFilter.setClientKey(clientKey);
        authenticationFilter.setTokenUtil(jwTokenUtil);
        registry.addInterceptor(authenticationFilter);
    }
}
