package nph.laboratory.template.security;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class CustomAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new CustomAuthentication();
//        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
