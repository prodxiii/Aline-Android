package coolgroup.com.aline;

import coolgroup.com.aline.Model.User;

public class Controller {
    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    private Controller() {
        return;
    }

    public static Controller getInstance() {
        return instance;
    }

    private User user;

    public void setMainUser(User u) {
        user = u;
    }

    public User getMainUser() {
        return user;
    }

}
