package nph.laboratory.template.app;

import java.security.Principal;

public class UserNameOnlyPrincipal implements Principal {

    private String accountName;

    public UserNameOnlyPrincipal(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String getName() {
        return accountName;
    }

    public void setName(String accountName) {
        this.accountName = accountName;
    }
}
