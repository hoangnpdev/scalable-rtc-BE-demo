package nph.laboratory.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nph.laboratory.template.message.pipeline.MessagePipelines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TemplateApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

	private final MessagePipelines messagePipelines;


	public TemplateApplication(MessagePipelines messagePipelines) {
		this.messagePipelines = messagePipelines;
	}

	@Override
	public void run(String... args) throws Exception {
		messagePipelines.kafkaToElasticSearch();
	}
}
