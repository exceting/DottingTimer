/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.debug.db.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author sunqinwen
 * @version \: ConnectionPool.java,v 0.1 2018-11-16 15:23
 * 仅用于本地 debug = true 的测试，数据源为mysql，生产环境不建议使用mysql
 */
public class ConnectionPool {

    private Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    public static ConnectionPool connectionPool;

    private DruidDataSource dataSource;

    private ConnectionPool() {
    }

    public ResultSet getResult(String sql) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("dotting tracer connection pool get result error! sql={}", sql, e);
        } finally {
            if(statement != null){
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    logger.error("dotting tracer connection pool close statement error! sql={}", sql, e);
                }
            }
        }
        return null;
    }

    public int writeResult(String sql) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("dotting tracer connection pool write result error! sql={}", sql, e);
        } finally {
            if(statement != null){
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    logger.error("dotting tracer connection pool close statement error! sql={}", sql, e);
                }
            }
        }
        return 0;
    }

    public static void initConnectionPool(String driver, String user, String password, String url) {
        connectionPool = new ConnectionPool().setDataSource(driver, user, password, url);
    }

    private ConnectionPool setDataSource(String driver, String user, String password, String url) {
        this.dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(5);
        dataSource.setMaxWait(2000);
        dataSource.setTestOnBorrow(true);
        return this;
    }

}
