package Model;

public class User {
    private String username;
    private String name;
    private String password;
    private String airportID; //which airport is the user associated to
    private int role;
    //1: super admin, can add airport, can add airport manager
    //2: airport manager, can manage users
    //3: normal atc: can import/export details


    public User(String username, String name, String password, String airportID, int role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.airportID = airportID;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccessControl() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getAirportID() {
        return airportID;
    }

    public int getRole() {
        return role;
    }
}
