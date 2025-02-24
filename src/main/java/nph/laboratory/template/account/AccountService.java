package nph.laboratory.template.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final SessionManager sessionManager;

    public AccountService(AccountRepository accountRepository, SessionManager sessionManager) {
        this.accountRepository = accountRepository;
        this.sessionManager = sessionManager;
    }

    public List<Account> listAllAccount() {
        List<Account> accountList = accountRepository.findAll();
        log.info("account list size {}", accountList.size());
        return accountList;
    }

    public Account createNewAccount(AccountRegisterInfo registerInfo) {
        return accountRepository.save(registerInfo.toAccount());
    }

    public String login(AccountLoginInfo accountLoginInfo) throws IllegalAccessException {
        long noMatchedAccount = accountRepository.countByAccountNameAndPassword(
                accountLoginInfo.getAccountName(),
                accountLoginInfo.getPassword()
        );
        if (noMatchedAccount != 1) {
            throw new IllegalAccessException("Wrong account info");
        }
        return sessionManager.newSession(accountLoginInfo.getAccountName());
    }
}
