package nph.laboratory.template.app;

import nph.laboratory.template.account.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final SessionManager sessionManager;

    public WebSecurityConfig(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/account:login", "/account:register", "/web-socket")
                .authorizeHttpRequests(req -> {
                    req
                            .requestMatchers("/account:login", "/account:register", "/web-socket")
                            .permitAll();
                }).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain otherSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterAfter(new CustomAuthenticationFilter(sessionManager), CorsFilter.class)
                .authorizeHttpRequests(req -> {
                    req.anyRequest().authenticated();
                }).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
