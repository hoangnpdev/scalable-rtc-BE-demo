package nph.laboratory.template.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nph.laboratory.template.message.dto.RtcMessage;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class JsonDeserializer implements Deserializer<RtcMessage> {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Override
    public RtcMessage deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, RtcMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
