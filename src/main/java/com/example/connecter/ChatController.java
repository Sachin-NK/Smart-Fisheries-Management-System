package com.example.connecter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatController {

    @FXML
    private ListView<ChatMessage> chatListView;

    private Connection getConnection() throws SQLException {
        // Adjust for MySQL or SQLite as needed
        String url = "jdbc:mysql://localhost:3306/fishing";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    private List<ChatMessage> loadMessagesFromDB() {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT sender_username, message, timestamp, message_type FROM chat_messages";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                messages.add(new ChatMessage(
                        rs.getString("sender_username"),
                        rs.getString("message"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("message_type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    @FXML
    public void initialize() {
        List<ChatMessage> list = loadMessagesFromDB();
        ObservableList<ChatMessage> obsList = FXCollections.observableArrayList(list);
        chatListView.setItems(obsList);

        chatListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage msg, boolean empty) {
                super.updateItem(msg, empty);

                if (empty || msg == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Example: the current user's username is "Alice"
                    boolean isCurrentUser = "Alice".equals(msg.getSenderUsername());

                    String displayText = msg.getSenderUsername() + ": " + msg.getMessage();

                    setText(displayText);

                    if (isCurrentUser) {
                        // Align right for current user
                        setStyle("-fx-alignment: CENTER-RIGHT; -fx-background-color: #dcf8c6; -fx-padding: 5px;");
                    } else {
                        // Align left for others or system
                        setStyle("-fx-alignment: CENTER-LEFT; -fx-background-color: #ffffff; -fx-padding: 5px;");
                    }
                }
            }
        });

    }
}
