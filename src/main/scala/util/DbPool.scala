package util

import bean.Dbp
import com.cloudera.hive.jdbc41.HS2DataSource
import com.zaxxer.hikari.HikariDataSource
import common.Borrow
import javax.sql.DataSource
import scalikejdbc.{ConnectionPool, DB, DataSourceConnectionPool}

/**
 *
 * 数据库连接池工具类
 * scalikejdbc
 */
object DbPool extends Borrow {

  /**
   *
   * HiKariCP是数据库连接池的一个后起之秀，号称性能最好，可以完美地PK掉其他连接池。
   *
   * @param dbp
   */
  def initHikariCP(dbp: Dbp): Unit = {
    if (!ConnectionPool.isInitialized(dbp.poolName)) {
      val dataSource: DataSource = {
        val ds = new HikariDataSource()
        ds.setDriverClassName(dbp.driver)
        ds.setJdbcUrl(dbp.url)
        ds.setUsername(dbp.username)
        ds.setPassword(dbp.password)
        // 等待连接池分配连接的最大时长(毫秒),超过这个时长还没可用的连接则发生SQLException,缺省:30秒
        ds.setConnectionTimeout(3000)
        // 一个连接idle状态的最大时长(毫秒),超时则被释放(retired),缺省:10分钟
        ds.setIdleTimeout(600000)
        // 一个连接的生命时长(毫秒),超时而且没被使用则被释放(retired),缺省:30分钟,建议设置比数据库超时时长少30秒
        // 参考MySQL wait_timeout参数(show variables like '%timeout%'; 28800 单位秒 即8个小时))
        ds.setMaxLifetime(1800000)
        // 池中最大连接数，包括闲置和使用中的连接。缺省值：10
        // 推荐的公式：((core_count * 2) + effective_spindle_count)
        ds.setMaximumPoolSize(10)
        // 池中维护的最小空闲连接数
        ds.setMinimumIdle(3)
        ds.setConnectionTestQuery("select 1")
        ds
      }
      ConnectionPool.add(Symbol(dbp.poolName), new DataSourceConnectionPool(dataSource))
    }
  }

  /**
   *
   * hive 2.2.0 才支持HiKariCP，所以这里使用cdh提供的工具包
   *
   * @param dbp
   */
  def initHive(dbp: Dbp): Unit = {
    if (!ConnectionPool.isInitialized(dbp.poolName)) {
      val dataSource: DataSource = {
        val ds = new HS2DataSource()
        ds.setURL(dbp.url)
        ds.setUserID(dbp.username)
        ds.setPassword(dbp.password)
        ds.setCustomProperty("ConnectionTestQuery", "select 1")
        ds
      }
      ConnectionPool.add(Symbol(dbp.poolName), new DataSourceConnectionPool(dataSource))
    }
  }

  /**
   *
   * 关闭连接池
   *
   * @param dbp
   */
  def close(dbp: Dbp): Unit = ConnectionPool.close(dbp.poolName)

  /**
   *
   * 使用连接，使用完毕后释放连接
   *
   * @param poolName
   * @param execute
   * @tparam A
   * @return
   */
  def usingDb[A](poolName: String)(execute: DB => A): A = using(DB(ConnectionPool(Symbol(poolName)).borrow()))(execute)
}
