package nph.laboratory.template.message;

import lombok.Data;

@Data
public class RtcMessage {
    private String accountName;
    private String message;

    public static RtcMessage from(String accountName, String message) {
        RtcMessage rtcMessage = new RtcMessage();
        rtcMessage.setAccountName(accountName);
        rtcMessage.setMessage(message);
        return rtcMessage;
    }
}
