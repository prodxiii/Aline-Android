package coolgroup.com.aline.Model;

public interface iVOIPNotifier {

    void addListener(iVOIPListener listener);

    void removeListener(iVOIPListener listener);

}
