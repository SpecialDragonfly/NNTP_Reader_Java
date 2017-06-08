package reader;

import java.sql.*;

public class MysqlConnection {

    private String host;
    private int port;
    private String dbName;
    private String username;
    private String password;
    private Connection connection;

    public MysqlConnection(String host, int port, String dbName, String username, String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(this.host);
        sb.append(":");
        sb.append(this.port);
        sb.append("/");
        sb.append(this.dbName);

        this.connection = DriverManager.getConnection(sb.toString(), this.username, this.password);
    }

    public Connection getConnection() {
        return this.connection;
    }
}
