package stay_healthy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserMainWindowController implements Initializable {

    Connection con=null;
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;

    private BodyModel person;

    private HumanFactory humanFactory = new HumanFactory();

    private String current_scene_state;

    private ObservableList<FoodModel> diaryList = FXCollections.observableArrayList();

    private ArrayList<Double> stats;

    private ObservableList<PieChart.Data> pieChartData= FXCollections.observableArrayList();

    private SceneLoader sceneLoader = new SceneLoader();

    public String mess = "none";

    private FoodInflateDAO foodDAO = new FoodInflateDAO();


    @FXML
    private Label user_name_label;
    @FXML
    private Label age_label;
    @FXML
    private Label weight_label;
    @FXML
    private Label height_label;
    @FXML
    private Button log_out_button;
    @FXML
    private Button settings_button;
    @FXML
    private Button see_detailed_side_button;
    @FXML
    private Button see_detailed_button;
    @FXML
    private Button change_button;
    @FXML
    private Button general_view_button;
    @FXML
    private Button add_dish_button;
    @FXML
    private Label dialog_label;
    @FXML
    private Label warnings_label;
    @FXML
    private BorderPane border_pane;
    @FXML
    private TableView<FoodModel> diary_table_view;
    @FXML
    private TableColumn<FoodModel, String> dish_name_column;
    @FXML
    private TableColumn<FoodModel, String> how_much_column;
    @FXML
    private PieChart stats_chart;


    public UserMainWindowController()
    {
        con = ConnectionUtil.connDB();
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        System.out.println("THE SCENE IS BEiNG INITIALIZED");

       current_scene_state="general_view";
       mess = "inited";

       dish_name_column.setCellValueFactory(new PropertyValueFactory<FoodModel, String>("name"));
       how_much_column.setCellValueFactory(new PropertyValueFactory<FoodModel, String>("how_much_string"));

    }

    @FXML
    public void GeneralViewSideButton(ActionEvent event)
    {
        if(current_scene_state.equals("general_view"))
        {
            return;
        }
        else
        {
            setCenterPane("GeneralView");
            current_scene_state="general_view";
        }
    }

    @FXML
    public void SeeDetailedButton(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeeDetailed.fxml"));
            loader.setLocation(getClass().getResource("/fxml/SeeDetailed.fxml"));

            Parent root = (Parent) loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            SeeDetailedController controller = (SeeDetailedController) loader.getController();
            controller.inflateUI(this.person, this.stats);

            stage.show();

        } catch (IOException e)
        {
                e.printStackTrace();
        }
    }


    @FXML
    public void ChangeButton(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangeUserInfo.fxml"));
            loader.setLocation(getClass().getResource("/fxml/ChangeUserInfo.fxml"));

            Parent root = (Parent) loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            ChangeInfoController controller = (ChangeInfoController) loader.getController();
            controller.inflateUI(this.person);

            stage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void LogOutSideButton(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene login_scene = new Scene(login_parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(login_scene);
        window.show();
    }

    @FXML
    public void AddDishButton(ActionEvent event)
    {
        mess = "inited in add dish";
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddNewDishToDiary.fxml"));
            loader.setLocation(getClass().getResource("/fxml/AddNewDishToDiary.fxml"));

            Parent root = (Parent) loader.load();

            AddNewDishController controller = (AddNewDishController) loader.getController();

            controller.setReferenseTo(this);
            controller.InflateUI(this.person);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void AppendToTableView(FoodContainer container)
    {
        mess = "inited in append";
        for(Iterator iter = container.getIterator(); iter.has_next_item();)
        {
            FoodModel dish = (FoodModel)iter.next_item();
            diaryList.add(dish);
        }

        System.out.println("Now the list is " + diaryList.size() + " element long");

        set_dialog_label(MAGENTA, "The size is " + diaryList.size());

        this.stats = this.foodDAO.getStats();

        this.pieChartData.clear();

        this.pieChartData.add(new PieChart.Data("proteins", this.stats.get(1)));
        this.pieChartData.add(new PieChart.Data("fats", this.stats.get(2)));
        this.pieChartData.add(new PieChart.Data("carbons", this.stats.get(3)));

        this.stats_chart.setData(pieChartData);

        diary_table_view.setItems(diaryList);
        diary_table_view.refresh();

    }

    public void inflateUI(BodyModel per)
    {
        user_name_label.setText(per.getName());
        age_label.setText(Integer.toString(per.getAge()));
        weight_label.setText(Double.toString(per.getWeight()));
        height_label.setText(Double.toString(per.getHeight()));

        this.person = humanFactory.getNewHuman(per.getSex());

        this.person.copyModel(per);

        this.foodDAO.setUser_id(per.getId());

        this.stats = (this.foodDAO.getStats());

        this.pieChartData.add(new PieChart.Data("proteins", this.stats.get(1)));
        this.pieChartData.add(new PieChart.Data("fats", this.stats.get(2)));
        this.pieChartData.add(new PieChart.Data("carbons", this.stats.get(3)));

        this.stats_chart.setData(pieChartData);

        diaryList.setAll(this.foodDAO.getAll());

        diary_table_view.setItems(diaryList);
        diary_table_view.refresh();

    }

    private void set_dialog_label(Color col,String text)
    {
        dialog_label.setText(text);
        dialog_label.setTextFill(col);
        dialog_label.setVisible(true);
    }

    private void setCenterPane(String file_name)
    {
        Parent root = null;

        try
        {
            root = FXMLLoader.load(getClass().getResource("/fxml/" + file_name + ".fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        border_pane.setCenter(root);

    }

}
