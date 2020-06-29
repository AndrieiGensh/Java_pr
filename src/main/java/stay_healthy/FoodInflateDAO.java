package stay_healthy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodInflateDAO implements DAO<FoodModel>
{
    private int user_id;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    private Connection con = null;

    public ObservableList<FoodModel> getAll()
    {
        ObservableList<FoodModel> eaten_food = FXCollections.observableArrayList();

        String statement = "SELECT * FROM products_diary WHERE u_id = ?";
        try
        {
            preparedStatement = con.prepareStatement(statement);
            preparedStatement.setString(1, Integer.toString(this.user_id));
            resultSet = preparedStatement.executeQuery();

            String product_name;
            double how_much;
            String units;

            if(!resultSet.next())
            {
            }
            else
            {

                product_name = resultSet.getString("product_name");
                how_much = resultSet.getDouble("how_much");
                units = resultSet.getString("units");

                FoodModel dish = new FoodModel(product_name, 0.0, 0.0, 0.0, 0.0, units);
                dish.setHow_much(how_much);

                eaten_food.add(dish);

                while(resultSet.next())
                {
                    product_name = resultSet.getString("product_name");
                    how_much = resultSet.getDouble("how_much");
                    units = resultSet.getString("units");

                    dish = new FoodModel(product_name, 0.0, 0.0, 0.0, 0.0, units);
                    dish.setHow_much(how_much);

                    eaten_food.add(dish);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return eaten_food;
    }

    @Override
    public ArrayList<Double> getStats()
    {
        ArrayList<Double> stats = new ArrayList<Double>();

        String statement = "SELECT * FROM daily_stats WHERE user_id = ?";
        try
        {
            preparedStatement = con.prepareStatement(statement);
            preparedStatement.setString(1, Integer.toString(this.user_id));
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
            {
            }
            else
            {
                double kcals_stat = resultSet.getDouble("kcal");
                double proteins_stat = resultSet.getDouble("proteins");
                double fats_stat = resultSet.getDouble("fats");
                double carbons_stat = resultSet.getDouble("carbons");

                stats.add(kcals_stat);
                stats.add(proteins_stat);
                stats.add(fats_stat);
                stats.add(carbons_stat);

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return stats;
    }

    public void save()
    {
    }

    public void update() {

    }

    public void delete() {

    }

    FoodInflateDAO()
    {
        this.con = ConnectionUtil.connDB();
    }

    void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    int getUser_id()
    {
        return this.user_id;
    }

}
