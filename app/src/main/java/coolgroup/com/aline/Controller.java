package coolgroup.com.aline;

import com.google.firebase.database.FirebaseDatabase;

import coolgroup.com.aline.Model.FirebaseCommunicator;
import coolgroup.com.aline.Model.User;
import coolgroup.com.aline.Model.iServerCommunicator;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    public final iServerCommunicator serverCommunicator = new FirebaseCommunicator(db);

    private User mainUser;

    public Controller() {}

    /**
     * Initialise the controller with a signed in user.
     * @param user The current signed in user.
     */
    public Controller(User user) {
        mainUser = user;
    }

    public static Controller getInstance() {
        return instance;
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User user) {
        mainUser = user;
    }

}
