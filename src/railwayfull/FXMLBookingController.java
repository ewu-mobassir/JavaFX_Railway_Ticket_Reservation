package railwayfull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class FXMLBookingController{

    @FXML
    private Label msg_box;

    @FXML
    private ComboBox<Integer> noOfSeats;

    @FXML
    private DatePicker travelDate;

    @FXML
    private ComboBox<String> seatClass;

    @FXML
    private Button close_btn;

    @FXML
    private ComboBox<String> stationTo;

    @FXML
    private ComboBox<String> stationFrom;

    @FXML
    private ComboBox<String> train;

    @FXML
    void close_window(ActionEvent event) {
        Stage currentStage = (Stage)close_btn.getScene().getWindow();
        currentStage.close();
    }
    
    @FXML
    void bookTicket(ActionEvent event) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/railway", "root", "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT * FROM `train` WHERE `TrainName`='"+train.getValue()+"' ");
            rs.first();
            int route = rs.getInt("RouteNo");
            int trainID = rs.getInt("TrainID");
            int shovonSeats = rs.getInt("ShovonSeats");
            int sChairSeats = rs.getInt("ShovonChairSeats");
            int acSeats = rs.getInt("ACSeats");
            double pricePerKM = rs.getDouble("PricePerKM");
            
            rs = st.executeQuery("SELECT * FROM `station` WHERE `RouteNo`="+route+" AND `StationName`='"+stationFrom.getValue()+"'");
            rs.first();
            int stopageFrom = rs.getInt("StopageNo");
            Station s1 = new Station(rs.getString("StationName"), rs.getInt("StationID"), rs.getInt("DistanceFromCentral"));
            
            
            rs = st.executeQuery("SELECT * FROM `station` WHERE `RouteNo`="+route+" AND `StationName`='"+stationTo.getValue()+"'");
             rs.first();
            int stopageTo = rs.getInt("StopageNo");
            Station s2 = new Station(rs.getString("StationName"), rs.getInt("StationID"), rs.getInt("DistanceFromCentral"));
            
            boolean direction = s1.getDirection(s2);
            
            LocalDate date = travelDate.getValue();
            
            int n = noOfSeats.getValue();
            boolean seatsEmpty = true;
            
            int lower = (stopageFrom<stopageTo)?stopageFrom:stopageTo;
            int upper = (stopageFrom>stopageTo)?stopageFrom:stopageTo;
            
            for (int i = lower; i < upper; i++) {
                rs = st.executeQuery("SELECT * FROM `bookedtickets` WHERE `TrainID` = "+trainID+" AND `Date` = '"+date+"' AND `direction` = "+direction+" AND `StopageNo` = " +i);
                if(!rs.next()){
                    //System.out.println("Working");
                    st.executeUpdate("INSERT INTO `bookedtickets`(`TrainID`, `Date`, `direction`, `StopageNo`, `bookedAC`, `bookedShovon`, `bookedSChair`) VALUES ("+trainID+",'"+date+"',"+direction+","+i+",0,0,0)");
                    seatsEmpty = true;
                }
                else if(seatClass.getValue().equals("Shovon Seat") && rs.getInt("bookedShovon") + n > shovonSeats){
                    seatsEmpty = false;
                    //System.out.println("Seat Overflow");
                    break;
                }
                else if(seatClass.getValue().equals("Shovon Chair Seat") && rs.getInt("bookedSChair") + n > sChairSeats) {
                    seatsEmpty = false;
                    //System.out.println("Seat Overflow");
                    break;
                }
                else if(seatClass.getValue().equals("AC Seat") && rs.getInt("bookedAC") + n > acSeats) {
                    seatsEmpty = false;
                    break;
                }
            }
            
            if(seatsEmpty){
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setHeaderText("Confirm Ticket Purchase");
                confirm.setContentText("Purchasing " + n + " tickets from " + s1.getStationName() + " to " + s2.getStationName()
                + "\n\n Total Price = "+ (s1.calcPrice(s2, seatClass.getValue(),pricePerKM)*noOfSeats.getValue())
                + "");
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.get() == ButtonType.OK){
                    for (int i = lower; i < upper; i++) {
                        if(seatClass.getValue().equals("AC Seat"))
                            st.executeUpdate("UPDATE `bookedtickets` SET `bookedAC` = `bookedAC`+"+n+" WHERE `bookedtickets`.`TrainID` = "+trainID+" AND `bookedtickets`.`Date` = '"+date+"' AND `bookedtickets`.`direction` = "+direction+" AND `bookedtickets`.`StopageNo` = "+i+"");
                        else if(seatClass.getValue().equals("Shovon Chair Seat"))
                            st.executeUpdate("UPDATE `bookedtickets` SET `bookedShovon` = `bookedShovon`+"+n+" WHERE `bookedtickets`.`TrainID` = "+trainID+" AND `bookedtickets`.`Date` = '"+date+"' AND `bookedtickets`.`direction` = "+direction+" AND `bookedtickets`.`StopageNo` = "+i+"");
                        else if(seatClass.getValue().equals("Shovon Seat"))
                            st.executeUpdate("UPDATE `bookedtickets` SET `bookedSChair` = `bookedSChair`+"+n+" WHERE `bookedtickets`.`TrainID` = "+trainID+" AND `bookedtickets`.`Date` = '"+date+"' AND `bookedtickets`.`direction` = "+direction+" AND `bookedtickets`.`StopageNo` = "+i+"");
                    }
                    
                    
//                    Stage stageStation = new Stage();
//                    Parent root = FXMLLoader.load(getClass().getResource("TicketPrint.fxml"));
//                    TicketPrintController.printTicket(train.getValue(), stationFrom.getValue(), stationTo.getValue(), noOfSeats.getValue(), date, seatClass.getValue(), pricePerKM);
//                    Scene scene = new Scene(root);
//                    
//                    stageStation.setScene(scene);
//                    stageStation.show();
                    RailwayFull.saveLog("PURCHASED "+n+" TICKET(S) FOR TRAIN "+train.getValue()+" FROM "+stationFrom.getValue()+" TO "+stationTo.getValue()+" OF CLASS "+seatClass.getValue()+" FOR PRICE "+(s1.calcPrice(s2, seatClass.getValue(),pricePerKM)*noOfSeats.getValue())+" FOR "+date.toString()+" AT "+LocalDateTime.now().toString());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Ticket Purchse Succesfull");
                    alert.setContentText("You have succesfully purchased " + n +" "+ seatClass.getValue()+"s from " + s1.getStationName() + " to " + s2.getStationName());
                    alert.showAndWait();
                    alert.close(); 
                    stationFrom.getEditor().clear();
                    stationTo.getItems().clear();
                    stationTo.setDisable(true);
                    travelDate.getEditor().clear();
                    travelDate.setDisable(true);
                    train.getItems().clear();
                    train.setDisable(true);
                    seatClass.getItems().clear();
                    seatClass.setDisable(true);
                    noOfSeats.getItems().clear();
                    noOfSeats.setDisable(true);
                }
                else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Ticket Purchse Canceled");
                alert.setContentText("You have choosen to cancel your tickcet purchase");
                alert.showAndWait();
                alert.close();
                }
            }
            else{
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR! No Seats Available");
                alert.setContentText("Error: All seats of the selected class have already been booked");
                alert.showAndWait();
                alert.close();
            }
            
            
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
                alert.close();
            }
    }

    @FXML
    void stationToAction(ActionEvent event) {
        if(stationTo.getValue()!=null){
            travelDate.setDisable(false);
            train.setDisable(false);
            seatClass.setDisable(true);
            noOfSeats.setDisable(true);
            
            try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/railway", "root", "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT `RouteNo` FROM `station` WHERE `StationName`='"+stationFrom.getValue()+"' OR StationName='"+stationTo.getValue()+"' GROUP BY `RouteNo` HAVING COUNT(`RouteNo`)>1");
            
             String str = "";
            rs.first();
            str=""+rs.getInt("RouteNo");
            while(rs.next())
                str=str+", "+rs.getInt("RouteNo");
            //System.out.println(str);
            
            rs = st.executeQuery("SELECT DISTINCT `TrainName` FROM `train` WHERE `RouteNo` IN ("+str+")");
            train.getItems().clear();
            while(rs.next())
                train.getItems().add(rs.getString("TrainName"));
            
            
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
                alert.close();
            }
        }
    }

    @FXML
    void datePickerAction(ActionEvent event) {
        if(travelDate.getValue()!=null){
            LocalDate current = LocalDate.now();
            if(DAYS.between(current, travelDate.getValue())>10||DAYS.between(current, travelDate.getValue())<0){
                msg_box.setText("ERROR: Date must be within the next 10 days");
                travelDate.getEditor().clear();
            }
            else
                msg_box.setText("");
        }
    }

    @FXML
    void stationFromAction(ActionEvent event) {
        if(stationFrom.getValue()!=null){
            stationTo.getItems().clear();
            try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/railway", "root", "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT `RouteNo` FROM `station` WHERE `station`.`StationName`='"+stationFrom.getValue()+"'");
            
            String str = "";
            rs.first();
            str=""+rs.getInt("RouteNo");
            while(rs.next())
                str=str+", "+rs.getInt("RouteNo");
           // System.out.println(str);
            rs = st.executeQuery("SELECT DISTINCT `StationName` FROM `station` WHERE `RouteNo` IN ("+str+") ORDER BY `station`.`StationName` ASC");
            while(rs.next()){
                stationTo.getItems().add(rs.getString("StationName"));
            }
            stationTo.getItems().removeAll(stationFrom.getValue());
            
            
            }catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
                alert.close();
            }
            stationTo.setDisable(false);
            travelDate.setDisable(true);
            train.setDisable(true);
            seatClass.setDisable(true);
            noOfSeats.setDisable(true);
        }
    }

    @FXML
    void trainPickerAction(ActionEvent event) {
        seatClass.setDisable(false);
        seatClass.getItems().clear();
        seatClass.getItems().addAll("Shovon Seat","Shovon Chair Seat","AC Seat");
    }

    @FXML
    void seatClassPickerAction(ActionEvent event) {
        noOfSeats.setDisable(false);
        noOfSeats.getItems().clear();
        noOfSeats.getItems().addAll(1,2,3,4);
    }

    @FXML
    void noOfSeatsPickerActiom(ActionEvent event) {
        
    }
    
    @FXML
    void initialize(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/railway", "root", "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT `StationName` FROM `station` ORDER BY `station`.`StationName` ASC ");
            
            while(rs.next()){
                stationFrom.getItems().add(rs.getString("StationName"));
            }
            
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
            alert.close();
        }
    }
    
    

}
