package nph.laboratory.template.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/session", "/web-socket")
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/session", "/web-socket").permitAll();
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
