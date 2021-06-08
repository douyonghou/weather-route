package common

import scala.util.control.Exception.ignoring

/**
 *
 * 借贷模式
 */
trait Borrow {

  /**
   *
   * 声明一个类型，这个类型必须包含close方法
   */
  type closeable = { def close(): Unit }

  /**
   *
   * @param resource 资源
   * @param execute 使用资源的函数
   * @tparam R closeable类型的子类型
   * @tparam T 返回类型
   * @return
   */
  def using[R <: closeable, T](resource: R)(execute: R => T): T = {
    try {
      // 一个函数，参数是resource
      execute(resource)
    } finally {
      // 使用资源完毕后自动释放资源
      ignoring(classOf[Throwable]) apply resource.close()
    }
  }
}
