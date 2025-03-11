package nph.laboratory.template.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nph.laboratory.template.message.dto.RtcMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    public static final String BOOTSTRAP_SERVERS = "localhost:29092, localhost:39092, localhost:49092";

    @Bean
    public KafkaProducer<String, RtcMessage> kafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "chat-msg-publishing");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getCanonicalName());
        return new KafkaProducer<>(props);
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

}
