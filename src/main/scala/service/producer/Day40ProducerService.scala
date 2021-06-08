package service.producer

import java.util.Properties

import common.Config
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import scalikejdbc.SQL
import service.producer.Day15ProducerService.nowWeatherProducer
import service.{Day40GatewayApiService, Hour24GatewayApiService}
import util.DbPool

/**
 * @ClassName : Hour24ProducerService
 * @Description : 未来24小时
 * @Author : douyonghou
 * @Date: 2021-04-21 22:14
 */
object Day40ProducerService {
  /**
   * 生产者发送未来24小时到topic
   *
   * @param producer
   * @return
   */
  def nowWeatherProducer(producer: KafkaProducer[String, String], area: String = "盂县"): scala.collection.mutable.HashMap[String, String] = {
    var metadataMap = new scala.collection.mutable.HashMap[String, String]()
    val day40WeatherJson = Day40GatewayApiService.get40DayWentyData(area)
    val record: ProducerRecord[String, String] = new ProducerRecord[String, String]("Day40WeatherData", day40WeatherJson)
    try {
      val value = producer.send(record)
      val metadata: RecordMetadata = value.get()
      metadataMap += ("offset" -> metadata.offset().toString)
      metadataMap += ("partition" -> metadata.partition().toString)
      metadataMap += ("topic" -> metadata.topic())
      metadataMap += ("checksum" -> metadata.checksum().toString)
      (metadata.offset().toString, metadata.partition().toString, metadata.topic(), metadata.checksum().toString)
      metadataMap
    } catch {
      case e: Exception => {
        e.printStackTrace()
        metadataMap
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val kafkaProps = new Properties();
    kafkaProps.put("bootstrap.servers", "192.168.2.2:9092, 192.168.2.3:9092");
    kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](kafkaProps)
    DbPool.initHikariCP(Config.iDbp)
    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
    val list: List[(String, String, String)] = DbPool.usingDb(Config.iDbp.poolName) { db =>
      val res = db.autoCommit { implicit session =>
        val sql =
          s"""
             |select * from City where pid in (select id from City where pid in('140000', '230000')) ;
             |""".stripMargin
        println(sql)
        SQL(sql).map(rs => (
          rs.string("id"),
          rs.string("name"),
          rs.string("pid")
        )).list().apply()
      }
      res
    }
    list.foreach(line =>{
      nowWeatherProducer(producer, line._2)
    } )
  }
}
