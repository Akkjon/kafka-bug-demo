package com.example.kafkabugdemo;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;
    @Autowired
    private BindingServiceProperties bindingServiceProperties;


    @Test
    void test(CapturedOutput output) {

        sendMessage(getTestMessageJson(), "consumer-in-0");

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(output).contains("Data: this-is-a-test");
        });
    }

    private void sendMessage(String payload, String binding) {

        String topic = bindingServiceProperties.getBindingDestination(binding);
        ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(topic, payload.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(producerRecord);
    }

    private String getTestMessageJson() {
        return "this-is-a-test";
    }
}
