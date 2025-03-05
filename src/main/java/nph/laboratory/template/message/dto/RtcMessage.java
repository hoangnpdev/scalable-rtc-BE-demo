package nph.laboratory.template.message.dto;

import lombok.Data;
import nph.laboratory.template.message.entity.GlobalMsg;

import java.time.LocalDateTime;

@Data
public class RtcMessage {

    private LocalDateTime timestamp;
    private String accountName;
    private String message;
    private String channelName;

    public static RtcMessage from(String accountName, String message, String channelName) {
        RtcMessage rtcMessage = new RtcMessage();
        rtcMessage.setAccountName(accountName);
        rtcMessage.setMessage(message);
        rtcMessage.setTimestamp(LocalDateTime.now());
        rtcMessage.setChannelName(channelName);
        return rtcMessage;
    }

    public static RtcMessage from(GlobalMsg globalMsg) {
        RtcMessage rtcMessage = new RtcMessage();
        rtcMessage.setAccountName(globalMsg.getAccountName());
        rtcMessage.setMessage(globalMsg.getMessage());
        rtcMessage.setTimestamp(globalMsg.getTimestamp());
        rtcMessage.setChannelName(globalMsg.getChannel().getChannelName());
        return rtcMessage;
    }
}
