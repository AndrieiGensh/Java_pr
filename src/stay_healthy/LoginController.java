package stay_healthy;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import static javafx.scene.paint.Color.*;


public class LoginController implements Initializable{

    public BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

    @FXML
    private TextField name_or_email_text;

    @FXML
    private TextField password_text;

    @FXML
    private Button log_in_button;

    @FXML
    private Label err_label;

    @FXML
    private Label mes_label;

    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if(con==null)
        {
            err_label.setTextFill(TOMATO);
            err_label.setText("Server error!!!");
        }
        else
        {
            err_label.setTextFill(Color.GREEN);
            err_label.setText("Server has been connected");
        }
    }

    public LoginController()
    {
        con = ConnectionUtil.connDB();
    }

    @FXML
    public void CreateAccountButtonControl(ActionEvent event) throws IOException {
        Parent new_account_parent = FXMLLoader.load(getClass().getResource("NewAccount.fxml"));
        Scene new_account_scene = new Scene(new_account_parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(new_account_scene);
        window.show();
    }

    @FXML
    public void LoginButtonControll(ActionEvent event) throws SQLException, IOException {
        if(event.getSource()==log_in_button)
        {
            if(login_status().equals("Success"))
            {
                mes_label.setText("Login success");
                mes_label.setVisible(true);

                String statement = "SELECT * from users WHERE e_mail = ? OR name = ?";
                preparedStatement=con.prepareStatement(statement);
                preparedStatement.setString(1,name_or_email_text.getText());
                preparedStatement.setString(2,name_or_email_text.getText());
                resultSet=preparedStatement.executeQuery();

                int id;
                String person_name;

                if(resultSet.next())
                {
                    id=resultSet.getInt("user_id");
                    System.out.println("id in login == "+id);
                    person_name = resultSet.getString("name");
                    statement = "SELECT * from user_info WHERE u_id = ?";
                    preparedStatement=con.prepareStatement(statement);
                    preparedStatement.setString(1,Integer.toString(id));
                    resultSet=preparedStatement.executeQuery();

                    if(resultSet.next())
                    {
                        String person_sex = resultSet.getString("sex");

                        System.out.println("We got " + person_sex);

                        person = humanFactory.getNewHuman(person_sex);

                        person.setId(id);
                        person.setName(person_name);
                        person.setActivity_level(resultSet.getString("phy_act_level"));
                        person.setAge(resultSet.getInt("age"));
                        person.setHeight(resultSet.getDouble("height"));
                        person.setWeight(resultSet.getDouble("weight"));
                    }
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("stay_healthy/UserMainWindow.fxml"));
                loader.setLocation(getClass().getResource("UserMainWindow.fxml"));
                Parent user_window_parent = loader.load();

                UserMainWindowController controller = (UserMainWindowController) loader.getController();
                controller.inflateUI(person);

                Scene user_window_scene = new Scene(user_window_parent);

                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                window.setScene(user_window_scene);
                window.show();
            }
            else
            {
                mes_label.setText("Login failed");
                mes_label.setVisible(true);
            }
        }
    }

    public String login_status()
    {
        String status = "Success";
        String name = name_or_email_text.getText();
        String password = password_text.getText();

        if(name.isEmpty()||password.isEmpty())
        {
            status = "Error";
            set_error_label(TOMATO,"No empty field allowed");
        }
        else
        {
            try
            {
                String query = "SELECT * FROM users WHERE e_mail = ?  AND password = ?";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, name);
                //preparedStatement.setString(2, name);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                {
                    set_error_label(TOMATO,"No match for your data");
                    status = "Error";
                }
                else
                {
                    set_error_label(GREEN,"Welcome back, " + name);
                }
            }
            catch(SQLException ex)
            {
                System.err.println(ex.getMessage());
                status = "Exception";
            }

        }

        return status;
    }

    private void set_error_label(Color col,String text)
    {
        err_label.setTextFill(col);
        err_label.setText(text);
    }


}
