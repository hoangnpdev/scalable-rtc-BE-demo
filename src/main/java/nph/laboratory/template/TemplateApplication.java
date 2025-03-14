package nph.laboratory.template;

import nph.laboratory.template.message.pipeline.MessagePipelines;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
		messagePipelines.kafkaOut();
	}
}
