package nph.laboratory.template.message.service;

import nph.laboratory.template.message.dto.RtcMessage;
import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import nph.laboratory.template.message.repository.GlobalChannelRepository;
import nph.laboratory.template.message.repository.GlobalMsgRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GlobalMsgService {

    private final GlobalChannelRepository globalChannelRepository;
    private final GlobalMsgRepository globalMsgRepository;

    public GlobalMsgService(GlobalChannelRepository globalChannelRepository, GlobalMsgRepository globalMsgRepository) {
        this.globalChannelRepository = globalChannelRepository;
        this.globalMsgRepository = globalMsgRepository;
    }

    public List<RtcMessage> listMessageByChannel(String channelName) {
        GlobalChannel globalChannel = globalChannelRepository.findGlobalChannelByChannelName(channelName);
        return globalMsgRepository.findGlobalMsgByChannelOrderByTimestampAsc(globalChannel)
                .stream()
                .map(RtcMessage::from)
                .toList();
    }


    public void persistMessage(RtcMessage msg) {
        GlobalMsg persistMsg = new GlobalMsg();
        persistMsg.setMessage(msg.getMessage());
        persistMsg.setChannel(
                globalChannelRepository.findGlobalChannelByChannelName(msg.getChannelName())
        );
        persistMsg.setAccountName(msg.getAccountName());
        persistMsg.setTimestamp(LocalDateTime.now());
        globalMsgRepository.save(persistMsg);
    }
}
