package coolgroup.com.aline.Model;

/**
 * This interface acts as a wrapper interface for any class that implements VOIP calling
 * functionality.
 */
public interface iVOIPCommunicator {

    void startCall(User user);

    void hangUpCall();

}
