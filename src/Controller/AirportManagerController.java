package Controller;

import View.Main;
import View.MainWithLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AirportManagerController implements Initializable {

    @FXML
    private Label identityLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView airportTable;
    @FXML
    private Button importAirportButton;
    @FXML
    private Button deleteAirportButton;
    @FXML
    private TextArea airportDetails;
    @FXML
    private Button editAirportButton;
    @FXML
    private Button exportAirportButton;
    @FXML
    private TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        Main.getStage().close();
        new MainWithLogin().start(new Stage());
    }

}
