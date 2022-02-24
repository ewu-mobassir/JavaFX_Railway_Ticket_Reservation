/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package railwayfull;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLStationController {

    @FXML
    private TextField distF;

    @FXML
    private Button close_btn;

    @FXML
    private TextField stName;

    @FXML
    private TextField stId;

    @FXML
    private TextField stopageNo;
    
    @FXML
    private TextField routeNo;

    @FXML
    void close_window(ActionEvent event) {
        Stage currentStage = (Stage)close_btn.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void addStation(ActionEvent event) {
        try {
            //System.out.println(stId.getText()+",'"+stName.getText()+"',"+routeNo.getText()+","+distF.getText()+","+stopageNo.getText());
            RailwayFull.addStation(stId.getText()+",'"+stName.getText()+"',"+routeNo.getText()+","+distF.getText()+","+stopageNo.getText());
            RailwayFull.saveLog("ADDED STATION "+stId.getText()+" "+stName.getText()+" ON ROUTE "+routeNo.getText()+" WHERE DISTANCE FROM CENTRAL STATION = "+distF.getText()+" STOPAGE NO = "+stopageNo.getText()+" AT "+LocalDateTime.now());
        } catch (Exception ex) {
            System.out.println("ERROR: SQL Exception! Cause: "+ex.getLocalizedMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
            alert.close();
            
        }
    }

}
