package stay_healthy;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    PreparedStatement preparedStatement=null;
    ResultSet resultSet = null;
    Connection con;

    ObservableList<>

    public AddNewDishController()
    {
        con = ConnectionUtil.connDB();
    }

    @FXML
    private Button ok_button;
    @FXML
    private TextField food_name_text;
    @FXML
    private TextField hoe_much_text;
    @FXML
    private Label warning_label;
    @FXML
    private TableView<String> found_food_table;
    @FXML
    private Button search_button;

    @FXML
    public void SearchButtonControl(ActionEvent event) throws SQLException {
        if(check_search_field())
        {
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
                while(resultSet.next())
            }

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public boolean check_search_field()
    {
        String text = food_name_text.getText();
        int tmp_int;
        double tmp_doule;
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
                warning_label.setVisible(false);
                return true;
            }
        }
    }

    public void set_warning_label(Color color, String text)
    {
        warning_label.setText(text);
        warning_label.setTextFill(color);
        warning_label.setVisible(true);
    }


}
