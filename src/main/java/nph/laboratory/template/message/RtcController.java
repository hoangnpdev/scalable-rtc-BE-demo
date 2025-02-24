package nph.laboratory.template.message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RtcController {

    @MessageMapping("/global-channel/**")
    public String handleMessage(String msg) {
        return msg;
    }
}
