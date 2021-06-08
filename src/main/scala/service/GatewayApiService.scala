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
object GatewayApiService {
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
      .addTextPara("area", "迎泽区")
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    resBody
  }

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

    resBody +"\u0001"+ date
  }

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
    println(resBody)
    resBody +"\u0001"+ date
  }

  /**
   * 未来天气查询：7天预报
   * @param area
   * @return：json
   */
  def get7DayWentyData(area: String = "山西省"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-11", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody)
    resBody +"\u0001"+ date
  }

  /**
   * 未来天气查询：40天预报
   * @param area
   * @return：json
   */
  def get40DayWentyData(area: String = "丽江"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-12", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .post()
    val resNowJson: ResNowJson = JSON.parseObject(res, classOf[ResNowJson])
    val resBody: String = resNowJson.showapi_res_body
    println(resBody)
    resBody +"\u0001"+ date
  }

  /**
   * 历史数据查询
   * @param area：地区
   * @param month：数据日期
   * @param startDate：开始时间
   * @param endDate：结束时间
   * @return：json
   */
  def getHistoryFortyData(area: String = "兴县", month:String = "202101", startDate:String = "20210401", endDate:String = "20210405"): String = {
    val res: String = new ShowApiRequest("http://route.showapi.com/9-12", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("area", area)
      .addTextPara("month",month)
      .addTextPara("startDate",startDate)
      .addTextPara("endDate",endDate)
      .post()
    println(res)
    res +"\u0001"+ date
  }

  /** *
   * 获取api景点数据
   * 返回：json
   */
  def getSceneryData(cityName: String = "山西省"): java.util.List[Content] = {
    val res: String = new ShowApiRequest("http://route.showapi.com/268-1", "576267", "55c832971f644a23bc1025d418df4760")
      .addTextPara("keyword", "山西省")
      .post()
    // 景点数据列表
    val resJson: ResJson = JSON.parseObject(res, classOf[ResJson])
    val contens: String = resJson.showapi_res_body.pagebean.contentlist.replaceAll("\\\\n|\\\\t|\\\\r|\\s+", "")
    // 省粒度的各个景点数据
    val contentList: java.util.List[Content] = JSON.parseArray(contens, classOf[Content])
    return contentList
  }

  def main(args: Array[String]): Unit = {
//    get24HourWentyData()
    println(date)
  }

}
