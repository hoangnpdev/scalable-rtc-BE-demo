package nph.laboratory.template.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    private final AccountService accountService;

    public HelloController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<Account>> listUser(){
        return ResponseEntity.ok(accountService.listAllAccount());
    };


    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello world");
    }

}
