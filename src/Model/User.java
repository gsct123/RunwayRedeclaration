package Model;

public class User {
    private String username;
    private String password;
    private String airport; //which airport is the user associated to
    private int role;
    //1: super admin, can add airport, can add airport manager
    //2: airport manager, can manage users
    //3: normal atc: can import/export details


    public User(String username, String password, String airport, int role) {
        this.username = username;
        this.password = password;
        this.airport = airport;
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

    public String getAirport() {
        return airport;
    }

    public int getRole() {
        return role;
    }
}
