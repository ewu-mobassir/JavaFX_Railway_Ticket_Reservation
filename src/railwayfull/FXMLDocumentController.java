/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package railwayfull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Eram
 */
public class FXMLDocumentController{

    @FXML
    void exit_app(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void BookTicketMenu(ActionEvent event) throws IOException {
        System.out.println("Booking Ticket");
        Stage stageStation = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLBooking.fxml"));
        Scene scene = new Scene(root);
        stageStation.setScene(scene);
        stageStation.show();
    }

    @FXML
    void addStationMrnu(ActionEvent event) throws IOException {
        System.out.println("Adding Station");
        Stage stageStation = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLStation.fxml"));
        Scene scene = new Scene(root);
        stageStation.setScene(scene);
        stageStation.show();
    }

    @FXML
    void addTrainMenu(ActionEvent event) throws IOException {
        System.out.println("Adding Train");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLTrain.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void openLog(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("PurchaseHistory.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
