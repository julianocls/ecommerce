package br.com.alura.ecommerce;

import java.util.UUID;

public class CorrelationId {

    private final String id;
    private final String title;

    public CorrelationId(String title) {
        this.title = title;
        this.id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(id + "-" + title);
    }
}
