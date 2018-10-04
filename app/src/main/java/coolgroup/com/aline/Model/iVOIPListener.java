package coolgroup.com.aline.Model;

/**
 * Classes that implement this interface are ones which react to events that take place in an
 * iVOIPNotifier instance. Typical usages of this interface should be UX events, or perhaps model
 * update events (such as most recent calls, etc). This interface fulfils the Observer software
 * pattern, in partnership with iVOIPNotifier.
 */
public interface iVOIPListener {

    /**
     * Method called by an iVOIPNotifier instance when a VOIP call ends.
     */
    void onCallEnded();

    /**
     * Method called by an iVOIPNotifier instance when a VOIP call begins.
     */
    void onCallEstablished();

    /**
     * Method called by an iVOIPNotifier instance when a VOIP call begins to ring.
     */
    void onCallRinging();

}
