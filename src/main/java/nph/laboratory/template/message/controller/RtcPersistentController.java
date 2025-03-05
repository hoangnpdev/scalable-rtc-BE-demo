package nph.laboratory.template.message.controller;


import nph.laboratory.template.message.dto.RtcMessage;
import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import nph.laboratory.template.message.service.GlobalChannelService;
import nph.laboratory.template.message.service.GlobalMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rtc")
public class RtcPersistentController {

    private final GlobalChannelService globalChannelService;
    private final GlobalMsgService globalMsgService;

    public RtcPersistentController(GlobalChannelService globalChannelService, GlobalMsgService globalMsgService) {
        this.globalChannelService = globalChannelService;
        this.globalMsgService = globalMsgService;
    }

    @GetMapping("/global-channel")
    public ResponseEntity<List<GlobalChannel>> globalChannel() {
        return ResponseEntity.ok(
                globalChannelService.getGlobalChannels()
        );
    }

    @GetMapping("/global-message/{channel-name}")
    public ResponseEntity<List<RtcMessage>> globalMessage(@PathVariable("channel-name") String channelName) {
        return ResponseEntity.ok(
                globalMsgService.listMessageByChannel(channelName)
        );
    }

}
