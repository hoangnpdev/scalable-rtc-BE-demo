package nph.laboratory.template.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nph.laboratory.template.message.dto.RtcMessage;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer implements Serializer<RtcMessage> {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Override
    public byte[] serialize(String s, RtcMessage rtcMessage) {
        try {
            return objectMapper.writeValueAsBytes(rtcMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
