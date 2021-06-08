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
object LoadSceneryGetway {
  /**
   * 入库景点表中
   * @param contentList
   */
  def loadDataScenery(contentList: util.List[Content]): Unit = {
    DbPool.initHikariCP(Config.iDbp)
    //    val contentList: util.List[Content] = GatewayApiService.getSceneryData()
    (0 to contentList.size() - 1).map(i => {
      val content: Content = contentList.get(i)
      DbPool.usingDb(Config.iDbp.poolName) { db =>
        db.autoCommit { implicit session =>
          val sql =
            s"""
               |INSERT INTO weather.Scenery
               |(id, areaId, address, cityId, coupon, ct,
               |summary, areaName, opentime, name, cityName, content,
               |price, attention, proName, proId, location, picList)
               |VALUES('${content.id}','${content.areaId}','${content.address}','${content.cityId}','${content.coupon}','${content.ct}',
               |'${content.summary}','${content.areaName}','${content.opentime}','${content.name}','${content.cityName}','${content.content}',
               |'${content.price}','${content.attention}','${content.proName}','${content.proId}','${content.location}','${content.picList}');
               |""".stripMargin
          println(sql)
          SQL(sql).update().apply()
        }
      }
    })

  }
  def main(args: Array[String]): Unit = {

  }
}
