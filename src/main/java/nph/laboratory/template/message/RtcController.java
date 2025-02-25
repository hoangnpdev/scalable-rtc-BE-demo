package nph.laboratory.template.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class RtcController {

    @MessageMapping("/global-channel/**")
    public RtcMessage handleMessage(String msg, Principal principal) throws JsonProcessingException {
        return RtcMessage.from(principal.getName(), msg);
    }


}
