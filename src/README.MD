

```bash
docker run -e ADV_HOST=127.0.0.1 -e EULA="https://dl.lenses.stream/d/?id=91a1302c-7fef-490b-ae60-3c187a44e7f1" --rm -p 3030:3030 -p 9092:9092 -p 2181:2181 -p 8081: 8081 -p 9581:9581 -p 9582:9582 -p 9584:9584 -p 9585:9585 landoop/kafka-lenses-dev

docker stop jolly_davinci

```



```bash
# ********************************************************************************
# You may visit http://127.0.0.1:3030 in about 45 seconds. Login with admin/admin.
# ********************************************************************************
# The services (kafka and lenses) need some time to start-up.
# The broker is accessible at PLAINTEXT://127.0.0.1:9092, Schema Registry at http://127.0.0.1:8081 and Zookeeper at 127.0.0.1:2181.
# For documentation please refer to -> https://lenses.stream/dev/lenses-box/
# If you have trouble running the image or want to give us feedback (or a rant), come chat with us at https://gitter.im/Landoop/support
```