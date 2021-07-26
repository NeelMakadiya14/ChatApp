package ChatApp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Temp {
    public static void main(String[] args) {
        Map<String, ArrayList<String>> Chat = new HashMap<>();

        if(Chat.get("Neel")==null){
            Chat.put("Neel",new ArrayList<String>());
            Chat.get("Neel").add("msg1");
        }

        Chat.get("Neel").add("msg2");

        System.out.println(Chat);
    }
}


