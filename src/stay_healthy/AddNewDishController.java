package stay_healthy;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

import javafx.stage.Stage;
import utils.ConnectionUtil;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewDishController implements Initializable {

    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet = null;
    private Connection con;

    private boolean is_selected = false;

    private ObservableList<FoodModel> found = FXCollections.observableArrayList();

    public AddNewDishController()
    {
        con = ConnectionUtil.connDB();
    }

    @FXML
    private Button ok_button;
    @FXML
    private TextField food_name_text;
    @FXML
    private TextField how_much_text;
    @FXML
    private Label warnings_label;
    @FXML
    private TableView<FoodModel> found_food_table;
    @FXML
    private TableColumn<FoodModel, String> found_food_column;
    @FXML
    private Button search_button;


    @FXML
    public void OkButton(ActionEvent event)
    {
        if(check_measure_filed())
        {
            if(is_selected)
            {
                double value = Double.parseDouble(how_much_text.getText());
                set_warning_label(GREEN, "Read " + value);
            }
        }
    }

    @FXML
    public void SelectEventHandler(MouseEvent event)
    {
        if(event.getClickCount() > 1)
        {
            execute_on_edit();
        }
    }

    @FXML
    public void SearchButtonControl(ActionEvent event) throws SQLException {
        if(check_search_field())
        {
            found.clear();
            String food_name = food_name_text.getText();
            String statement = "SELECT * FROM food_info WHERE food_name = ?";
            preparedStatement = con.prepareStatement(statement);
            preparedStatement.setString(1, food_name);
            resultSet = preparedStatement.executeQuery();

            if(!(resultSet.next()))
            {
                set_warning_label(YELLOW, "Nothing found in the database");
            }
            else
            {
                String name;
                double kcal;
                double proteins;
                double fats;
                double carbons;
                String measure;

                name = resultSet.getString("food_name");
                kcal = resultSet.getDouble("kcal");
                proteins = resultSet.getDouble("proteins");
                fats = resultSet.getDouble("fats");
                carbons = resultSet.getDouble("carbons");
                measure = resultSet.getString("measure");

                FoodModel dish = new FoodModel(name, kcal, proteins, fats, carbons, measure);

                found.add(dish);

                while(resultSet.next())
                {
                    name = resultSet.getString("food_name");
                    kcal = resultSet.getDouble("kcal");
                    proteins = resultSet.getDouble("proteins");
                    fats = resultSet.getDouble("fats");
                    carbons = resultSet.getDouble("carbons");
                    measure = resultSet.getString("measure");

                    dish = new FoodModel(name, kcal, proteins, fats, carbons, measure);

                    found.add(dish);
                }
                set_warning_label(GREEN, "FOUND");
                System.out.println(found.size());
                found_food_table.setItems(found);

            }

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        found_food_column.setCellValueFactory(new PropertyValueFactory<FoodModel, String>("name"));
    }

    public void execute_on_edit()
    {
        if(found_food_table.getSelectionModel().getSelectedItem() != null)
        {
            is_selected = true;
            FoodModel dish = found_food_table.getSelectionModel().getSelectedItem();
            how_much_text.setText(dish.getMeasure().get());
        }
        else
        {
            is_selected = false;
        }
    }

    public boolean check_search_field()
    {
        String text = food_name_text.getText();
        if(text == null || text.isEmpty())
        {
            set_warning_label(TOMATO, "Nothing in the searching filed");
            return false;
        }
        if(text.length()>=20)
        {
            set_warning_label(TOMATO, "The input is too long");
            return false;
        }
        else
        {
            if(text.matches(".*\\d+.*"))
            {
                set_warning_label(TOMATO, "The text should not contain digits");
                return false;
            }
            else
            {
                warnings_label.setVisible(false);
                return true;
            }
        }
    }

    public boolean check_measure_filed()
    {
        String content =  how_much_text.getText();
        double value;
        if(content == null || content.isEmpty())
        {
            return false;
        }
        else
        {
            try
            {
                value = Double.parseDouble(content);
            }
            catch(Exception e)
            {
                value = -1.0;
            }

            return !(value <= 0) && !(value >= 2000);

        }
    }

    public void set_warning_label(Color color, String text)
    {
        warnings_label.setText(text);
        warnings_label.setTextFill(color);
        warnings_label.setVisible(true);
    }


}
