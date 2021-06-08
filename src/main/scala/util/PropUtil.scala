package util

import java.util.Properties

/**
 *
 * 读取配置文件工具类
 */
object PropUtil {

  private val properties = new Properties()

  /**
   *
   * 加载resources目录下的properties文件
   * @param fileName
   */
  def load(fileName:String):Unit = {
    if(fileName.nonEmpty) {
      val suffix = ".properties"
      val prop = if(fileName.endsWith(suffix)) fileName else fileName + suffix
      val stream = this.getClass.getResourceAsStream("/"+prop)
      properties.load(stream)
    }
  }

  /**
   *
   * 获取配置项值为String类型
   * @param key
   * @return
   */
  def getString(key: String): String = properties.getProperty(key)

  /**
   *
   * 获取配置项值为Int类型
   * @param key
   * @return
   */
  def getInt(key: String): Int = properties.getProperty(key).toInt
}
