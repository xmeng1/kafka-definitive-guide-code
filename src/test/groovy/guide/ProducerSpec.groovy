package guide

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import science.mengxin.java.kafka.guide.KafkaGuideConst
import spock.lang.Specification
import spock.util.concurrent.AsyncConditions


class ProducerSpec extends Specification {

    private static Properties kafkaProps = new Properties();
    KafkaProducer producer;
    ProducerRecord<String, String> record
    def setup() {
        kafkaProps.put("bootstrap.servers", "127.0.0.1:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(kafkaProps);
        record =
                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE, "Precision Products", "France");
    }

    def "send message Synchronously"() {
        given:
        System.out.println("start send");
        when:
        String result;
        try {
            result = producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        then:
        result
        println(result)
    }


    def "send message Asynchronously"() {
        given:
        def asyncConditions = new AsyncConditions()
        System.out.println("start send");
        when:
        String result;
        try {
            result = producer.send(record, new Callback() {
                @Override
                void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    asyncConditions.evaluate {
                        println(e)
                        assert e == null
                        assert recordMetadata != null
                        println(recordMetadata.toString())
                        if (e != null) {
                            e.printStackTrace();

                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        then:
        result
        println(result)
        asyncConditions.await(1000 as Double)
    }
}