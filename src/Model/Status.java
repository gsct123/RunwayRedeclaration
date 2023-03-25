package Model;

public class Status {
    private static boolean reset = false;

    public static void setReset(boolean reset) {
        Status.reset = reset;
    }

    public static boolean isReset() {
        return reset;
    }
}
