/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import dotting.timer.ui.po.Span;
import dotting.timer.ui.utils.Page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author sunqinwen
 * @version \: ConnectionPool.java,v 0.1 2018-11-16 15:23
 * 仅用于本地 debug = true 的测试，数据源为mysql，生产环境不建议使用mysql
 */
@Component
public class ConnectionPool {

    private Logger logger = LoggerFactory.getLogger(dotting.timer.ui.db.ConnectionPool.class);

    public static dotting.timer.ui.db.ConnectionPool connectionPool;

    private DruidDataSource dataSource;

    private ConnectionPool() {
    }

    public Span getResult(String sql) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return Span.getSpanObjByRs(rs);
        } catch (SQLException e) {
            logger.error("tree tracer connection pool get result error! sql={}", sql, e);
        } finally {
            if(statement != null){
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    logger.error("tree tracer connection pool close statement error for get result! sql={}", sql, e);
                }
            }
        }
        return null;
    }

    public List<Span> getResults(String sql) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return Span.getSpanObjsByRs(rs);
        } catch (SQLException e) {
            logger.error("tree tracer connection pool get results error! sql={}", sql, e);
        } finally {
            if(statement != null){
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    logger.error("tree tracer connection pool close statement error for get results! sql={}", sql, e);
                }
            }
        }
        return null;
    }

    public void getPageResults(Page<Span> page, String sql, String sqlCount) {
        Statement statement = null;
        Statement statementCount = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statementCount = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSet rsCount = statementCount.executeQuery(sqlCount);
            rsCount.next();
            page.setTotalCount(rsCount.getInt("count"));
            page.setRecords(Span.getSpanObjsByRs(rs));
        } catch (SQLException e) {
            logger.error("tree tracer connection pool get page results error! sql={}", sql, e);
        } finally {
            if(statement != null){
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    logger.error("tree tracer connection pool close statement error for get page results! sql={}", sql, e);
                }
            }
        }
    }

    public static void initConnectionPool() {
        connectionPool = new dotting.timer.ui.db.ConnectionPool().setDataSource();
    }

    private dotting.timer.ui.db.ConnectionPool setDataSource() {
        this.dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("sun123456");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/tree_tracer?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&zeroDateTimeBehavior=convertToNull");
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(5);
        dataSource.setMaxWait(2000);
        dataSource.setTestOnBorrow(true);
        return this;
    }

}
