package dao

import bean.Content
import common.Config
import scalikejdbc.SQL
import util.DbPool
import java.util

import com.alibaba.fastjson.JSON

/**
 * @ClassName : LoadGetwayApi
 * @Description : 入库数据
 * @Author : bs
 * @Date: 2021-04-19 22:17
 */
object LoadDay15Getway {


  /**
   * 写入未来15天气预报数据
   * @param map
   */
  def loadDataFuture15Day(map: Map[String, String]): Unit ={

    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
    DbPool.usingDb(Config.iDbp.poolName) { db =>
      db.autoCommit { implicit session =>
        val sql =
          s"""
             |INSERT INTO weather.Future15Day
             |(
             |daytime,
             |areaCode,
             |area,
             |nightairtemperature,
             |nightweatherpic,
             |nightweathercode,
             |nightweather,
             |dayweathercode,
             |dayweather,
             |daywindpower,
             |dayweatherpic,
             |daywinddirection
             |)
             |VALUES(
             |'${map("daytime")}',
             |'${map("areaCode")}',
             |'${map("area")}',
             |'${map("nightairtemperature")}',
             |'${map("nightweatherpic")}',
             |'${map("nightweathercode")}',
             |'${map("nightweather")}',
             |'${map("dayweathercode")}',
             |'${map("dayweather")}',
             |'${map("daywindpower")}',
             |'${map("dayweatherpic")}',
             |'${map("daywinddirection")}'
             |)
             |ON DUPLICATE KEY UPDATE
             |daytime = '${map("daytime")}',
             |areaCode = '${map("areaCode")}',
             |area = '${map("area")}',
             |nightairtemperature = '${map("nightairtemperature")}',
             |nightweatherpic = '${map("nightweatherpic")}',
             |nightweathercode = '${map("nightweathercode")}',
             |nightweather = '${map("nightweather")}',
             |dayweathercode = '${map("dayweathercode")}',
             |dayweather = '${map("dayweather")}',
             |daywindpower = '${map("daywindpower")}',
             |dayweatherpic = '${map("dayweatherpic")}',
             |daywinddirection = '${map("daywinddirection")}'
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
