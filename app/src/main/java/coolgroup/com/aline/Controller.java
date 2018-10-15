package coolgroup.com.aline;

import android.content.Context;

import coolgroup.com.aline.model.FirebaseCommunicator;
import coolgroup.com.aline.model.SinchCommunicator;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.model.iServerCommunicator;
import coolgroup.com.aline.model.iVOIPCommunicator;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    // Controlled classes
    public iServerCommunicator serverCommunicator = new FirebaseCommunicator();

    // Cannot create this on startup, since we need the user to be logged in to do it.
    // see createiVOIPCommunicator, which should be called as soon as login is successful.
    private iVOIPCommunicator voipCommunicator;
    private User mainUser;

    private Controller() {

    }

    public static Controller getInstance() {
        if (instance == null) {
            Controller.instance = new Controller();
        }
        return instance;
    }

    public iVOIPCommunicator createiVOIPCommunicator(Context c){
        if( voipCommunicator == null ){
            voipCommunicator = new SinchCommunicator(c, mainUser);
        }
        return voipCommunicator;
    }
    public iVOIPCommunicator getiVOIPCommunicator(){
        return voipCommunicator;
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }
}
