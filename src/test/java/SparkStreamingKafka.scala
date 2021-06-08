import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName : SparkStreamingKafka
 * @Description : ${description}
 * @Author : douyonghou
 * @Date: 2021-04-21 20:42
 */
object SparkStreamingKafka {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("aiqiyi")
      .master("local[*]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()
    val sc = spark.sparkContext
    val checkpointDir = "D:\\code\\weather-route\\checkpointDir"
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(60))
    ssc.checkpoint(checkpointDir)
    val topics = Array("NowWeatherData")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "192.168.2.2:9092,192.168.2.3:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "NowWeather",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    val resultDStream = stream.map(x=>x.value())
    resultDStream.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
