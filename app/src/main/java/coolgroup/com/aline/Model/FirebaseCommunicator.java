package coolgroup.com.aline.Model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseCommunicator implements iServerCommunicator {

    private final FirebaseDatabase db;
    private final DatabaseReference users;
    private final DatabaseReference contacts;

    public FirebaseCommunicator(FirebaseDatabase db) {
        this.db = db;
        users = db.getReference("Users");
        contacts = db.getReference("Contacts");
    }

    @Override
    public boolean logInUserEmail(String email, String password) {
        return false;
    }

    @Override
    public boolean logInUserPhone(String phone, String password) {
        return false;
    }

    @Override
    public boolean signUpUser(String email, String password, String name, String phone) {
        return false;
    }

    @Override
    public String getUserId(String email, String name, String phone) {
        return null;
    }

    @Override
    public User getBasicUserInfo(String userId) {
        return null;
    }

    @Override
    public ArrayList<String> getContactsList(String userId) {
        ArrayList<String> contactList = new ArrayList<>();

        // reference to user node in the contacts database subtree
        DatabaseReference userRef = contacts.child(userId);

        // each child key of the user node corresponds to a contact user ID
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;

                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contactId = contactSnapshot.getKey();
                    contactList.add(contactId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return contactList;
    }

    @Override
    public boolean addNewContact(String userId, String contactUserId) {
        if (!userExists(userId) || !userExists(contactUserId))
            return false;

        // reference to main user node in the contacts database subtree
        DatabaseReference userRef = contacts.child(userId);

        // add new key-value pair under the user node (only key matters)
        userRef.child(contactUserId).setValue(true);
        return true;
    }

    @Override
    public boolean removeContact(String userId, String contactUserId) {
        if (!userExists(userId) || !userExists(contactUserId))
            return false;

        // reference to main user node in the contacts database subtree
        DatabaseReference userRef = contacts.child(userId);

        // remove the key-value pair corresponding to the contact to be deleted
        userRef.child(contactUserId).removeValue();
        return true;
    }

    /**
     * Container class for passing values from anonymous inner class to outer scope.
     * @param <T> Type of the value to be passed
     */
    public class ValueContainer<T> {
        private T value;

        public ValueContainer() {

        }

        public ValueContainer(T v) {
            this.value = v;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T val) {
            this.value = value;
        }
    }

    /**
     * Determine whether a user ID is valid (corresponds to a user in the database)
     * @param userId The user ID to be checked
     * @return True iff the user ID is valid
     */
    private boolean userExists(String userId) {
        DatabaseReference userRef = users.child(userId);
        ValueContainer<Boolean> result = new ValueContainer<>(false);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    result.setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return result.getValue();
    }

}
