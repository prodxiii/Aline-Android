package coolgroup.com.aline;

import coolgroup.com.aline.model.FirebaseCommunicator;
import coolgroup.com.aline.model.iServerCommunicator;
import coolgroup.com.aline.model.User;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    // Controlled classes
    public final iServerCommunicator serverCommunicator = new FirebaseCommunicator();
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

    public void setMainUser(User user) {
        mainUser = user;
    }

}
