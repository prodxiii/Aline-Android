package coolgroup.com.aline.Model;

/**
 * Anything implementing this interface is a class that handles VOIP communication. It should fulfil
 * the Notifier part of the Observer software pattern. Its partner Observer interface is
 * iVOIPListener.java. Classes which implement this interface should call the methods in
 * iVOIPListener as appropriate.
 */
public interface iVOIPNotifier {

    /**
     * @param listener the iVOIPListener to be added to this class' internal list.
     */
    void addListener(iVOIPListener listener);

    /**
     * @param listener the iVOIPListener to be removed from this class' internal list.
     */
    void removeListener(iVOIPListener listener);

}
