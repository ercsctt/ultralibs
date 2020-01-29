package uk.co.ericscott.ultralibs.sql.callback;

import org.bukkit.scheduler.BukkitRunnable;
import uk.co.ericscott.ultralibs.sql.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryRunnable extends BukkitRunnable {

    private String statement;
    private QueryCallback<ResultSet, SQLException> callback;
    private SQLConnection sqlConnection;

    public QueryRunnable(SQLConnection sqlConnection, String statement, QueryCallback<ResultSet, SQLException> callback) {
        this.sqlConnection = sqlConnection;
        this.statement = statement;
        this.callback = callback;
    }

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = sqlConnection.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            callback.call(resultSet, null);
        }
        catch (SQLException e) {
            callback.call(null, e);
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}