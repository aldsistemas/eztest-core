package br.com.eztest.core.common

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger

class SimpleDataSource implements DataSource {

    String url
    String username
    String password
    Class driver
    Connection conn
    PrintWriter pw

    @Override
    PrintWriter getLogWriter() throws SQLException {
        return pw == null ? new PrintWriter(System.out) : pw;
    }

    @Override
    void setLogWriter(PrintWriter out) throws SQLException {
        pw = out;
    }

    @Override
    void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    Connection getConnection() throws SQLException {
        if (this.conn == null || this.conn.isClosed()) {
            createConnection();
        }
        return this.conn;
    }

    private void createConnection() {
        Class.forName(driver.getName());
        this.conn = DriverManager.getConnection(this.url, this.username, this.password);
    }


    @Override
    Connection getConnection(String username, String password) throws SQLException {
        return this.conn;
    }
}
