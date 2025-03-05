
sh kafka-topics.sh --bootstrap-server kafka1:19092, kafka2:19092, kafka3:19092 --create --topic global-message-topic --partitions 3 --replication-factor 2

sh kafka-console-consumer.sh --bootstrap-server kafka1:19092, kafka2:19092, kafka3:19092 --topic global-message-topic --from-beginning