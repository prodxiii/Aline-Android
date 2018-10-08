package coolgroup.com.aline.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import coolgroup.com.aline.model.User;

public class ContactsListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> contacts;

    public LiveData<ArrayList<User>> getContacts() {
        if (contacts == null) {
            contacts = new MutableLiveData<ArrayList<User>>();
            loadContacts();
        }
        return contacts;
    }

    private void loadContacts() {
        // Do an asynchronous operation to load contacts
    }
}
