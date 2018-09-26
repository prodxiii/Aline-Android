package coolgroup.com.aline.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.model.User;

public class ContactsListViewModel extends ViewModel {

    private MutableLiveData<List<User>> contacts;

    public LiveData<List<User>> getContacts() {
        if (contacts == null) {
            contacts = new MutableLiveData<>();
            loadContacts();
        }
        return contacts;
    }

    private void loadContacts() {
        String mainUid = Controller.getInstance().getMainUser().id;
        contacts.setValue(Controller.getInstance().serverCommunicator.getContactsUserList(mainUid));
    }

}
