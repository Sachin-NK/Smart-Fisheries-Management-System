package com.example.connecter;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.stage.Stage;




import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class Controller {

    @FXML
    private FlowPane imageContainer;

    @FXML
    private Button btn1;

    @FXML
    private  Button btn2;

    @FXML
    private BarChart weatherChart;

    @FXML
    public void initialize() {

        loadImages();


    }

    public void loadImages() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT photo,name,discription FROM images1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(90);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(10));


// Track position
            int column = 0;
            int row = 0;
            int cardsPerRow = 3;

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("photo");
                String name = rs.getString("name");
                String details = rs.getString("discription");

                if (imageBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);

                    Rectangle clip = new Rectangle(150, 150);
                    clip.setArcWidth(30);
                    clip.setArcHeight(30);
                    imageView.setClip(clip);
                    imageView.getStyleClass().add("image-view");

                    Label nameLabel = new Label(name);
                    nameLabel.getStyleClass().add("name-label");

                    Label detailLabel = new Label(details);
                    detailLabel.getStyleClass().add("detail-label");
                    detailLabel.setWrapText(true);
                    detailLabel.setMaxWidth(150);
                    detailLabel.setMaxHeight(100);

                    VBox card = new VBox(imageView, nameLabel, detailLabel);
                    card.setSpacing(10);
                    card.getStyleClass().add("card");

                    // Add to grid at current column and row
                    gridPane.add(card, column, row);

                    column++;
                    if (column == cardsPerRow) {
                        column = 0;
                        row++;
                    }
                }
            }

// Replace FlowPane content with GridPane
            imageContainer.getChildren().clear();
            imageContainer.getChildren().add(gridPane);




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sample_add() {
        try {
            System.out.println(getClass().getResource("Hellow"));
            Stage stage = (Stage) btn1.getScene().getWindow();
            stage.close();

            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/connecter/chat_view.fxml"));
            System.out.println(getClass().getResource("/com/example/connecter/chat_view.fxml"));


            newStage.setTitle("Add Contact");
            newStage.setScene(new Scene(root, 500, 500));
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Whether_info() {
        try {
            // Close current window (optional, if you want to replace it)
            Stage oldStage = (Stage) btn2.getScene().getWindow();
            oldStage.close();

            // Load weather_info.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/connecter/weather_info.fxml"));
            Parent root = loader.load();

            // Create new stage and show the new scene
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Weather Info");
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}





