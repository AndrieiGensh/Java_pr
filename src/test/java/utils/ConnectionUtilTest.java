package utils;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ConnectionUtilTest {

    @Test
    public void connDB() throws SQLException {
        Connection con = ConnectionUtil.connDB();
        Assert.assertNotNull(con);

        String statement = "SELECT * FROM users";
        PreparedStatement preparedStatement = null;
        preparedStatement = con.prepareStatement(statement);
        ResultSet resultSet = preparedStatement.executeQuery();

        Assert.assertTrue(resultSet.next());
    }
}