package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer implements Deserializer<Message> {

    //public static final String TYPE_CONFIG = "br.com.alura.ecommerce.type_config";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

// forma de serializar e desializar alterada para utilizacao de uma Message<T>
//    private Class<T> type;
//
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
//        try {
//            this.type = (Class<T>) Class.forName(typeName);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("Type for deserialization does not exists in the classpath!", e);
//        }
//    }

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), Message.class);
    }
}
