package ChatApp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    private static KafkaProducer<String,String > producer;

    public Producer(){
        String bootstrap_server = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        producer = new KafkaProducer<String, String>(properties);
    }

    public KafkaProducer<String,String> getProducer(){
        return producer;
    }

    public void produce(Message msg) {


        ObjectMapper obj = new ObjectMapper();
        String value = null;
        try {
            value = obj.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ProducerRecord<String,String> record = new ProducerRecord<>(msg.getReceiver(),msg.getSender(),value);
        producer.send(record);

        record = new ProducerRecord<>(msg.getSender(), msg.getReceiver(),value);
        producer.send(record);

//            producer.send(record, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    if(e!=null){
//                        System.err.println("Error : "+e);
//                    }
//                    else{
//                        System.out.println("Message Added : "+ "\nTopic : "+recordMetadata.topic() + "\nPartition : "+recordMetadata.partition() + "\nOffset : "+recordMetadata.offset() +"\nValue : "+record.value());
//                    }
//                }
//            });
//        producer.flush();
//        producer.close();
    }
}
