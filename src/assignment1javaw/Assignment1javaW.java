/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1javaw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Bearuang
 */
public class Assignment1javaW extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/ProfileList.fxml"));
       
       Scene scene = new Scene(root);
       
       primaryStage.setTitle("All Contacts");
       primaryStage.setScene(scene);
       primaryStage.show();
    } 
}
