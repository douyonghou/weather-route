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
object HistoryGatewayApiService {
  /**
   * 当前日期
   */
  val now: Date = new Date()
  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHH")
  val date = dateFormat.format(now)

  /**
   * 历史数据查询
   * @param area：地区
   * @param month：数据日期
   * @param startDate：开始时间
   * @param endDate：结束时间
   * @return：json
   */
  def getHistoryFortyData(area: String = "兴县", month:String = "202104", startDate:String = "20210401", endDate:String = "20210405"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-7", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .addTextPara("month",month)
      .addTextPara("startDate",startDate)
      .addTextPara("endDate",endDate)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody)
    resBody +"\u0001"+ date
  }

  def main(args: Array[String]): Unit = {
    getHistoryFortyData()
  }

}
