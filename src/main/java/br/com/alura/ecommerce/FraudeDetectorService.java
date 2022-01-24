package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class FraudeDetectorService {

    public static void main(String[] args) {
        var fraudeDetectorService = new FraudeDetectorService();
        var service = new KafkaService(FraudeDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", fraudeDetectorService::parse);
        service.run();
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Processing new order, checking for fraud!");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order processed!");
    }

}
