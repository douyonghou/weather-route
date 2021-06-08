package dao

import java.text.SimpleDateFormat

import bean.Content
import common.Config
import scalikejdbc.SQL
import util.DbPool
import java.util
import java.util.Date

import com.alibaba.fastjson.JSON

/**
 * @ClassName : LoadGetwayApi
 * @Description : 入库数据
 * @Author : bs
 * @Date: 2021-04-19 22:17
 */
object LoadHour24Getway {

  val now: Date = new Date()
  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
  val date: String = dateFormat.format(now)

  def loadDataFuture24Hour(map: Map[String, String]): Unit ={

    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
    DbPool.usingDb(Config.iDbp.poolName) { db =>
      db.autoCommit { implicit session =>
        val sql =
          s"""
             |INSERT INTO weather.Future24Hour
             |(
             |`time`,
             |areaCode,
             |areaName,
             |weather,
             |temperature,
             |winddirection,
             |windpower
             |)
             |VALUES(
             |'${map("time")}',
             |'${map("areaCode")}',
             |'${map("areaName")}',
             |'${map("weather")}',
             |'${map("temperature")}',
             |'${map("winddirection")}',
             |'${map("windpower")}'
             |)
             |ON DUPLICATE KEY UPDATE
             |time = '${map("time")}',
             |areaCode = '${map("areaCode")}',
             |areaName = '${map("areaName")}',
             |weather = '${map("weather")}',
             |temperature = '${map("temperature")}',
             |winddirection = '${map("winddirection")}',
             |windpower = '${map("windpower")}'
             |;
             |""".stripMargin
        println(sql)
        SQL(sql).update().apply()
      }
    }
  }

  def main(args: Array[String]): Unit = {

  }

}
