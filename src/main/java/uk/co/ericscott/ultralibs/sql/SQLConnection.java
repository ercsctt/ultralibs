package uk.co.ericscott.ultralibs.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import uk.co.ericscott.ultralibs.sql.credentials.Credentials;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {

    private HikariDataSource dataSource;
    private Credentials credentials;
    private int minimumConnections;
    private int maximumConnections;
    private long connectionTimeout;

    public SQLConnection(Credentials credentials) {
        this.credentials = credentials;

        this.init();
        this.setupPool();
    }

    private void init() {
        this.minimumConnections = 0;
        this.maximumConnections = 10;
        this.connectionTimeout = 10000L;
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + this.credentials.getHost() + ":" + this.credentials.getPort() + "/" + this.credentials.getDatabase());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(this.credentials.getUsername());
        config.setPassword(this.credentials.getPassword());
        config.setMinimumIdle(this.minimumConnections);
        config.setMaximumPoolSize(this.maximumConnections);
        config.setConnectionTimeout(this.connectionTimeout);

        try {
            this.dataSource = new HikariDataSource(config);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.sql.Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void close(java.sql.Connection conn, Statement statement, ResultSet res) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException ex) {}
        }

        if (statement != null) {
            try {
                statement.close();
            }
            catch (SQLException ex2) {}
        }

        if (res != null) {
            try {
                res.close();
            }
            catch (SQLException ex3) {}
        }
    }

    public void close(java.sql.Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException ex) {}
        }

        if (ps != null) {
            try {
                ps.close();
            }
            catch (SQLException ex2) {}
        }

        if (res != null) {
            try {
                res.close();
            }
            catch (SQLException ex3) {}
        }
    }

    public void closePool() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }

}
