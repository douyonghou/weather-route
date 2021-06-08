package service

import java.text.SimpleDateFormat
import java.util.Date

import bean.{Content, ResJson, ResNowJson}
import com.alibaba.fastjson.JSON
import com.show.api.ShowApiRequest

/**
 * @ClassName : NowGatewayApiService
 * @Description : 查询当前天气
 * @Author : bs
 * @Date: 2021-04-19 22:03
 */
object NowGatewayApiService {
  /**
   * 当前日期
   */
  val now: Date = new Date()
  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHH")
  val date = dateFormat.format(now)



  /** *
   * 地名-->查询当前天气
   * 返回：json
   */
  def getNowWeatherData(area: String =  "迎泽区"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-2", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody)
    resBody
  }

  def main(args: Array[String]): Unit = {
    getNowWeatherData()
  }

}
