import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable {

    @FXML
    private Button changePwd_backBtn;

    @FXML
    private PasswordField changePwd_cPwd;

    @FXML
    private AnchorPane changePwd_form;

    @FXML
    private Button changePwd_proceeddBtn;

    @FXML
    private PasswordField changePwd_pwd;

    @FXML
    private TextField forgot_answer;

    @FXML
    private Button forgot_backBtn;

    @FXML
    private AnchorPane forgot_form;

    @FXML
    private Button forgot_proceddBtn;

    @FXML
    private ComboBox<?> forgot_selectQ;

    @FXML
    private TextField forgot_username;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private CheckBox login_selectShowPwd;

    @FXML
    private TextField login_showPwd;

    @FXML
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField signup_Pwd;

    @FXML
    private TextField signup_answer;

    @FXML
    private Button signup_btn;

    @FXML
    private PasswordField signup_cPwd;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Button signup_loginBtn;

    @FXML
    private ComboBox<String> signup_selectQ;

    @FXML
    private TextField signup_username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fishmsdb", "root", "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void login() {
        alertMsg alert = new alertMsg();

        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMsg("Incorrect username or password");
        } else {
            String selectData = "SELECT * FROM user WHERE " + "username = ? and password = ?";
            connect = connectDB();
            
            if(login_selectShowPwd.isSelected()){
                login_password.setText(login_showPwd.getText());
            }else{
                login_showPwd.setText(login_password.getText());
            }
            
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, login_password.getText());

                result = prepare.executeQuery();
                if (result.next()) {
                    alert.successMsg("Successfully Login!");
                } else {
                    alert.errorMsg("Incorrect username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showPwd() {
        if (login_selectShowPwd.isSelected()) {
            login_showPwd.setText(login_password.getText());
            login_showPwd.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPwd.getText());
            login_showPwd.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void forgotPwd() {
        alertMsg alert = new alertMsg();

        if (forgot_username.getText().isEmpty() || forgot_selectQ.getSelectionModel().getSelectedItem() == null || forgot_answer.getText().isEmpty()) {
            alert.errorMsg("Please fill all blank fields");
        } else {
            String checkData = "SELECT username , question , answer FROM user " + " WHERE username = ? and question = ? and answer = ?";
            connect = connectDB();
            try {
                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, forgot_username.getText());
                prepare.setString(2, (String) forgot_selectQ.getSelectionModel().getSelectedItem());
                prepare.setString(3, forgot_answer.getText());

                result = prepare.executeQuery();
                if (result.next()) {
                    signup_form.setVisible(false);
                    login_form.setVisible(false);
                    forgot_form.setVisible(false);
                    changePwd_form.setVisible(true);
                } else {
                    alert.errorMsg("Incorrect Information");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void forgotQList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        forgot_selectQ.setItems(listData);
    }

    public void register() {
        alertMsg alert = new alertMsg();

        if (signup_email.getText().isEmpty()
                || signup_username.getText().isEmpty()
                || signup_Pwd.getText().isEmpty()
                || signup_cPwd.getText().isEmpty() //|| signup_selectQ.getSelectionModel().getSelectedItem()== null 
                //|| signup_answer.getText().isEmpty()
                ) {
            alert.errorMsg("All fields are necessary to be filled");

        } else if (!signup_Pwd.getText().equals(signup_cPwd.getText())) {
            alert.errorMsg("Password does not match");
        } else if (signup_Pwd.getText().length() < 8) {
            alert.errorMsg("Invalid Password, at least 8 characters needed");
        } else {
            String checkUsername = "SELECT * FROM user WHERE username = '" + signup_username.getText() + "' ";
            connect = connectDB();
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkUsername);

                if (result.next()) {
                    alert.errorMsg(signup_username.getText() + " is already exist.");
                } else {
                    String insertData = "INSERT INTO user " + "(email, username, password, question, answer, date)" + "VALUES(?,?,?,?,?,?)";
                    //String insertData = "INSERT INTO user "+ "(email, username, password, date)" + "VALUES(?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, signup_email.getText());
                    prepare.setString(2, signup_username.getText());
                    prepare.setString(3, signup_Pwd.getText());
                    prepare.setString(4, (String) signup_selectQ.getSelectionModel().getSelectedItem());
                    prepare.setString(5, signup_answer.getText());

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(6, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert.successMsg("Registered Successfully ");

                    registerClearFields();

                    signup_form.setVisible(false);
                    login_form.setVisible(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changePwd() {
        alertMsg alert = new alertMsg();

        if (changePwd_pwd.getText().isEmpty() || changePwd_cPwd.getText().isEmpty()) {
            alert.errorMsg("Please fill all blank fields");
        }

        if (!changePwd_pwd.getText().equals(changePwd_cPwd.getText())) {
            alert.errorMsg("Password does not match");
        } else if (changePwd_pwd.getText().length() < 8) {
            alert.errorMsg("Invalid Password, at least 8 characters needed.");
        } else {
            String updateData = "UPDATE user SET password = ?  , updateDate = ?" + "WHERE username =  '" + forgot_username.getText() + "' ";

            connect = connectDB();

            try {
                prepare = connect.prepareStatement(updateData);
                prepare.setString(1, changePwd_pwd.getText());

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(2, String.valueOf(sqlDate));

                prepare.executeUpdate();
                alert.successMsg("Successfully change password");

                signup_form.setVisible(false);
                login_form.setVisible(true);
                forgot_form.setVisible(false);
                changePwd_form.setVisible(false);
                
                login_username.setText("");
                login_password.setVisible(true);
                login_password.setText("");              
                login_showPwd.setVisible(false);                
                login_selectShowPwd.setSelected(false);
                
                changePwd_pwd.setText("");
                changePwd_cPwd.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == signup_loginBtn || event.getSource() == forgot_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(true);
            forgot_form.setVisible(false);
            changePwd_form.setVisible(false);
        } else if (event.getSource() == login_createAccount) {
            signup_form.setVisible(true);
            login_form.setVisible(false);
            forgot_form.setVisible(false);
            changePwd_form.setVisible(false);
        } else if (event.getSource() == login_forgotPassword || event.getSource() == changePwd_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePwd_form.setVisible(false);

            forgotQList();
        } else if (event.getSource() == changePwd_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePwd_form.setVisible(false);
        }
    }

    public void registerClearFields() {
        signup_email.setText("");
        signup_username.setText("");
        signup_Pwd.setText("");
        signup_cPwd.setText("");
        signup_selectQ.getSelectionModel().clearSelection();
        signup_answer.setText("");

    }

    private String[] questionList = {"What is your favourite food?", "What is your favourite color?", "What is the name of your pet?", "What is your favourite sport?"};

    public void questions() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        signup_selectQ.setItems(listData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questions();
        forgotQList();
        //System.out.println("FXML loaded!");

    }
}
