package utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection connDB()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con;
            con = DriverManager.getConnection("jdbc:mysql://localhost/java_project_database?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false","root","sqlserver2020");
            return con;

        } catch (Exception e)
        {
            //System.err.println("ConnectionUtil:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
