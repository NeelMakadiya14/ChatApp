package ChatAppFinal;

import ChatApp.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class ConsumerWithThread {
    public static void consume(String topic, int numThread) {

        Random rand = new Random();
        String bootstrap_server= "127.0.0.1:9092";

        int groupID = rand.nextInt(100000000);
        ArrayList<MyRunnable> list = new ArrayList<>();

        for(int i=0;i<numThread;i++){
            String name = "Thread_"+i;
            MyRunnable myRunnable = new MyRunnable(groupID,bootstrap_server,topic);
            Thread thread = new Thread(myRunnable,name);
            list.add(myRunnable);
            thread.start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            for(int i=0;i<10;i++){
                (list.get(i)).shutDown();
            }
//            System.out.println("All Thread is closed.");
        }));
    }
}

class MyRunnable implements Runnable{

    private KafkaConsumer<String,String> consumer;

    public MyRunnable(int groupID,String bootstrap_server, String topic){

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, String.valueOf(groupID));
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
        ConsumerRecords<String,String> records;
        try {
            while(true){
                records = consumer.poll(Duration.ofMillis(100));

                ChatApp.Message msg = null;
                for(ConsumerRecord<String,String> record : records){
                    try {
                        msg  = new ObjectMapper().readValue(record.value(), Message.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(msg.getSender() +" to "+ msg.getReceiver() +" : "+msg.getMsg());
                }
            }
        }
        catch (WakeupException e) {
//            System.out.println("Wakeup Exception is Caught..");
        } finally {
//            System.out.println("Closing the Consumer Thread.");
            consumer.close();
        }
    }

    public void shutDown(){
        consumer.wakeup();
    }
}
