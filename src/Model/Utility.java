package Model;

import View.Main;
import View.MainWithLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {

    @FXML
    public static void handleLogout(ActionEvent event) throws Exception {
        Main.getStage().close();
        new MainWithLogin().start(new Stage());
    }

    public static int getScaleRange(double input){
        if(input <= 500){
            return 90;
        } else if(input <= 1000){
            return 240;
        } else if(input <= 1500){
            return 390;
        } else if (input <= 2000){
            return 600;
        } else if (input <= 2500){
            return 750;
        } else if (input <= 3000){
            return 960;
        } else if (input <= 3500){
            return 1200;
        } else{
            return 1500;
        }
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }



    //allow zoom in / out when hodl down ctrl
    public static void initializeZoom(AnchorPane pane){
        pane.setOnScroll(scrollEvent -> {
            if (scrollEvent.isShortcutDown()){
                double zoom = scrollEvent.getDeltaY();
                System.out.println(pane.getScaleX() - zoom * -0.005);
                if (pane.getScaleX()>=0.5 && pane.getScaleY()>=0.5){
                    pane.setScaleX(pane.getScaleX() - zoom * -0.005);
                    pane.setScaleY(pane.getScaleY() - zoom * -0.005);
                }else if (pane.getScaleX()<0.5 && pane.getScaleY()<0.5){
                    pane.setScaleX(0.5);
                    pane.setScaleY(0.5);
                }

                scrollEvent.consume();
            }
        });
    }
    public static void initializeDrag(AnchorPane pane){
        pane.setOnMousePressed(mouseEvent->{
            if (mouseEvent.isShortcutDown()){
                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();
                pane.setOnMouseDragged(dragEvent -> {
                    double translationX = dragEvent.getX() - mouseX;
                    double translationY = dragEvent.getY() - mouseY;
                    pane.setTranslateX(pane.getTranslateX() + translationX);
                    pane.setTranslateY(pane.getTranslateY() + translationY);
                    dragEvent.consume();
                });
            }
        });
        pane.setOnMouseReleased(releaseEvent -> {
            pane.setOnMouseDragged(null);
        });
    }





}
