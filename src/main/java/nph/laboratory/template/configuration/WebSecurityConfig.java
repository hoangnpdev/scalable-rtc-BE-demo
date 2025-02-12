package nph.laboratory.template.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/session")
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/session").permitAll();
                });
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain otherSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterAfter(new CustomAuthenticationFilter(), SecurityContextHolderFilter.class)
                .authorizeHttpRequests(req -> {
            req.anyRequest().authenticated();
        });
        return http.build();
    }
}
