package stay_healthy;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXML;

import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.net.URL;

import java.util.ResourceBundle;



public class NewAccountController implements Initializable{

    BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

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
    ObservableList<String> phy_act_level_list = FXCollections.observableArrayList("High", "Average", "Occasional", "Low","None at all");

    public NewAccountController()
    {
        con = ConnectionUtil.connDB();

    }

    @FXML
    public void SubmitButtonControl(ActionEvent event) throws IOException {
        String st= new_account_creation_status();
        if(st.equals("Success"))
        {
            set_dialog_label(GREEN,"The account creation was successful");
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fxml/UserMainWindow.fxml"));
            loader.setLocation(getClass().getResource("/fxml/UserMainWindow.fxml"));
            Parent user_window_parent = loader.load();

            Scene user_window_scene = new Scene(user_window_parent);

            UserMainWindowController controller = loader.getController();
            controller.inflateUI(person);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(user_window_scene);
            window.show();
        }
        else
        {
           set_dialog_label(TOMATO,"Failed account creation. In submit");
        }
    }

    @FXML
    public void BackButtonControl(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene login_scene = new Scene(login_parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(login_scene);
        window.show();

    }

    public String new_account_creation_status()
    {
        String status = "Success";
        String nick_name = nick_name_field.getText();
        String e_mail = e_mail_field.getText();
        String sex = sex_choice.getValue();
        String act_level = physical_act_choice.getValue();
        int age;
        double height,weight;
        try
        {
             age = Integer.parseInt(age_field.getText());
        }
        catch(NumberFormatException ex)
        {
            age = 0;
        }
        try
        {
             height = Double.parseDouble(height_field.getText());
        }
        catch(NumberFormatException ex)
        {
            height=0.0;
        }
        try
        {
             weight = Double.parseDouble(weight_field.getText());
        }
        catch(NumberFormatException ex)
        {
            weight = 0.0;
        }
        String password = pass_field.getText();
        String repeat_pass = repeat_pass_field.getText();

        //check if the provided data are suitable or not

        if(check_the_data(nick_name,e_mail,password,repeat_pass,weight,height,age))
        {
            // if they are - then try to find, whether the account for the given e_mail already exists in the data base
            try
            {
                String statement = "SELECT * FROM users WHERE e_mail = ? ";
                preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1,e_mail);
                resultSet=preparedStatement.executeQuery();
                if(!(resultSet.next()))
                {
                    e_mail_mes_label.setTextFill(GREEN);
                    e_mail_mes_label.setText("E-mail is free");
                    e_mail_mes_label.setVisible(true);
                    //if the account does not exist yet - then try to create it and add the information to the users' data base
                    try
                    {
                        statement = "INSERT INTO users (name, e_mail, password) VALUES ( ?, ?, ?)";
                        preparedStatement = con.prepareStatement(statement);
                        preparedStatement.setString(1, nick_name);
                        preparedStatement.setString(2, e_mail);
                        preparedStatement.setString(3, password);
                        preparedStatement.executeUpdate();

                        //check if the insertion really succeeded by trying to select the data about the newly created account from the database

                        statement = "SELECT * FROM users WHERE e_mail = ?";
                        preparedStatement = con.prepareStatement(statement);
                        preparedStatement.setString(1, e_mail);
                        resultSet = preparedStatement.executeQuery();

                        int id=0;

                        if(resultSet.next())
                        {
                            set_dialog_label(GREEN,"The creation was successful");
                            id = resultSet.getInt("user_id");

                            statement="INSERT INTO user_info (u_id, age, height, weight, sex, phy_act_level) VALUES ( ?, ?, ?, ?, ?, ?)";
                            preparedStatement=con.prepareStatement(statement);
                            preparedStatement.setString(1,Integer.toString(id));
                            preparedStatement.setString(2,Integer.toString(age));
                            preparedStatement.setString(3,Double.toString(height));
                            preparedStatement.setString(4,Double.toString(weight));
                            preparedStatement.setString(5,sex);
                            preparedStatement.setString(6,act_level);
                            preparedStatement.executeUpdate();

                            //filling in the information about our user into the person model *(to calculate necessary TDEE and to pass
                            // this info to other stages and windows of the application)

                            person = humanFactory.getNewHuman(sex);

                            person.setId(id);
                            person.setName(nick_name);
                            person.setWeight(weight);
                            person.setHeight(height);
                            person.setAge(age);
                            person.setActivity_level(act_level);
                            person.claculateTDEE("Maintain");

                        }
                        else
                        {
                            set_dialog_label(TOMATO,"The creation failed. No data in data base ");
                            status = "Failed creating";
                        }

                        //then try to insert the addition data (age, weight and so on) to another data base using the user's ID from previous data base
                    }
                    catch (SQLException ex)
                    {
                        set_dialog_label(YELLOW,"Could not add the user to data base");
                        status = "Failed insert";
                        System.err.println(ex.getMessage());
                    }
                }
                else
                {
                    //otherwise the account creation fails
                    e_mail_mes_label.setTextFill(TOMATO);
                    e_mail_mes_label.setText("The account for this email exists");
                    e_mail_mes_label.setVisible(true);
                    status = "Exists";
                }
            }
            catch(SQLException ex)
            {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
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
        physical_act_choice.setItems(phy_act_level_list);
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
                    name_mes_label.setText("Correct");
                    name_mes_label.setTextFill(GREEN);
                    name_mes_label.setVisible(true);
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
                e_mail_mes_label.setText("Correct");
                e_mail_mes_label.setTextFill(GREEN);
                e_mail_mes_label.setVisible(true);
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
                    pass_mes_label.setText("Correct");
                    pass_mes_label.setTextFill(GREEN);
                    pass_mes_label.setVisible(true);
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
                rep_pass_mes_label.setText("Correct");
                rep_pass_mes_label.setTextFill(GREEN);
                rep_pass_mes_label.setVisible(true);
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
            if(height>=2.8)
            {
                height_mes_label.setTextFill(TOMATO);
                height_mes_label.setText("Very unlikely data...");
                height_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
                height_mes_label.setText("Correct");
                height_mes_label.setTextFill(GREEN);
                height_mes_label.setVisible(true);
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
            if(weight>=300)
            {
                weight_mes_label.setTextFill(TOMATO);
                weight_mes_label.setText("Very unlikely data...");
                weight_mes_label.setVisible(true);
            }
            else
            {
                how_many_are_correct+=1;
                weight_mes_label.setText("Correct");
                weight_mes_label.setTextFill(GREEN);
                weight_mes_label.setVisible(true);
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
                age_mes_label.setText("Correct");
                age_mes_label.setTextFill(GREEN);
                age_mes_label.setVisible(true);
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
