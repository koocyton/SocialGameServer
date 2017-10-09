/*
 * 在 close 前， 需要注销（deregister）jdbc驱动
 * 不然会内存泄漏
 * 所以需要自己指定
 */

package com.doopp.gauss.server.database;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SafeBasicDataSource extends BasicDataSource {

    @Override
    public synchronized void close() throws SQLException {
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        super.close();
    }
}
