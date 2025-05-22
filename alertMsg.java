
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class alertMsg {

        private Alert alert;
        
        public void errorMsg(String msg){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }
        
        public void successMsg (String msg){
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }
    }