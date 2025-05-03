module com.example.connecter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;


    opens com.example.connecter to javafx.fxml;
    exports com.example.connecter;
}