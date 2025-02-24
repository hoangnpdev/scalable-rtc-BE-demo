package nph.laboratory.template.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account:register")
    public ResponseEntity<Account> registerNewAccount(@RequestBody AccountRegisterInfo registerInfo) {
        return ResponseEntity.ok(
                accountService.createNewAccount(registerInfo)
        );
    }

    @PostMapping("/account:login")
    public ResponseEntity<String> session(@RequestBody AccountLoginInfo loginInfo) throws IllegalAccessException {
        return ResponseEntity.ok(accountService.login(loginInfo));
    }
}
