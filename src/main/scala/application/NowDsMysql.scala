package application

import bean.{CityInfo, FutureWeather, Now}
import com.alibaba.fastjson.JSON
import common.Config
import dao.LoadNowGetway
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.DbPool

/**
 * @ClassName : NowDsMysql
 * @Description : sparkStreaming实时流到mysql,消费当前天气数据
 * @Author :
 * @Date: 2021-04-21 21:07
 */
object NowDsMysql {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("NowDsMysqlStreaming")
      .master("local[*]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()
    val sc = spark.sparkContext
    val checkpointDir = "D:\\code\\weather-route\\checkpointDir"
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(10))
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
            val nowBean: Now = JSON.parseObject(line, classOf[Now])

            println(nowBean.time)
            println(nowBean.now)
            // 解析now里面的数据
            val futureWeather: FutureWeather = JSON.parseObject(nowBean.now, classOf[FutureWeather])
            println(nowBean.f1)
            println(nowBean.cityInfo)
            val cityInfo: CityInfo = JSON.parseObject(nowBean.cityInfo, classOf[CityInfo])
            println(cityInfo.c0)
            println(cityInfo.c3)
            val map = Map(
              "time" -> nowBean.time,
              "areaCode" -> cityInfo.c0,
              "areaName" -> cityInfo.c3,
              "aqi" -> futureWeather.aqi,
              "rain" -> futureWeather.rain,
              "sd" -> futureWeather.sd,
              "temperature" -> futureWeather.temperature,
              "temperaturetime" -> futureWeather.temperature_time,
              "weather" -> futureWeather.weather,
              "weatherpic" -> futureWeather.weather_pic,
              "winddirection" -> futureWeather.wind_direction,
              "windpower" -> futureWeather.wind_power,
              "areaDetail" -> futureWeather.aqiDetail.area,//地区
              "c7" -> cityInfo.c7, //所在省
              "f1" -> nowBean.f1
            )
            // 插入mysql中

            LoadNowGetway.loadDataNow(map)
          }
        }
        )
      )
    )

    ssc.start()
    ssc.awaitTermination()

  }
}
