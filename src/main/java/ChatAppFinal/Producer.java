package ChatAppFinal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    private static KafkaProducer<String,String> producer;

    public Producer(){
        String bootstrap_server = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        producer = new KafkaProducer<String, String>(properties);
    }

    public void Produce(String Topic, String Key, Message msg){
        ObjectMapper obj = new ObjectMapper();
        String Value = null;
        try {
            Value = obj.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ProducerRecord record = new ProducerRecord(Topic,Key,Value);
        producer.send(record);

//        producer.flush();
//        producer.close();
    }
}
