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
                System.out.println("Nothing in the database so far");
            }
            else
            {

                product_name = resultSet.getString("product_name");
                how_much = resultSet.getDouble("how_much");
                units = resultSet.getString("units");

                FoodModel dish = new FoodModel(product_name, 0.0, 0.0, 0.0, 0.0, units);
                dish.setHow_much(how_much);

                eaten_food.add(dish);
                System.out.println("The size of initialized list in init " + eaten_food.size());

                while(resultSet.next())
                {
                    product_name = resultSet.getString("product_name");
                    how_much = resultSet.getDouble("how_much");
                    units = resultSet.getString("units");

                    dish = new FoodModel(product_name, 0.0, 0.0, 0.0, 0.0, units);
                    dish.setHow_much(how_much);

                    eaten_food.add(dish);
                    System.out.println("The size of initialized list in init method in while is " + eaten_food.size());
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
                System.out.println("Nothing in the database so far");
            }
            else
            {
                System.out.println("THERE IS SOMETHING AFTER ALL IN THE STATS DATA");
                double kcals_stat = resultSet.getDouble("kcal");
                System.out.println(kcals_stat);
                double proteins_stat = resultSet.getDouble("proteins");
                System.out.println(proteins_stat);
                double fats_stat = resultSet.getDouble("fats");
                System.out.println(fats_stat);
                double carbons_stat = resultSet.getDouble("carbons");
                System.out.println(carbons_stat);

                stats.add(kcals_stat);
                stats.add(proteins_stat);
                stats.add(fats_stat);
                stats.add(carbons_stat);

                System.out.println(stats.size());
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
