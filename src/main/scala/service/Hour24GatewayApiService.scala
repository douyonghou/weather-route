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
object Hour24GatewayApiService {
  /**
   * 当前日期
   */
  val now: Date = new Date()
  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
  val date: String = dateFormat.format(now)

  /**
   * 未来天气查询：24小时预报
   * @param area
   * @return：json
   */
  def get24HourWentyData(area: String = "迎泽区"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-8", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody)

    resBody
  }

  def main(args: Array[String]): Unit = {
//    println(get24HourWentyData())
   val ss = "111" +"\u0001"+ date
    println(date)
    println(ss.split("\u0001",-1).head)
    println(ss.split("\u0001")(1))

  }

}
