package coolgroup.com.aline.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import coolgroup.com.aline.Model.User;

public class ContactsListViewModel extends ViewModel {

    private LiveData<ArrayList<User>> contacts;

    public LiveData<ArrayList<User>> getContacts() {
        return contacts;
    }
}
