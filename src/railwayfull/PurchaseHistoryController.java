package railwayfull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class PurchaseHistoryController {

    @FXML
    private Button close_btn;

    @FXML
    private TextArea display_board;

    @FXML
    void close(ActionEvent event) {
        Stage currentStage = (Stage)close_btn.getScene().getWindow();
        currentStage.close();
    }
    
    public void initialize() throws FileNotFoundException{
        FileInputStream inStream = new FileInputStream("log.txt");
        Scanner input = new Scanner(inStream);
        String str = "";
        while(input.hasNext()){
            str = str+input.nextLine()+"\n";
        }
        display_board.setText(str);
    }

}
