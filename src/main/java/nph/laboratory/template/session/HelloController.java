package nph.laboratory.template.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/session")
    public ResponseEntity<String> session() {
        return ResponseEntity.ok(sessionManager.newSession());
    }


    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello world");
    }

}
