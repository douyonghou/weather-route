package common

import bean.Ssp
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 封装StreamingContext
 * @param ssp spark-streaming所需参数
 */
class SsMain(ssp: Ssp) {

  /**
   *
   * createDirectStream需要的kafka参数
   */
  val kafkaParam: Map[String, Object] = Map[String,Object](
    "bootstrap.servers" -> ssp.bootstrap,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id"-> ssp.groupId,
    "enable.auto.commit" -> (false:java.lang.Boolean)
  )

  /**
   *
   * @param ssc spark-streaming所需参数
   * @param process spark-streaming处理函数
   */
  def execute(ssc: StreamingContext)(process: InputDStream[ConsumerRecord[String,String]] => Unit): Unit = {

    val input = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](ssp.topics, kafkaParam)
    )
    // 处理批次的InputDStream
    process(input)
    //
    input.foreachRDD{ rdd =>
      if(!rdd.isEmpty()) {
        // 获取偏移量
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        // 处理完该批次的InputDStream后保存当前的偏移量
      }
    }
    ssc.start()
    ssc.awaitTermination()
  }

  /**
   *
   * @param conf conf
   * @param process 处理函数
   */
  def execute(conf: SparkConf)(process: InputDStream[ConsumerRecord[String,String]] => Unit): Unit = {
    val ssc = new StreamingContext(conf, Seconds(ssp.duration))
    execute(ssc)(process)
  }

  /**
   *
   * @param sparkSession sparkSession
   * @param process 处理函数
   */
  def execute(sparkSession: SparkSession)(process: InputDStream[ConsumerRecord[String,String]] => Unit): Unit = {
    val sc = sparkSession.sparkContext
    val ssc = new StreamingContext(sc, Seconds(ssp.duration))
    execute(ssc)(process)
  }
}