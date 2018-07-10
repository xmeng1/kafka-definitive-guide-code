package science.mengxin.java.kafka.guide;

public class KafkaGuideConst {

    private KafkaGuideConst() {
    }

    public final static String TOPIC_GUIDE = "guide";
    public final static String HOST = "192.168.230.129";
    public final static String HTTP = "http://";
    public final static String KAFKA_PORT = "9092";
    public final static String SCHEMA_REGISTRY_PORT = "8081";

    public final static String KAFKA_SERVER_URL = HOST + ":" + KAFKA_PORT;
    public final static String SCHEMA_REGISTRY_URL =  HTTP + HOST + ":" + SCHEMA_REGISTRY_PORT;
}
