package nph.laboratory.template.message.service;

import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.message.dto.RtcMessage;
import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import nph.laboratory.template.message.repository.GlobalChannelRepository;
import nph.laboratory.template.message.repository.GlobalMsgRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GlobalMsgService {

    private final KafkaProducer<String, RtcMessage> kafkaProducer;
    private final GlobalChannelRepository globalChannelRepository;
    private final GlobalMsgRepository globalMsgRepository;

    public GlobalMsgService(GlobalChannelRepository globalChannelRepository,
                            GlobalMsgRepository globalMsgRepository,
                            KafkaProducer<String, RtcMessage> kafkaProducer) {
        this.globalChannelRepository = globalChannelRepository;
        this.globalMsgRepository = globalMsgRepository;
        this.kafkaProducer = kafkaProducer;
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
        kafkaProducer.send(
                new ProducerRecord<>("global-message-topic", "", msg),
                (recordMetadata, e) -> {
                    if (e != null) {
                        log.info("error {}", ExceptionUtils.getStackTrace(e));
                    }
                }
        );
    }
}
