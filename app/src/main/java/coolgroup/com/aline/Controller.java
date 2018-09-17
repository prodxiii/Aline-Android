package coolgroup.com.aline;

import coolgroup.com.aline.model.FirebaseCommunicator;
import coolgroup.com.aline.model.User;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    // Controlled classes
    public FirebaseCommunicator serverCommunicator = new FirebaseCommunicator(); // Not sure if this should be of type iServerCommunicator or FirebaseCommunicator?
    private User mainUser;

    private Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            Controller.instance = new Controller();
        }
        return instance;
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }
}
