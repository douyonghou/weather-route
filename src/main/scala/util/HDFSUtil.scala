package util

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.TaskContext
import scala.collection.mutable.ArrayBuffer

object HDFSUtil {
  /**
   * 为了减少SPARK分区写小文件的生成,可以按照此种策略创建文件并追加写入
   *
   * @param path  写目录
   * @param value 阈值，达到此大小则新建文件
   * @return
   */
  def getPartitionFile(fs: FileSystem, path: String, value: Long = 128 * 1024 * 1024L): String = {
    val catalog = if (path.endsWith("/")) path else path.concat("/")
    val prefix = "000000" + "_"
    val list = fs.listFiles(new Path(catalog), false)
    val index = new ArrayBuffer[Int]()
    while (list.hasNext) {
      val r = (prefix + "([1-9]\\d*$)").r
      list.next().getPath.getName match {
        case r(num) => index.append(num.toInt)
        case _ =>
      }
    }
    val fillPath = catalog + prefix

    if (index.isEmpty) {
      val fullPath = fillPath + 1
      fs.createNewFile(new Path(fullPath))
      fullPath
    } else {
      val fullPath = fillPath + index.max
      val file = fs.getFileStatus(new Path(fullPath))

      // 达到阈值创建一个新文件
      if (file.getLen > value) {
        val fullPath = fillPath + (index.max + 1)
        fs.createNewFile(new Path(fullPath))
      }
      fullPath
    }
  }

}
