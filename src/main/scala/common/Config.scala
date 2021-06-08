package common

import java.io.File

import bean.{Dbp, Ssp, Weather}
import util.PropUtil

/**
 *
 * 配置参数对象
 * 包含所有的配置项
 */
object Config {

  /**
   *
   * 加载配置文件
   */
  PropUtil.load("dev")

  /**
   *
   * 采报系统-连接池名称
   */
  private val sPoolName: String = PropUtil.getString("source.db.pool_name")
  /**
   *
   * 采报系统-驱动类
   */
  private val sDriver: String = PropUtil.getString("source.db.driver")
  /**
   *
   * 采报系统-url地址
   */
  private val sUrl: String = PropUtil.getString("source.db.url")
  /**
   *
   * 采报系统-用户名
   */
  private val sUsername: String = PropUtil.getString("source.db.username")
  /**
   *
   * 采报系统-密码
   */
  private val sPassword: String = PropUtil.getString("source.db.password")

  /**
   *
   * hive-连接池名称
   */
  private val tPoolName: String = PropUtil.getString("hive.db.pool_name")
  /**
   *
   * hive-驱动类
   */
  private val tDriver: String = PropUtil.getString("hive.db.driver")
  /**
   *
   * hive-url地址
   */
  private val tUrl: String = PropUtil.getString("hive.db.url")
  /**
   *
   * hive-用户名
   */
  private val tUsername: String = PropUtil.getString("hive.db.username")
  /**
   *
   * hive-密码
   */
  private val tPassword: String = PropUtil.getString("hive.db.password")

  /**
   *
   * 指标系统-连接池名称
   */
  private val iPoolName: String = PropUtil.getString("index.db.pool_name")
  /**
   *
   * 指标系统-驱动类
   */
  private val iDriver: String = PropUtil.getString("index.db.driver")
  /**
   *
   * 指标系统-url地址
   */
  private val iUrl: String = PropUtil.getString("index.db.url")
  /**
   *
   * 指标系统-用户名
   */
  private val iUsername: String = PropUtil.getString("index.db.username")
  /**
   *
   * 指标系统-密码
   */
  private val iPassword: String = PropUtil.getString("index.db.password")

  /**
   *
   * 采报系统-连接配置
   */
  val sDbp = Dbp(sPoolName, sDriver, sUrl, sUsername, sPassword)
  /**
   *
   * hive-连接配置
   */
  val tDbp = Dbp(tPoolName, tDriver, tUrl, tUsername, tPassword)
  /**
   *
   * 指标系统-连接配置
   */
  val iDbp = Dbp(iPoolName, iDriver, iUrl, iUsername, iPassword)

  /**
   *
   * kafka-bootstrap.server参数属性值
   */
  private val bootstrap: String = PropUtil.getString("kafka.bootstrap.server")
  /**
   *
   * kafka-zookeeper.server参数属性值
   */
  private val zk: String = PropUtil.getString("kafka.zookeeper.server")
  /**
   *
   * kafka-消费者
   */
  private val groupId: String = PropUtil.getString("kafka.consume.group")
  /**
   *
   * kafka-主题
   */
  private val topics: Array[String] = PropUtil.getString("kafka.consume.topic")
    .split(",")
  /**
   *
   * spark-streaming 消费时间间隔
   */
  private val duration: Int = PropUtil.getInt("kafka.consume.interval")

  /**
   *
   * 封装spark-streaming所需参数
   */
  val ssp = Ssp(bootstrap, zk, groupId, topics, duration) // kafka信息

  /**
   *
   * 天气接口的url
   */
  val url: String = PropUtil.getString("api.weather.url")
  /**
   *
   * 天气接口的url
   */
  val appId: String = PropUtil.getString("api.appId")
  /**
   *
   * 天气接口的url
   */
  val appSecret: String = PropUtil.getString("api.appSecret")

  /**
   *
   *
   */
  val weather = Weather(url,appId,appSecret)

  val ORG_ID = "org_id"
  val START_STDAT = "start_stdat"
  val END_STDAT = "end_stdat"
  val UPDATE_TIME = "update_time"
  val DELETED = "is_delete"

  /**
   *
   * HDFS HA 配置
   */
  val NAME_SERVICES: String = PropUtil.getString("dfs.nameservices")
  val HA_NODE: String = PropUtil.getString("dfs.ha.namenodes")
  val HA_NODE_1_IP: String = PropUtil.getString("dfs.namenode.rpc-address.1")
  /**
   *
   * hdfs client 配置
   */

  val HA_NODE_2_IP: String = PropUtil.getString("dfs.namenode.rpc-address.2")

  //hive
  val HIVE_META: String = PropUtil.getString("hive.meta.uris")
  val HIVE_DB: String = PropUtil.getString("hive.db.name")
  // hive表的行分隔符
  val HIVE_SEPARATOR: String = PropUtil.getString("hive.db.separator")

  /**
   *
   * hbase 连接参数
   * hbase.zookeeper.quorum属性值
   */
  val ZOOKEEPER_QUORUM: String = PropUtil.getString("hbase.zookeeper.quorum")
  /**
   *
   * hbase 连接参数
   * hbase.zookeeper.property.clientPort属性值
   */
  val ZOOKEEPER_CLIENT_PORT: String = PropUtil.getString("hbase.zookeeper.property.clientPort")
  /**
   *
   * hbase 连接参数
   * hbase.zookeeper.znode.parent属性值
   */
  val ZOOKEEPER_ZNODE_PARENT: String = PropUtil.getString("hbase.zookeeper.znode.parent")

  /**
   *
   * 指标存储表使用的namespace
   */
  val namespace: String = PropUtil.getString("index.namespace")
  /**
   *
   * 指标存储表。存储指标计算的结果值。
   * 按计算周期分别创建不同的存储表
   * 存储表 - 日
   */
  val dayTable: String = PropUtil.getString("index.day.table")
  /**
   *
   * 存储表 - 月
   */
  val monthTable: String = PropUtil.getString("index.month.table")
  /**
   *
   * 存储表 - 季度
   */
  val quarterTable: String = PropUtil.getString("index.quarter.table")
  /**
   *
   * 存储表 - 年
   */
  val yearTable: String = PropUtil.getString("index.year.table")

  /**
   *
   * 同步失败日志记录表
   */
  val dsErrorLogTable: String = PropUtil.getString("ds.error.log.table")

  /**
   *
   * 涉及到的文件的路径
   * 当在jar中运行时候, 这个目录指的是jar所在的同级目录
   * 项目根目录
   */
  val homePath: String = System.getProperty("user.dir") + File.separator
  /**
   *
   * 根目录下的data子目录的路径
   */
  val dataPath: String = homePath + "data" + File.separator
  /**
   *
   * 根目录下的data子目录下的dict文件夹的路径
   * 存储字典数据
   */
  val dictBasePath: String = dataPath + "dict" + File.separator
  /**
   *
   * 根目录下的data子目录下的unit文件夹的路径
   * 存储编制序列数据
   */
  val unitBasePath: String = dataPath + "unit" + File.separator
  /**
   *
   * 根目录下的data子目录下的item文件夹的路径
   * 存储数据项，支撑数据项数据
   */
  val itemBasePath: String = dataPath + "item" + File.separator
  /**
   * 根目录下的org子目录下的部队编制文件夹的路径
   * 存部队编制序列表路径
   */
  val formqSeqPath: String = dataPath + "org" + File.separator
  /**
   *
   * 涉及指标后台系统的表
   * 字典schema表
   */
  val dictSchemaTableName = "dictionary_schema"
  /**
   *
   * 字典数据表
   */
  val dictDataTableName = "dictionary_data"
  /**
   *
   * 量纲表
   */
  val unitTableName = "measurement_unit_info"
  /**
   *
   * 数据项表
   */
  val dataItemTableName = "base_data_table_info"
  /**
   *
   * 支撑数据项表
   */
  val indexItemTableName = "base_data_item_info"
  /**
   *
   * hive存储在HDFS上的路径
   */
  val hdfsHivePath = "/apps/hive/warehouse/"
//  val defaultFS : String = PropUtil.getString("fs.defaultFS")
//  val replaceDatanode : String = PropUtil.getString("replace-datanode")
//  val support : String = PropUtil.getString("dfs.support.append")
//  val HDFS = HDFS(defaultFS, replaceDatanode, support)
//  HDFS

  def main(args: Array[String]): Unit = {
    println(Config.iDbp.poolName)
  }
}
