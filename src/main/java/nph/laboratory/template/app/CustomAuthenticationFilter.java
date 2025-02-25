package nph.laboratory.template.app;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.account.SessionManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class CustomAuthenticationFilter implements Filter {

    private final SessionManager sessionManager;

    public CustomAuthenticationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("entering filter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Enumeration<String> sessionEnum = req.getHeaders(HttpHeaders.AUTHORIZATION);
        if (!sessionEnum.hasMoreElements()) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String session = sessionEnum.nextElement();
        if (!sessionManager.checkSessionExist(session)) {
            throw new BadCredentialsException("Invalid credentials");
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserNameOnlyAuthentication authentication = UserNameOnlyAuthentication
                .fromAccountName(sessionManager.getAccountName(session));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
