package stay_healthy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class ChangeInfoController implements Initializable
{
    BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

    PreparedStatement preparedStatement=null;
    ResultSet resultSet = null;
    Connection con;

    @FXML
    private TextField height_text;
    @FXML
    private TextField weight_text;
    @FXML
    private TextField age_text;
    @FXML
    private Button submit_button;
    @FXML
    private Label warnings_label;

    public ChangeInfoController()
    {
        con = ConnectionUtil.connDB();
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        if(con==null)
        {
            set_warnings_label(TOMATO,"The connection to db failed");
        }
    }

    @FXML
    private void SubmitButtonControl(ActionEvent event) throws SQLException, IOException {
        if(info_change_status().equals("Success"))
        {
            System.out.println("Changes successfully applied");

            person.setWeight(Double.parseDouble(weight_text.getText()));
            person.setHeight(Double.parseDouble(height_text.getText()));
            person.setAge(Integer.parseInt(age_text.getText()));
            person.claculateTDEE("Maintain");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stayhealthy/UserMainWindow.fxml"));
            fxmlLoader.setLocation(getClass().getResource("UserMainWindow.fxml"));

            Parent user_window = (Parent)fxmlLoader.load();

            UserMainWindowController controller = (UserMainWindowController)fxmlLoader.getController();
            controller.inflateUI(person);

            Stage stage = (Stage)submit_button.getScene().getWindow();
            stage.close();
        }
        else
        {
            set_warnings_label(TOMATO,"Failed to change data");
        }
    }

    @FXML
    private void CancelButtonControl(ActionEvent event)
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    private String info_change_status() throws SQLException {
        String status="Success";
        double weight;
        double height;
        int age;

        try
        {
            weight = Double.parseDouble(weight_text.getText());
        }
        catch(NumberFormatException ex)
        {
            weight=0.0;
        }

        try
        {
            height = Double.parseDouble(height_text.getText());
        }
        catch(NumberFormatException ex)
        {
            height=0.0;
        }

        try
        {
            age = Integer.parseInt(age_text.getText());
        }
        catch(NumberFormatException ex)
        {
            age =0;
        }

        if(check_data(weight,height,age))
        {
            String statement = "INSERT INTO user_info (u_id, age, weight, height, sex, phy_act_level, choice) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE age = ?, weight = ?, height = ?";
            preparedStatement=con.prepareStatement(statement);
            preparedStatement.setString(1,Integer.toString(person.getId()));
            preparedStatement.setString(2,Integer.toString(age));
            preparedStatement.setString(3,Double.toString(weight));
            preparedStatement.setString(4,Double.toString(height));
            preparedStatement.setString(5,person.getSex());
            preparedStatement.setString(6,person.getActivity_level());
            preparedStatement.setString(7,"Maintain");
            preparedStatement.setString(8,Integer.toString(age));
            preparedStatement.setString(9,Double.toString(weight));
            preparedStatement.setString(10,Double.toString(height));

            preparedStatement.executeUpdate();
        }
        else
        {
            status = "Failed";
        }

        return status;
    }

    private boolean check_data(double weight,double height,int age)
    {
        int how_many_correct=0;
        if(weight>0&&weight<300.0)
        {
            how_many_correct+=1;
        }
        if(height>0&&height<=2.8)
        {
            how_many_correct+=1;
        }
        if(age>0&&age<=100)
        {
            how_many_correct+=1;
        }

        return how_many_correct == 3;
    }

    public void set_warnings_label(Color col, String text)
    {
        warnings_label.setText(text);
        warnings_label.setTextFill(col);
        warnings_label.setVisible(true);
    }

    public void inflateUI(BodyModel per)
    {
        this.person = humanFactory.getNewHuman(per.getSex());
        this.person.copyModel(per);
        System.out.println(this.person.getId() + this.person.getName());
    }

}
