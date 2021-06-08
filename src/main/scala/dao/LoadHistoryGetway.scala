package dao

import org.apache.hadoop.conf.Configuration
import util.{HDFSClient, HDFSUtil}

/**
 * @ClassName : LoadHistoryGetway
 * @Description : 入库数据hive历史数据
 * @Date: 2021-04-19 22:17
 */
object LoadHistoryGetway {


  /**
   * 历史数据写入hive中
   * @param
   */
  def loadDataHistoryDay(path: String = "/user/hive/warehouse/weather.db/historyweather", resBody: String = "141123\u0001兴县\u00012021-04-05\u00012021-04-07\u0001json"): Unit ={
    System.setProperty("HADOOP_USER_NAME", "root")
    val conf: Configuration = new Configuration()
    conf.set("fs.defaultFS", "hdfs://192.168.2.2:9820")
    conf.set("dfs.support.append", "true")
    val client = HDFSClient(conf)
    val resPath = HDFSUtil.getPartitionFile(client.fs, path)
    client.append(resPath,resBody)
  }

  def main(args: Array[String]): Unit = {

  }

}
