package coolgroup.com.aline;

public class Controller {
    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    public Controller() {
        return;
    }

    public static Controller getInstance() {
        return instance;
    }
}
