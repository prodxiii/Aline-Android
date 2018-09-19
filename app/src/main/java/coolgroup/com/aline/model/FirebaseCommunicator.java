package coolgroup.com.aline.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseCommunicator implements iServerCommunicator {

    // Declare Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference users;

    public FirebaseCommunicator() {
        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * Getter for FirebaseDatabase
     *
     * @return db
     */
    public FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    /**
     * Authenticate a user by email address and password.
     *
     * @param email    The email registered to the account.
     * @param password The user’s password.
     * @return True if the account exists and details are correct, else false.
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
     * @return True if the account exists and details are correct, else false.
     */
    @Override
    public void logInUserPhone(String phone, String password, OnSuccessListener<User> listener) {
        return;
    }

    /**
     * Register an account for a user.
     *
     * @param email    The user’s email address.
     * @param password The user’s selected password.
     * @param name     The user’s full name.
     * @param phone    The user’s phone number.
     * @return True if the user doesn’t already exist (e.g. email taken)
     * and the format of all arguments is valid (e.g. password length).
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
     * @param email The email of the user to be queried.
     * @param name  The name of the user to be queried.
     * @param phone The phone of the user to be queried.
     * @return The user ID if the user exists, else null.
     */
    @Override
    public String getUserId(String email, String name, String phone) {
        return null;
    }

    /**
     * Retrieve the basic details of a user.
     *
     * @param userId The user ID of the user to be queried.
     * @return A User object corresponding to the user, if it exists (else null).
     */
    @Override
    public User getBasicUserInfo(String userId) {
        return null;
    }

    /**
     * Retrieve all contacts of a user.
     *
     * @param userId The user to be queried.
     * @return An ArrayList of the user’s contacts.
     */
    @Override
    public ArrayList<String> getContactsList(String userId) {
        return null;
    }

    /**
     * Add a new user to the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be added.
     * @return True if the update was successful.
     */
    @Override
    public boolean addNewContact(String userId, String contactUserId) {
        return false;
    }

    /**
     * Remove a user from the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be removed.
     * @return True if the removal was successful.
     */
    @Override
    public boolean removeContact(String userId, String contactUserId) {
        return false;
    }

    private User firebaseUsertoUser(FirebaseUser mFirebaseUser) {
        return new User(mFirebaseUser.getEmail(),
                        mFirebaseUser.getDisplayName(),
                        mFirebaseUser.getPhoneNumber(),
                        mFirebaseUser.getUid());
    }


}
