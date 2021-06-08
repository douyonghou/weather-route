package application


import util.DbPool

import bean.{FutureDay15Json, FutureDayBean, FutureDayJson}
import com.alibaba.fastjson.{JSON, JSONArray}
import common.Config
import dao.{LoadDay15Getway, LoadDay40Getway}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

/**
 * @ClassName : NowDsMysql
 * @Description : ${description}
 * @Author : douyonghou
 * @Date: 2021-04-21 21:07
 */
object Day15DsMysql {
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
    val topics = Array("Day15WeatherData")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "192.168.2.2:9092,192.168.2.3:9092",
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
          if(line.length > 10){
            println(line)
            val futureDayJson: FutureDay15Json = JSON.parseObject(line, classOf[FutureDay15Json])
            val futureDayList: java.util.List[FutureDayBean] = JSON.parseArray(futureDayJson.dayList, classOf[FutureDayBean])
            ( 0 to futureDayList.size() - 1 ).map(i =>{
              val futureDay = futureDayList.get(i)
              val map = Map(
                "area" -> futureDay.area,
                "nightwinddirection" -> futureDay.night_wind_direction,
                "nightairtemperature" -> futureDay.night_air_temperature,
                "nightweatherpic" -> futureDay.night_weather_pic,
                "nightweathercode" -> futureDay.night_weather_code,
                "nightweather" -> futureDay.night_weather,
                "dayweathercode" -> futureDay.day_weather_code,
                "dayweather" -> futureDay.day_weather,
                "daywindpower" -> futureDay.day_wind_power,
                "dayweatherpic" -> futureDay.day_weather_pic,
                "daywinddirection" -> futureDay.day_wind_direction,
                "dayairtemperature" -> futureDay.day_air_temperature,
                "areaCode" -> futureDay.areaCode,
                "areaid" -> futureDay.areaid,
                "nightwindpower" -> futureDay.night_wind_power,
                "daytime" -> futureDay.daytime
              )
              LoadDay15Getway.loadDataFuture15Day(map)
            })
          }

        }
        )
      )
    )

    ssc.start()
    ssc.awaitTermination()

  }
}
