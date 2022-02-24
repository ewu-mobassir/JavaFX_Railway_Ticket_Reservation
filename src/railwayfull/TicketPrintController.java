package railwayfull;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketPrintController {

    @FXML
    private static Label TKSeats;

    @FXML
    private static Label TKCurrentDate;

    @FXML
    private static Label TKTo;

    @FXML
    private static Label TKTravelDate;

    @FXML
    private static Label TKFrom;

    @FXML
    private static Label TKClass;

    @FXML
    private static Label TKFare;

    @FXML
    private static Label TKName;

    @FXML
    void c60000(ActionEvent event) {

    }
    
    @FXML
    public static void printTicket(String trainName, String stFrom, String stTo, int noOfSeats, LocalDate travelDate, String seatClass, double fare){
        TKName.setText(trainName);
        TKFrom.setText(stFrom);
        TKTo.setText(stTo);
        TKSeats.setText(noOfSeats+"");
        TKCurrentDate.setText(LocalDate.now().toString());
        TKTravelDate.setText(travelDate.toString());
        TKClass.setText(seatClass);
        TKFare.setText(fare+"");
    }

}
