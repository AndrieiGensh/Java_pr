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
import java.util.Date;
import java.util.ResourceBundle;

public class AddNewDishController implements Initializable {

    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet = null;
    private Connection con;

    private boolean is_selected = false;

    private UserMainWindowController controller;

    private ObservableList<FoodModel> found = FXCollections.observableArrayList();
    private ObservableList<FoodModel> added = FXCollections.observableArrayList();

    private BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

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
    private Button submit_button;
    @FXML
    private TextArea dishes_text_area;

    @FXML
    public void OkButton(ActionEvent event)
    {
        if(check_measure_filed())
        {
            if(is_selected)
            {
                double value = Double.parseDouble(how_much_text.getText());
                String textAreaContent = dishes_text_area.getText();

                if(found_food_table.getSelectionModel().getSelectedItem() != null)
                {
                    FoodModel dish = found_food_table.getSelectionModel().getSelectedItem();
                    dish.setHow_much(Double.parseDouble(how_much_text.getText()));
                    textAreaContent = textAreaContent + "\n" + dish.getName().get() + "(" + value + " " + dish.getMeasure().get() + ")";
                    dishes_text_area.setText(textAreaContent);
                    dish.multiply_by(value);
                    added.add(dish);
                }

                how_much_text.clear();
                found.removeAll(found);
                found_food_table.setItems(found_food_table.getItems());

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
            String statement = "SELECT * FROM food_info WHERE food_name like ?";
            preparedStatement = con.prepareStatement(statement);
            preparedStatement.setString(1, "%" + food_name.toLowerCase() + "%");
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


    public void  SubmitButtonControl(ActionEvent event) throws IOException, SQLException {

        FoodModel[] foodArray = new FoodModel[added.size()];

        for(int i =0; i < added.size(); i++)
        {
            foodArray[i] = added.get(i);
        }

        FoodContainer foodContainer = new FoodContainer(foodArray);

        foodContainer.sumUp();

        String statement = "INSERT INTO daily_stats (user_id, date, kcal, proteins, fats, carbons) VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLiCATE KEY UPDATE kcal = kcal + ?, proteins = proteins + ?, fats = fats + ?, carbons = carbons + ?";

        preparedStatement = con.prepareStatement(statement);
        preparedStatement.setString(1, Integer.toString(person.getId()));

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        preparedStatement.setDate(2, sqlDate);
        preparedStatement.setString(3, Double.toString(foodContainer.getKcal()));
        preparedStatement.setString(4, Double.toString(foodContainer.getProteins()));
        preparedStatement.setString(5, Double.toString(foodContainer.getFats()));
        preparedStatement.setString(6, Double.toString(foodContainer.getCarbons()));

        preparedStatement.setString(7, Double.toString(foodContainer.getKcal()));
        preparedStatement.setString(8, Double.toString(foodContainer.getProteins()));
        preparedStatement.setString(9, Double.toString(foodContainer.getFats()));
        preparedStatement.setString(10, Double.toString(foodContainer.getCarbons()));

        preparedStatement.executeUpdate();

        statement = "INSERT INTO products_diary (u_id, product_name, how_much, units) VALUES (?, ?, ?, ?)";
        preparedStatement = con.prepareStatement(statement);

        for(int i = 0; i < added.size(); i++)
        {
            preparedStatement.setString(1, Integer.toString(person.getId()));
            preparedStatement.setString(2, foodArray[i].getName().get());
            preparedStatement.setString(3, Double.toString(foodArray[i].getHow_much()));
            preparedStatement.setString(4, foodArray[i].getMeasure().get());

            preparedStatement.executeUpdate();
        }

        this.controller.AppendToTableView(foodContainer);

        Stage stage = (Stage)submit_button.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        found_food_column.setCellValueFactory(new PropertyValueFactory<FoodModel, String>("name"));
        dishes_text_area.setDisable(true);
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

    public void setReferenseTo(UserMainWindowController controller)
    {
        this.controller = controller;
    }

    public void InflateUI(BodyModel person)
    {
        this.person = humanFactory.getNewHuman(person.getSex());
        this.person.copyModel(person);
    }

}
