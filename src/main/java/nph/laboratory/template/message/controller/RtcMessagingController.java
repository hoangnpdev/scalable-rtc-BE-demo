package nph.laboratory.template.message.controller;

import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.message.service.GlobalMsgService;
import nph.laboratory.template.message.dto.RtcMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class RtcMessagingController {

    private final GlobalMsgService globalMsgService;

    public RtcMessagingController(GlobalMsgService globalMsgService) {
        this.globalMsgService = globalMsgService;
    }

    @MessageMapping("/global-channel/{channel-name}")
    public RtcMessage handleMessage(String msg, @DestinationVariable("channel-name") String channelName, Principal principal) {
        log.info("Receiving message {}", msg);
        log.info("sender {}", principal.getName());
        RtcMessage rtcMessage = RtcMessage.from(principal.getName(), msg, channelName);
        globalMsgService.persistMessage(rtcMessage);
        return rtcMessage;
    }


}
