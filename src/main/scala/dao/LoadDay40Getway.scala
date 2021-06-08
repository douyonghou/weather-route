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
object LoadDay40Getway {
  /**
   * 写入未来40天气预报数据
   * @param map
   */
  def loadDataFuture40Day(map: Map[String, String]): Unit ={

    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
    DbPool.usingDb(Config.iDbp.poolName) { db =>
      db.autoCommit { implicit session =>
        val sql =
          s"""
             |INSERT INTO weather.Future40Day
             |             (
             |              area,
             |              nightairtemperature,
             |              nightweatherpic,
             |              nightweathercode,
             |              nightweather,
             |              dayweathercode,
             |              dayweather,
             |              daywindpower,
             |              dayweatherpic,
             |              dayairtemperature,
             |              areaCode,
             |              areaid,
             |              nightwindpower,
             |              daytime
             |             )
             |             VALUES(
             |              '${map("area")}',
             |              '${map("nightairtemperature")}',
             |              '${map("nightweatherpic")}',
             |              '${map("nightweathercode")}',
             |              '${map("nightweather")}',
             |              '${map("dayweathercode")}',
             |              '${map("dayweather")}',
             |              '${map("daywindpower")}',
             |              '${map("dayweatherpic")}',
             |              '${map("dayairtemperature")}',
             |              '${map("areaCode")}',
             |              '${map("areaid")}',
             |              '${map("nightwindpower")}',
             |              '${map("daytime")}'
             |             )
             |             ON DUPLICATE KEY UPDATE
             |              area ='${map("area")}',
             |              nightairtemperature = '${map("nightairtemperature")}',
             |              nightweatherpic ='${map("nightweatherpic")}',
             |              nightweathercode ='${map("nightweathercode")}',
             |              nightweather ='${map("nightweather")}',
             |              dayweathercode ='${map("dayweathercode")}',
             |              dayweather ='${map("dayweather")}',
             |              daywindpower ='${map("daywindpower")}',
             |              dayweatherpic ='${map("dayweatherpic")}',
             |              dayairtemperature ='${map("dayairtemperature")}',
             |              areaCode ='${map("areaCode")}',
             |              areaid ='${map("areaid")}',
             |              nightwindpower ='${map("nightwindpower")}',
             |              daytime ='${map("daytime")}'
             |             ;
             |""".stripMargin
        println(sql)
        SQL(sql).update().apply()
      }
    }
  }

}
