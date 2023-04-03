package Model;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Miscellaneous {
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
        pane.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            if (scrollEvent.isShortcutDown()){
                double zoom = scrollEvent.getDeltaY();
                System.out.println(pane.getScaleX() + zoom * -0.01);
                pane.setScaleX(pane.getScaleX() - zoom * -0.01);
                pane.setScaleY(pane.getScaleY() - zoom * -0.01);
                scrollEvent.consume();
            }
        });
    }

    public static void initializeDrag(AnchorPane pane){
        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragEvent -> {
            if (dragEvent.isShortcutDown()){
                System.out.println();
                pane.setTranslateX(pane.getTranslateX() + dragEvent.getX() - pane.getWidth()/2);
                pane.setTranslateY(pane.getTranslateY() + dragEvent.getY() - pane.getHeight()/2);
                System.out.println(pane.getTranslateX() + dragEvent.getX() - pane.getWidth()/2);
                System.out.println(pane.getTranslateY() + dragEvent.getY() - pane.getHeight()/2);
                dragEvent.consume();
            }
        });
    }
}
