package Model;

import java.util.ArrayList;

public class Airport {
    private String name;
    private ArrayList<Runway> runwayList;

    public Airport(String name, ArrayList<Runway> runwayList) {
        this.name = name;
        this.runwayList = runwayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Runway> getRunwayList() {
        return runwayList;
    }

    public void setRunwayList(ArrayList<Runway> runwayList) {
        this.runwayList = runwayList;
    }
}
