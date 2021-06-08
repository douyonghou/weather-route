package application


import bean.{Future24HourBean, FutureHour, FutureHourJson}
import com.alibaba.fastjson.JSON
import common.Config
import dao.LoadHour24Getway
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import util.DbPool

/**
 * @ClassName : NowDsMysql
 * @Description : 未来24小时天气预报
 * @Date: 2021-04-21 21:07
 */
object Hour24DsMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Hour24DsMysqlStreaming")
      .master("local[*]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()
    val sc = spark.sparkContext
    val checkpointDir = "D:\\code\\weather-route\\checkpointDir"
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(10))
    ssc.checkpoint(checkpointDir)
    val topics = Array("Hour24WeatherData")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "192.168.2.2:9092,192.168.2.3:9092,192.168.2.4:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "NowWeather",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    DbPool.initHikariCP(Config.iDbp)
    val resultDStream: DStream[String] = stream.map(x => x.value())
    resultDStream.foreachRDD(rdd =>
      rdd.foreachPartition(part =>
        part.foreach(line => {
          if (line.length > 10){

            println(line)
            val futureHour: FutureHourJson = JSON.parseObject(line, classOf[FutureHourJson])
            val futurehourList: java.util.List[Future24HourBean] = JSON.parseArray(futureHour.hourList, classOf[Future24HourBean])
            (0 to futurehourList.size() - 1).map(i =>{
              val future24hour = futurehourList.get(i)
              println(future24hour)
              val map = Map(
                "time" -> future24hour.time,
                "areaCode" -> futureHour.areaCode,
                "areaName" -> futureHour.area,
                "weather" -> future24hour.weather,
                "temperature" -> future24hour.temperature,
                "winddirection" -> future24hour.wind_direction,
                "windpower" -> future24hour.wind_power
              )
              LoadHour24Getway.loadDataFuture24Hour(map)
            })
          }


          // 插入mysql中

          //
          //          println(nowBean.time)
          //          println(nowBean.now)
          //          println(nowBean.f1)
          //          println(nowBean.cityInfo)
          //          val cityInfo: CityInfo = JSON.parseObject(nowBean.cityInfo, classOf[CityInfo])
          //          println(cityInfo.c0)
          //          println(cityInfo.c3)

          //
          //
        }
        )
      )
    )

    ssc.start()
    ssc.awaitTermination()

  }
}
