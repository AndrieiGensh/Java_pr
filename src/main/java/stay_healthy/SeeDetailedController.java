package stay_healthy;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import javafx.fxml.FXML;

import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;


public class SeeDetailedController implements Initializable
{
    @FXML
    private Button got_it_button;
    @FXML
    private Label kcal_consumed;
    @FXML
    private Label prot_consumed;
    @FXML
    private Label fats_consumed;
    @FXML
    private Label carb_consumed;
    @FXML
    private Label kcal_norm;
    @FXML
    private Label prot_norm;
    @FXML
    private Label fats_norm;
    @FXML
    private Label carb_norm;
    @FXML
    private TextArea tips_text_area;

    private BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    void inflateUI(BodyModel person, ArrayList<Double> consumed)
    {
        this.person = this.humanFactory.getNewHuman(person.getSex());
        this.person.copyModel(person);
        this.person.claculateTDEE("Maintain");

        this.person.calculateNeeds();

        ArrayList<Double> needs = this.person.getNeeds();

        this.kcal_norm.setText(Double.toString(needs.get(0)));
        this.prot_norm.setText(Double.toString(needs.get(1)));
        this.fats_norm.setText(Double.toString(needs.get(2)));
        this.carb_norm.setText(Double.toString(needs.get(3)));

        if(consumed.size() != 0)
        {
            this.kcal_consumed.setText(Double.toString(consumed.get(0)));
            this.prot_consumed.setText(Double.toString(consumed.get(1)));
            this.fats_consumed.setText(Double.toString(consumed.get(2)));
            this.carb_consumed.setText(Double.toString(consumed.get(3)));
        }

        this.generateTips();

    }

    void generateTips()
    {
        String tips = "";

        if(Double.parseDouble(this.kcal_consumed.getText()) - Double.parseDouble(this.kcal_norm.getText()) < 0)
        {
            tips += "You need to eat more. The daily Norm is not achieved yet!\n";
        }
        if(Double.parseDouble(this.prot_consumed.getText()) - Double.parseDouble(this.prot_norm.getText()) < 0)
        {
            tips += "You need more proteins. Eat meat, fish, cheese etc.\n";
        }
        if(Double.parseDouble(this.fats_consumed.getText()) - Double.parseDouble(this.fats_norm.getText()) < 0)
        {
            tips += "You need more fats. Eat meat, milk, fish and nuts\n";
        }
        if(Double.parseDouble(this.carb_consumed.getText()) - Double.parseDouble(this.carb_norm.getText()) < 0)
        {
            tips += "You need more carbohydrates. Eat more bakery and greens \n";
        }

        this.tips_text_area.setText(tips);
    }

    @FXML
    void GotItButtonControl(ActionEvent event)
    {
        Stage stage = (Stage)this.got_it_button.getScene().getWindow();
        stage.close();
    }


}
