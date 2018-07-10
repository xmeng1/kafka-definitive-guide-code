package guide

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import science.mengxin.java.kafka.guide.KafkaGuideConst
import science.mengxin.java.kafka.guide.model.Customer
import science.mengxin.java.kafka.guide.model.CustomerSerializer
import spock.lang.Specification
import spock.util.concurrent.AsyncConditions


class ProducerSpec extends Specification {

    private static Properties kafkaProps = new Properties();
    KafkaProducer producer;
    ProducerRecord<String, String> record
    def setup() {
        kafkaProps.put("bootstrap.servers", KafkaGuideConst.KAFKA_SERVER_URL);

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


    def "send custom message"() {
        given: "object"
        Customer customer = new Customer(10, "Alice")

        and: "create record for customer"
        ProducerRecord<String, Customer> customerRecord =
                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE, "alice", customer);
        and: "config the serializer for producer"
        kafkaProps.put("value.serializer", CustomerSerializer.class.getName());
        KafkaProducer producerNew = new KafkaProducer<String, String>(kafkaProps);
        when: "send message"
        String result1;
        try {
            result1 = producerNew.send(customerRecord).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        then:
        result1
        println(result1)
    }


    def "send message with Avro" () {
        kafkaProps.put("key.serializer",
                "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kafkaProps.put("value.serializer",
                "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kafkaProps.put("schema.registry.url", KafkaGuideConst.SCHEMA_REGISTRY_URL);
//        kafkaProps.put("value.serializer", KafkaAvroSerializer.class.getName());
        int wait = 500;

        Producer<String, science.mengxin.java.kafka.guide.model.avro.Customer> producerAvro = new KafkaProducer<>(kafkaProps);
        // We keep producing new events until someone ctrl-c
        science.mengxin.java.kafka.guide.model.avro.Customer customer = new science.mengxin.java.kafka.guide.model.avro.Customer();
        customer.setEmail("xx@gmail.com")
        // if set id, the message will be set specific partition based on hash of key
        customer.setId(10)
        customer.setName("xx")

        System.out.println("Generated customer " +
                customer.toString());
        ProducerRecord<String, science.mengxin.java.kafka.guide.model.avro.Customer> record =
                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE,
                        String.valueOf(customer.getId()), customer);
        when:
        String result1 = producerAvro.send(record).get();

        then:
        result1

        when:
        // if not set, key, the data will be set random!
        science.mengxin.java.kafka.guide.model.avro.Customer customer2 = new science.mengxin.java.kafka.guide.model.avro.Customer();
        customer2.setEmail("xx@gmail.com")
        customer2.setName("xx")
        ProducerRecord<String, science.mengxin.java.kafka.guide.model.avro.Customer> record2 =
                new ProducerRecord<>(KafkaGuideConst.TOPIC_GUIDE, customer2);
        String result2 = producerAvro.send(record2).get();

        then:
        result2

    }
}