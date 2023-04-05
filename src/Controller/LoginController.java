package Controller;

import Model.Utility;
import Model.User;
import View.ErrorPopUp.Error;
import View.Main;
import View.MainWithLogin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    HashMap<String, User> users = new HashMap<>();
    private String username = null;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private AnchorPane userPane;
    @FXML
    private AnchorPane passwordPane;
    @FXML
    private Button loginButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsers("src/Data/users.xml");
            DropShadow shadow = new DropShadow(13, Color.valueOf("#4cc9f0"));
            userPane.setEffect(shadow);
            passwordPane.setEffect(shadow);
            usernameField.setOnAction(actionEvent -> checkUsername());
            passwordField.setOnAction(actionEvent -> checkPassword());
            loginButton.setOnMouseClicked(actionEvent -> checkPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean checkUsername(){
        try{
            String name = usernameField.getText().trim();
            if(name.length() == 0){
                new Error().showError(usernameField, "Please enter a valid username", "");
                return false;
            } else{
                if(users.containsKey(name)){
                    this.username = name;
                    return true;
                } else{
                    new Error().showError(usernameField, "Username does not exist, please check again", name);
                    passwordField.clear();
                    return false;
                }
            }

        } catch (Exception ignored){return false;}
    }

    public void checkPassword(){
        try{
            String password = passwordField.getText().trim();
            String name = usernameField.getText().trim();
            if(name.length() > 0 && password.length() > 0 && checkUsername()){
                if(users.get(username).getPassword().equals(Utility.toHexString(Utility.getSHA(password)))) {
                    MainWithLogin.getStage().close();
                    new Main(username).start(new Stage());
                } else{
                    new Error().showError(passwordField, "Invalid password", "");
                }
            } else if (name.length() <= 0){
                new Error().showError(usernameField, "Please enter a valid username", "");
            } else if (password.length() <= 0){
                new Error().showError(passwordField, "Please enter a valid password", "");
            } else{
                new Error().showError(usernameField, "Username does not exist, please check again", name);
            }
        } catch (Exception ignored){}
    }

    //have the xml ready before writing this writer
    //this function read from a xml file and instantiate list of airports available
    public void loadUsers(String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);

        // Get a NodeList of all airport elements
        NodeList userElements = doc.getElementsByTagName("user");

        // Loop over each airport element and create an Airport object
        for (int i = 0; i < userElements.getLength(); i++) {
            Node userNode = userElements.item(i);

            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;

                //get username
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                String airport = userElement.getElementsByTagName("airport").item(0).getTextContent();
                int role = Integer.parseInt(userElement.getElementsByTagName("role").item(0).getTextContent());

                User user = new User(username, password, airport, role);
                users.put(username, user);
            }
        }
    }
}
