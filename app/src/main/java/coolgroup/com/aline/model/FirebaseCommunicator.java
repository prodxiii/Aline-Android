package coolgroup.com.aline.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCommunicator implements iServerCommunicator {

    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    private final DatabaseReference users = mFirebaseDatabase.getReference("Users");
    private final DatabaseReference contacts = mFirebaseDatabase.getReference("Contacts");

    public FirebaseCommunicator() {

    }

    /**
     * Return the Firebase Database reference.
     */
    public FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    /**
     * Authenticate a user by email address and password.
     *
     * @param email    The email registered to the account.
     * @param password The user’s password.
     * @param listener Listener called upon successful authentication.
     */
    @Override
    public void logInUserEmail(String email, String password, OnSuccessListener<User> listener) {
        Task<AuthResult> task = mFirebaseAuth.signInWithEmailAndPassword(email, password);
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser mFirebaseUser = authResult.getUser();
                listener.onSuccess(firebaseUsertoUser(mFirebaseUser));
            }
        });
    }

    /**
     * Authenticate a user by phone number and password.
     *
     * @param phone    The phone number registered to the account.
     * @param password The user’s password.
     * @param listener Listener called upon successful authentication.
     */
    @Override
    public void logInUserPhone(String phone, String password, OnSuccessListener<User> listener) {

    }

    /**
     * Register an account for a user.
     *
     * @param email    The user’s email address.
     * @param password The user’s selected password.
     * @param name     The user’s full name.
     * @param phone    The user’s phone number.
     * @param listener Listener called upon successful registration.
     */
    @Override
    public void signUpUser(String email, String password, String name, String phone, OnSuccessListener<User> listener) {
//        Controller currentController = Controller.getInstance();
//        Task<AuthResult> toReturn = mFirebaseAuth.createUserWithEmailAndPassword(email, password);
//        //TODO: check that login succeeded before assigning values as if it has.
//        currentController.setMainUser(
//                new User(email, name, phone, mFirebaseAuth.getCurrentUser().getUid())
//                );
//        return;
    }

    /**
     * Retrieve a user ID string.
     *
     * @param email    The email of the user to be queried.
     * @param name     The name of the user to be queried.
     * @param phone    The phone of the user to be queried.
     * @param listener Listener called upon retrieval.
     */
    @Override
    public void getUserId(String email, String name, String phone, OnSuccessListener<String> listener) {

    }

    /**
     * Retrieve the basic details of a user.
     *
     * @param userId   The user ID of the user to be queried.
     * @param listener Listener called upon retrieval.
     */
    @Override
    public void getBasicUserInfo(String userId, OnSuccessListener<User> listener) {
        // reference to user node in the users database subtree
        DatabaseReference userRef = users.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                listener.onSuccess(new User(userId, email, name, phone));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieve all contacts of a user.
     *
     * @param userId   The user to be queried.
     * @param listener Listener called upon retrieval.
     */
    @Override
    public void getContactsList(String userId, OnSuccessListener<List<String>> listener) {
        List<String> contactList = new ArrayList<>();

        // reference to user node in the contacts database subtree
        DatabaseReference userRef = contacts.child(userId);

        // each child key of the user node corresponds to a contact user ID
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contactId = contactSnapshot.getKey();
                    contactList.add(contactId);
                }
                listener.onSuccess(contactList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieve all contacts of a user as User instances.
     *
     * @param userId   The user to be queried.
     * @param listener Listener called upon retrieval.
     */
    @Override
    public void getContactsUserList(String userId, OnSuccessListener<List<User>> listener) {
        List<User> contacts = new ArrayList<>();

        getContactsList(userId, new OnSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> contactIds) {
                for (String contactId : contactIds) {
                    getBasicUserInfo(contactId, new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User contact) {
                            contacts.add(contact);
                        }
                    });
                }
                listener.onSuccess(contacts);
            }
        });
    }

    /**
     * Add a new user to the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be added.
     */
    @Override
    public void addContact(String userId, String contactUserId) {
        contacts.child(userId).push().setValue(contactUserId);
    }

    /**
     * Remove a user from the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be removed.
     */
    @Override
    public void removeContact(String userId, String contactUserId) {
        contacts.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contact : dataSnapshot.getChildren()) {
                    if (contactUserId.equals(contact.getValue(String.class))) {
                        contact.getRef().removeValue();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User firebaseUsertoUser(FirebaseUser mFirebaseUser) {
        return new User(mFirebaseUser.getUid(),
                        mFirebaseUser.getEmail(),
                        mFirebaseUser.getDisplayName(),
                        mFirebaseUser.getPhoneNumber());
    }

}
