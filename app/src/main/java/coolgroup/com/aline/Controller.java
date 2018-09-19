package coolgroup.com.aline;

import com.google.firebase.database.FirebaseDatabase;

import coolgroup.com.aline.model.FirebaseCommunicator;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.model.iServerCommunicator;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    public final iServerCommunicator serverCommunicator = new FirebaseCommunicator(db);

    private User mainUser;

    private Controller() {}

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
