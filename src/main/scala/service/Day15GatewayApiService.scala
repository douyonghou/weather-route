package service

import java.text.SimpleDateFormat
import java.util.Date

import bean.{Content, ResJson, ResNowJson}
import com.alibaba.fastjson.JSON
import com.show.api.ShowApiRequest

/**
 * @ClassName : GatewayApiService
 * @Description : 调用易用接口数
 * @Author : bs
 * @Date: 2021-04-19 22:03
 */
object Day15GatewayApiService {
  /**
   * 当前日期
   */
  val now: Date = new Date()
  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHH")
  val date = dateFormat.format(now)

  /**
   * 未来天气查询：15天预报
   * @param area
   * @return：json
   */
  def get15DayWentyData(area: String = "迎泽区"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-9", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody +"\u0001"+ date)
    resBody
  }

  def main(args: Array[String]): Unit = {
    get15DayWentyData()
  }

}
