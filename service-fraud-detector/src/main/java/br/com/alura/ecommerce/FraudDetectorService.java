package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var fraudeDetectorService = new FraudDetectorService();
        try(var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudeDetectorService::parse,
                Map.of())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
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

        var message = record.value();
        var order = message.getPayload();
        if(isFraud(order)) {
            // pretending that the fraud happens when the amount is >= 4500
            System.out.println("Order is a fraud, rejected! " + order);
            kafkaDispatcher.send("ECOMMERCE_NEW_ORDER_REJECTED", order.getEmail(),
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()),
                    order);
        } else {
            System.out.println("Order approved! " + order);
            kafkaDispatcher.send("ECOMMERCE_NEW_ORDER_APPROVED", order.getEmail(),
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()),
                    order);
        }

    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
