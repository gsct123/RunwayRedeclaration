package Model;

import javafx.collections.ObservableList;

public class Airport {
    private String name;
    private ObservableList<LogicalRunway> logicalRunways;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<LogicalRunway> getLogicalRunways() {
        return logicalRunways;
    }

    public void setLogicalRunways(ObservableList<LogicalRunway> logicalRunways) {
        this.logicalRunways = logicalRunways;
    }
}
