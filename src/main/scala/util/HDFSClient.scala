package util

import java.io.ByteArrayInputStream

import common.{Borrow, Logging}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.IOUtils

/**
 *
 * HDFS操作工具类
 * @param func
 */
class HDFSClient(func:() => FileSystem) extends Borrow with Logging with Serializable {

  lazy val fs: FileSystem = func()
//  val hdfsClient = HDFSClient(HDFSClient.configuration)

  /**
   *
   * 追加写文件
   */
  def append(file: String, content: String, encode: String = "UTF-8"): Unit = {
    val in = new ByteArrayInputStream((content+"\n").getBytes(encode))
    val out = fs.append(new Path(file))
    IOUtils.copyBytes(in, out,4096,true)
  }
}

object HDFSClient extends Logging {

  def apply(conf:Configuration): HDFSClient = {
    val func = () => {
      val fs = FileSystem.get(conf)
      // 添加钩子函数
      sys.addShutdownHook{
        warn(s"Execute hook thread: $name")
        fs.close()
      }
      fs
    }
    new HDFSClient(func)
  }
}
