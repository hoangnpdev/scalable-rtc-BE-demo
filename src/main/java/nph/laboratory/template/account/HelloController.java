package nph.laboratory.template.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private AccountService accountService;

    @GetMapping("/session")
    public ResponseEntity<String> session() {
        return ResponseEntity.ok(sessionManager.newSession());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Account>> listUser(){
        return ResponseEntity.ok(accountService.listAllUser());
    };


    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello world");
    }

}
