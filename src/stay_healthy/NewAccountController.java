package stay_healthy;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXML;

import utils.ConnectionUtil;

import java.net.URL;

import java.util.ResourceBundle;



public class NewAccountController implements Initializable{

    @FXML
    private Button submit_buton;
    @FXML
    private Button back_button;
    @FXML
    private Label dialog_label;
    @FXML
    private Label name_mes_label;
    @FXML
    private Label e_mail_mes_label;
    @FXML
    private Label pass_mes_label;
    @FXML
    private Label rep_pass_mes_label;
    @FXML
    private Label age_mes_label;
    @FXML
    private Label weight_mes_label;
    @FXML
    private Label height_mes_label;
    @FXML
    private TextField e_mail_field;
    @FXML
    private TextField nick_name_field;
    @FXML
    private TextField weight_field;
    @FXML
    private TextField height_field;
    @FXML
    private TextField age_field;
    @FXML
    private PasswordField pass_field;
    @FXML
    private PasswordField repeat_pass_field;
    @FXML
    private ChoiceBox<String> sex_choice;
    @FXML
    private ChoiceBox<String> physical_act_choice;

    PreparedStatement preparedStatement=null;
    ResultSet resultSet = null;
    Connection con;

    ObservableList<String> sex_list = FXCollections.observableArrayList("Male","Female");
    ObservableList<String> phy_act_level = FXCollections.observableArrayList("High", "Average", "Occasional", "Low","None at all");

    public NewAccountController()
    {
        con = ConnectionUtil.connDB();

    }

    @FXML
    public void SubmitButtonControl(ActionEvent event)
    {

    }

    @FXML
    public void BackButtonControl()
    {

    }

    public String new_account_creation_status()
    {
        String status = "Success";
        String nick_name = nick_name_field.getText();
        String e_mail = e_mail_field.getText();
        String sex = sex_choice.getValue();
        String act_level = physical_act_choice.getValue();
        int age = Integer.parseInt(age_field.getText());
        double height = Double.parseDouble(height_field.getText());
        double weight = Double.parseDouble(weight_field.getText());
        String password = pass_field.getText();
        String repeat_pass = repeat_pass_field.getText();

        if(check_the_data(nick_name,e_mail,password,repeat_pass,weight,height,age))
        {

        }
        else
        {
            status = "Failed";
        }
        return status;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sex_choice.setValue("Male");
        sex_choice.setItems(sex_list);
        physical_act_choice.setValue("Average");
        physical_act_choice.setItems(phy_act_level);
        if(con == null)
        {
            set_dialog_label(TOMATO,"No connection to the server");
        }
        else
        {
            set_dialog_label(GREEN,"Connection successful");
        }
    }

    public boolean check_the_data(String name, String e_mail,String password,String repeat_pass,double weight,double height,int age)
    {
        int how_many_are_correct=0;
        if(name.isEmpty())
        {
            name_mes_label.setText("Empty field not allowed");
            name_mes_label.setTextFill(TOMATO);
            name_mes_label.setVisible(true);
        }
        else
        {
            if(name.length()<=8)
            {
                name_mes_label.setText("Too short (9 characters minimum)");
                name_mes_label.setTextFill(TOMATO);
                name_mes_label.setVisible(true);
            }
            else
            {
                if(name.length()>=20)
                {
                    name_mes_label.setText("Too long (18 characters maximum)");
                    name_mes_label.setTextFill(TOMATO);
                    name_mes_label.setVisible(true);
                }
                else
                {
                    how_many_are_correct+=1;
                }
            }
        }

        if(e_mail.isEmpty())
        {
            e_mail_mes_label.setText("Empty field not allowed");
            e_mail_mes_label.setTextFill(TOMATO);
            e_mail_mes_label.setVisible(true);
        }
        else
        {
            if(!e_mail.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
            {
                e_mail_mes_label.setText("The e_mail address doesn't match the pattern");
                e_mail_mes_label.setTextFill(TOMATO);
                e_mail_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
            }
        }

        if(password.isEmpty())
        {
            pass_mes_label.setTextFill(TOMATO);
            pass_mes_label.setText("Empty field not allowed");
            pass_mes_label.setVisible(true);
        }
        else
        {
            if(password.length()<=8)
            {
                pass_mes_label.setText("Too short (9 characters minimum)");
                pass_mes_label.setTextFill(TOMATO);
                pass_mes_label.setVisible(true);
            }
            else
            {
                if(password.length()>=20)
                {
                    pass_mes_label.setText("Too long (18 characters maximum)");
                    pass_mes_label.setTextFill(TOMATO);
                    pass_mes_label.setVisible(true);
                }
                else
                {
                    how_many_are_correct+=1;
                }
            }
        }

        if(repeat_pass.isEmpty())
        {
            rep_pass_mes_label.setText("Does not match the password");
            rep_pass_mes_label.setTextFill(TOMATO);
            rep_pass_mes_label.setVisible(true);
        }
        else
        {
            if(!repeat_pass.equals(password))
            {
                rep_pass_mes_label.setText("Does not match the password");
                rep_pass_mes_label.setTextFill(TOMATO);
                rep_pass_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
            }
        }

        if(height<=0)
        {
            height_mes_label.setTextFill(TOMATO);
            height_mes_label.setText("Wrong data!!!");
            height_mes_label.setVisible(true);
        }
        else
        {
            if(height>=300)
            {
                height_mes_label.setTextFill(TOMATO);
                height_mes_label.setText("Very unlikely data...");
                height_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
            }
        }

        if(weight<=0)
        {
            weight_mes_label.setTextFill(TOMATO);
            weight_mes_label.setText("Wrong data!!!");
            weight_mes_label.setVisible(true);
        }
        else
        {
            if(weight>=2.8)
            {
                weight_mes_label.setTextFill(TOMATO);
                weight_mes_label.setText("Very unlikely data...");
                weight_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
            }
        }

        if(age<=0)
        {
            age_mes_label.setText("Wrong data!!!");
            age_mes_label.setTextFill(TOMATO);
            age_mes_label.setVisible(true);
        }
        else
        {
            if(age>100)
            {
                age_mes_label.setText("Very unlikely data...");
                age_mes_label.setTextFill(TOMATO);
                age_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
            }
        }

        return how_many_are_correct == 7;
    }

    public void set_dialog_label(Color col, String text)
    {
        dialog_label.setText(text);
        dialog_label.setTextFill(col);
    }
}
