package science.mengxin.java.kafka.guide;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerDemo {

    private static Properties kafkaProps = new Properties();

    public static void main(String[] args) {

        kafkaProps.put("bootstrap.servers", "127.0.0.1:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(kafkaProps);

//        ProducerRecord<String, String> record =
//                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE, "Precision Products",
//                        "France");
//        try {
//            producer.send(record);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ProducerRecord<String, String> record =
                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE, "Precision Products", "France");
        System.out.println("start send");
        try {
            producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
