package nph.laboratory.template.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {

    @MessageMapping("/echo")
    public String echo(String msg) {
        return msg;
    }
}
