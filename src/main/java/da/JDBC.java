package main.java.da;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import main.java.Config;

public class JDBC
{
    private static String DRIVERCLASS;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    static
    {
        DRIVERCLASS = Config.JDBC.getValue("jdbc.driverClassName");
        URL = Config.JDBC.getValue("jdbc.url");
        USERNAME = Config.JDBC.getValue("jdbc.username");
        PASSWORD = Config.JDBC.getValue("jdbc.password");
        
        try
        {
            Class.forName(DRIVERCLASS).newInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
    {
        Connection conn = threadLocal.get();
        if (conn == null)
        {
            try
            {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                threadLocal.set(conn);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static boolean closeConnection()
    {
        boolean isClosed = true;
        Connection conn = threadLocal.get();
        threadLocal.set(null);
        if (conn != null)
        {
            try
            {
                conn.close();
            } catch (SQLException e)
            {
                isClosed = false;
                e.printStackTrace();
            }
        }
        return isClosed;
    }
}
