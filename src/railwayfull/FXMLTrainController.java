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

public class FXMLTrainController {

    @FXML
    private TextField trainID;

    @FXML
    private TextField shovonSeats;

    @FXML
    private TextField acSeats;

    @FXML
    private TextField priceperkm;
    
    @FXML
    private Button close_btn;

    @FXML
    private TextField schairSeats;

    @FXML
    private TextField trainName;

    @FXML
    private TextField routeNo;

    @FXML
    void close_train(ActionEvent event) {
        Stage currentStage = (Stage)close_btn.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void add_train(ActionEvent event) {
        try {
            String query = (trainID.getText()+", '"+trainName.getText()+"', "+routeNo.getText()+", "+shovonSeats.getText()+", "+schairSeats.getText()+", "+acSeats.getText()+", "+priceperkm.getText());
            //System.out.println(query);
            RailwayFull.addTrain(query);
            RailwayFull.saveLog("ADDED TRAIN "+trainID.getText()+" "+trainName.getText()+" ON ROUTE "+routeNo.getText()+" WITH SEATS "+shovonSeats.getText()+"xShovon, "+schairSeats.getText()+"sShovonChair "+acSeats.getText()+"xAC AND PRICE PER KM = "+priceperkm.getText()+" AT "+LocalDateTime.now());
            
            
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
            alert.close();
        }

    }

}
