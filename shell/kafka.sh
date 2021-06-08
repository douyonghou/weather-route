sh kafka-topics.sh --create --topic NowWeatherData --bootstrap-server 192.168.2.2:9092 --replication-factor 2 --partitions 3
sh kafka-topics.sh --describe --topic NowWeatherData --bootstrap-server 192.168.2.2:9092
sh kafka-console-consumer.sh --topic NowWeatherData --from-beginning --bootstrap-server 192.168.2.2:9092
sh kafka-topics.sh --create --topic Hour24WeatherData --bootstrap-server 192.168.2.2:9092 --replication-factor 2 --partitions 3
sh kafka-console-consumer.sh --topic Day40WeatherData --from-beginning --bootstrap-server 192.168.2.2:9092
sh kafka-topics.sh --create --topic Day40WeatherData --bootstrap-server 192.168.2.2:9092 --replication-factor 2 --partitions 3
sh kafka-console-consumer.sh --topic Day15WeatherData --from-beginning --bootstrap-server 192.168.2.2:9092