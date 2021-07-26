package ChatApp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import static ChatApp.User.createTopic;

public class GroupChat {

    String userName;
    String groupName;

    public static void main(String[] args) {
       GroupChat groupChat =new GroupChat();
       groupChat.run();
    }

    public void run(){
        System.out.print("Enter The UserName : ");
        Scanner sc = new Scanner(System.in);

        userName = sc.nextLine();

        System.out.print("Enter The GroupName : ");

        groupName = sc.nextLine();


        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                consume(groupName);
            }
        });

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Groupsend(groupName,userName);
                }

            }
        });

        listener.start();
        sender.start();


    }

    public static void Groupsend(String groupName, String userName){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

//        System.out.print("Enter the message : ");
        String msg = null;
        try {
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


        createTopic(groupName,1);

        Producer Myproducer = new Producer();
        KafkaProducer<String,String> producer= Myproducer.getProducer();
        ProducerRecord record = new ProducerRecord(groupName,userName,msg);
        producer.send(record);

    }

    public static void consume(String topic){

        Random rand = new Random();
        String bootstrap_server= "127.0.0.1:9092";

        int groupID = rand.nextInt(100000000);

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, String.valueOf(groupID));
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(topic));

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String,String> record : records){
                System.out.println(record.key() +" : "+ record.value());
            }
        }
    }
}



