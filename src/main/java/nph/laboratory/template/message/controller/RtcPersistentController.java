package nph.laboratory.template.message.controller;


import nph.laboratory.template.message.dto.MessageQuery;
import nph.laboratory.template.message.dto.RtcMessage;
import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import nph.laboratory.template.message.service.GlobalChannelService;
import nph.laboratory.template.message.service.GlobalMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping("/global-message/{channel-name}/search")
    public ResponseEntity<List<RtcMessage>> search(
            @PathVariable("channel-name") String channelName,
            @RequestBody MessageQuery query) throws IOException {
        return ResponseEntity.ok(
                globalMsgService.searchMessageByChannel(channelName, query.getQuery())
        );
    }

}
