package nph.laboratory.template.app;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserNameOnlyAuthentication implements Authentication {
    private boolean authenticated = false;
    private String accountName;
    private UserNameOnlyPrincipal principal;

    private UserNameOnlyAuthentication() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public UserNameOnlyPrincipal getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return accountName;
    }

    public static UserNameOnlyAuthentication fromAccountName(String accountName) {
        UserNameOnlyAuthentication authentication = new UserNameOnlyAuthentication();
        authentication.accountName = accountName;
        authentication.authenticated = true;
        authentication.principal = new UserNameOnlyPrincipal(accountName);
        return authentication;
    }
}
