package nph.laboratory.template.message.pipeline;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.clickhouse.data.ClickHouseValue;
import com.clickhouse.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.message.JsonDeserializer;
import nph.laboratory.template.message.KafkaConfiguration;
import nph.laboratory.template.message.dto.RtcMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class MessagePipelines {

    private final ElasticsearchClient elasticsearchClient;
    private final NamedParameterJdbcTemplate chJdbcTemplate;

    public MessagePipelines(ElasticsearchClient elasticsearchClient, @Qualifier("clickhouseJdbcTemplate") NamedParameterJdbcTemplate chJdbcTemplate) {
        this.elasticsearchClient = elasticsearchClient;
        this.chJdbcTemplate = chJdbcTemplate;
    }


    @Async
    public void kafkaOut() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "EsConsumerGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getCanonicalName());
        try (final KafkaConsumer<String, RtcMessage> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("global-message-topic"));
            while (true) {
                ConsumerRecords<String, RtcMessage> kafkaRecords = consumer.poll(Duration.ofSeconds(10));
                List<RtcMessage> records = new ArrayList<RtcMessage>();
                kafkaRecords.forEach(e -> records.add(e.value()));
                if (kafkaRecords.count() > 0) {
//                    toElasticSearch(records);
                    toClickhouse(records);
                }
            }

        }
    }

    private void toElasticSearch(List<RtcMessage> records) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        log.info("insert some records");
        records
                .forEach(record -> br.operations(
                        op -> op.index(
                                idx -> idx
                                        .index("global-message-index")
                                        .id(record.getMsgId())
                                        .document(record)
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

    private void toClickhouse(List<RtcMessage> records) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(records.toArray());
        int[] updateCounts = chJdbcTemplate.batchUpdate(
                "INSERT INTO rtc_chat_monitor.rtc_message (*) VALUES (:channelName, :accountName, :message, :msgId, :timestamp)", batch);
        long total = Arrays.stream(updateCounts).asLongStream().sum();
        if (total != records.size()) {
            log.error("some error");
        }
    }

}
