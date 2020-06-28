package stay_healthy;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.net.URL;

public class SceneLoader {

    private Parent view = null;

   public Parent getPane(String filename)
   {
       try
       {
           view = FXMLLoader.load(getClass().getResource(filename + ".fxml"));
       }
       catch(Exception e)
       {
           System.out.println("Error occurred : " + e.getMessage());
       }

       return view;

   }

}
