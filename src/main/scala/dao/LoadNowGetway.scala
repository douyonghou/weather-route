package dao

import bean.Content
import common.Config
import scalikejdbc.SQL
import util.DbPool
import java.util

/**
 * @ClassName : LoadGetwayApi
 * @Description : 入库数据
 * @Author : bs
 * @Date: 2021-04-19 22:17
 */
object LoadNowGetway {
  /**
   * 入库当前天气预报
   * @param
   */
  def loadDataNow(map: Map[String, String]): Unit = {

    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
      DbPool.usingDb(Config.iDbp.poolName) { db =>
        db.autoCommit { implicit session =>
          val sql =
            s"""
               |INSERT INTO weather.NowWeather
               |(
               |time,
               |areaCode,
               |areaName,
               |aqi,
               |rain,
               |sd,
               |temperature,
               |temperaturetime,
               |weather,
               |weatherpic,
               |winddirection,
               |windpower,
               |areaDetail,
               |c7,
               |f1
               |)
               |VALUES(
               |'${map("time")}',
               |'${map("areaCode")}',
               |'${map("areaName")}',
               |'${map("aqi")}',
               |'${map("rain")}',
               |'${map("sd")}',
               |'${map("temperature")}',
               |'${map("temperaturetime")}',
               |'${map("weather")}',
               |'${map("weatherpic")}',
               |'${map("winddirection")}',
               |'${map("windpower")}',
               |'${map("areaDetail")}',
               |'${map("c7")}',
               |'${map("f1")}'
               |)
               |ON DUPLICATE KEY UPDATE
               |time = '${map("time")}',
               |areaCode = '${map("areaCode")}',
               |areaName = '${map("areaName")}',
               |aqi = '${map("aqi")}',
               |rain = '${map("rain")}',
               |sd = '${map("sd")}',
               |temperature = '${map("temperature")}',
               |temperaturetime = '${map("temperaturetime")}',
               |weather = '${map("weather")}',
               |weatherpic = '${map("weatherpic")}',
               |winddirection = '${map("winddirection")}',
               |windpower = '${map("windpower")}',
               |areaDetail = '${map("areaDetail")}',
               |c7 = '${map("c7")}',
               |f1 = '${map("f1")}'
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
