package nph.laboratory.template.account;

import lombok.Data;

@Data
public class AccountRegisterInfo {
    private String accountName;
    private String password;

    public Account toAccount() {
        Account newAccount = new Account();
        newAccount.setAccountName(this.accountName);
        newAccount.setPassword(this.password);
        return newAccount;
    }
}
