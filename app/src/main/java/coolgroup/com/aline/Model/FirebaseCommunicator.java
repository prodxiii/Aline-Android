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
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contactId = contactSnapshot.getKey().toString();
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
        return false;
    }

    @Override
    public boolean removeContact(String userId, String contactUserId) {
        return false;
    }
}
