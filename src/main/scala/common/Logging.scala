package common

import org.slf4j.{Logger, LoggerFactory}

/**
 *
 * 日志记录
 */
trait Logging {

  /**
   * Logger对象
   * lazy 延迟初始化，第一次使用时才初始化
   */
  private lazy val log: Logger = LoggerFactory.getLogger(name)

  /**
   *
   * @return 类名，去掉$符号
   */
  protected def name: String = this.getClass.getName.stripSuffix("$")

  /**
   *
   * @param msg 记录消息
   */
  protected def debug(msg: => String): Unit = if(log.isDebugEnabled) log.debug(msg)

  /**
   *
   * @param msg 记录消息
   */
  protected def info(msg: => String): Unit = if(log.isInfoEnabled) log.info(msg)

  /**
   *
   * @param msg 记录消息
   */
  protected def warn(msg: => String): Unit = if(log.isWarnEnabled) log.warn(msg)

  /**
   *
   * @param msg 记录消息
   */
  protected def error(msg: => String): Unit = if(log.isErrorEnabled) log.error(msg)

  /**
   *
   * @param msg 记录消息
   * @param e 异常
   */
  protected def error(msg: => String,e: Exception): Unit = if(log.isErrorEnabled) {
    log.error(msg, e)
  }
}
