package nph.laboratory.template.message;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    private final ObjectMapper objectMapper;

    public ElasticsearchConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean(destroyMethod = "close")
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).setDefaultHeaders(new Header[]{
                new BasicHeader("Content-type", "application/json")
        }).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper));
        return new ElasticsearchClient(transport);
    }
}
