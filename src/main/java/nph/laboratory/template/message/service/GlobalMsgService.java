package nph.laboratory.template.message.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.message.dto.RtcMessage;
import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import nph.laboratory.template.message.repository.GlobalChannelRepository;
import nph.laboratory.template.message.repository.GlobalMsgRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GlobalMsgService {

    private final KafkaProducer<String, RtcMessage> kafkaProducer;
    private final GlobalChannelRepository globalChannelRepository;
    private final GlobalMsgRepository globalMsgRepository;
    private final ElasticsearchClient esClient;

    public GlobalMsgService(GlobalChannelRepository globalChannelRepository,
                            GlobalMsgRepository globalMsgRepository,
                            KafkaProducer<String, RtcMessage> kafkaProducer, ElasticsearchClient esClient) {
        this.globalChannelRepository = globalChannelRepository;
        this.globalMsgRepository = globalMsgRepository;
        this.kafkaProducer = kafkaProducer;
        this.esClient = esClient;
    }

    public List<RtcMessage> listMessageByChannel(String channelName) {
        GlobalChannel globalChannel = globalChannelRepository.findGlobalChannelByChannelName(channelName);
        return globalMsgRepository.findGlobalMsgByChannelOrderByTimestampAsc(globalChannel)
                .stream()
                .map(RtcMessage::from)
                .toList();
    }


    public void persistMessage(RtcMessage msg) {
        GlobalMsg persistedMsg = new GlobalMsg();
        persistedMsg.setMessage(msg.getMessage());
        persistedMsg.setChannel(
                globalChannelRepository.findGlobalChannelByChannelName(msg.getChannelName())
        );
        persistedMsg.setAccountName(msg.getAccountName());
        persistedMsg.setTimestamp(LocalDateTime.now());
        GlobalMsg returnedPersistedMsg = globalMsgRepository.save(persistedMsg);
        kafkaProducer.send(
                new ProducerRecord<>("global-message-topic", "", RtcMessage.from(returnedPersistedMsg)),
                (recordMetadata, e) -> {
                    if (e != null) {
                        log.info("error {}", ExceptionUtils.getStackTrace(e));
                    }
                }
        );
    }

    public List<RtcMessage> searchMessageByChannel(String channelName, String query) throws IOException {
        Query channelMatching = TermQuery.of(q -> q
                .field("channelName")
                .value(channelName)
        )._toQuery();
        Query searchMatching = MatchQuery.of(q -> q
                .field("message")
                .query(query)
        )._toQuery();
        Query boolQuery = BoolQuery.of(q -> q
                .must(channelMatching)
                .must(searchMatching)
        )._toQuery();
        SearchResponse<RtcMessage> res = esClient.search(
                s -> s.index("global-message-index")
                        .query(boolQuery),
                RtcMessage.class
        );
        return res.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }
}
