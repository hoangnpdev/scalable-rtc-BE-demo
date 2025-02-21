package nph.laboratory.template.account;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> listAllUser() {
        List<Account> accountList = accountRepository.findAll();
        log.info("account list size {}", accountList.size());
        return accountList;
    }
}
