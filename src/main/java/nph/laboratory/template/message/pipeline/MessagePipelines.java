package nph.laboratory.template.message.pipeline;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.message.JsonDeserializer;
import nph.laboratory.template.message.KafkaConfiguration;
import nph.laboratory.template.message.dto.RtcMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class MessagePipelines {

    private final ElasticsearchClient elasticsearchClient;

    public MessagePipelines(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    @Async
    public void kafkaToElasticSearch() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "EsConsumerGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getCanonicalName());
        try (final KafkaConsumer<String, RtcMessage> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("global-message-topic"));
            while (true) {
                BulkRequest.Builder br = new BulkRequest.Builder();
                ConsumerRecords<String, RtcMessage> records = consumer.poll(Duration.ofSeconds(10));
                if (records.count() > 0) {
                    log.info("insert some records");
                    records
                            .forEach(record -> br.operations(
                                    op -> op.index(
                                            idx -> idx
                                                    .index("global-message-index")
                                                    .id(record.value().getMsgId())
                                                    .document(record.value())
                                    ))
                            );
                    try {
                        BulkResponse bulkResponse = elasticsearchClient.bulk(br.build());
                        if (bulkResponse.errors()) {
                            log.error("Bulk had errors");
                            for (BulkResponseItem item : bulkResponse.items()) {
                                if (item.error() != null) {
                                    log.error(item.error().reason());
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("es ingesting error {}", ExceptionUtils.getStackTrace(e));
                    }
                }
            }

        }
    }
}
