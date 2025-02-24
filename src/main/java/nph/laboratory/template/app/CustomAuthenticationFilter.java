package nph.laboratory.template.app;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.account.SessionManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class CustomAuthenticationFilter implements Filter {

    private SessionManager sessionManager;

    public CustomAuthenticationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("entering filter");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        CustomAuthentication authentication = new CustomAuthentication();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Enumeration<String> sessionEnum = req.getHeaders(HttpHeaders.AUTHORIZATION);
        if (sessionEnum.hasMoreElements()) {
            String session = sessionEnum.nextElement();
            log.info("session {}", session);
            if (sessionManager.checkSessionExist(session)) {
                authentication.setAuthenticated(true);
                authentication.setName(sessionManager.getAccountName(session));
            };
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
