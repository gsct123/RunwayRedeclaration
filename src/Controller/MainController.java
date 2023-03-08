package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.util.stream.Collectors;

public class MainController {
    private ObservableList<String> items = FXCollections.observableArrayList();

    private MenuButton airportMenu;

    public void initialize() {
        items.addListener((ListChangeListener<String>) change -> {
            airportMenu.getItems().setAll(
                    items.stream().map(MenuItem::new).collect(Collectors.toList())
            );
        });
    }

}
