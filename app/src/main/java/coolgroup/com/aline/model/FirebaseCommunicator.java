package coolgroup.com.aline.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import coolgroup.com.aline.Controller;

public class FirebaseCommunicator implements iServerCommunicator {

    // Declare Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public FirebaseCommunicator() {
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * Retrieve a user ID string.
     *
     * @return The user ID if the user exists, else null.
     */
    @Override
    public String getCurrentUID() {
        return mAuth.getCurrentUser().getUid();
    }

    /**
     * AuthenticateActivity a user by email address and password.
     *
     * @param email    The email registered to the account.
     * @param password The user’s password.
     * @return True if the account exists and details are correct, else false.
     */
    @Override
    public Task<AuthResult> logInUserEmail(String email, String password) {
        Task<AuthResult> toReturn = mAuth.signInWithEmailAndPassword(email, password);
//        Controller.getInstance().setMainUser(new User(email, password));
//        Controller.getInstance().getMainUser().setuID(mFirebaseAuth.getCurrentUser().getUid());
        return toReturn;
    }

    /**
     * AuthenticateActivity a user by phone number and password.
     *
     * @param phone    The phone number registered to the account.
     * @param password The user’s password.
     * @return True if the account exists and details are correct, else false.
     */
    @Override
    public boolean logInUserPhone(String phone, String password) {
        return false;
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
    public Task<AuthResult> signUpUser(String email, String password, String name, String phone) {
//        Controller currentController = Controller.getInstance();
        Task<AuthResult> toReturn = mAuth.createUserWithEmailAndPassword(email, password);
//        currentController.setMainUser(new User(email, password));
//        currentController.getMainUser().setName(name);
//        currentController.getMainUser().setPhone(name);
//        currentController.getMainUser().setuID(mFirebaseAuth.getCurrentUser().getUid());
        return toReturn;
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

    /**
     * Create user reference in the Firebase Realtime Database
     *
     * @param name  The name of user
     * @param email The login email of user
     * @param phone The phone number of user
     *
     * @return Task<Void>
     */
    @Override
    public Task<Void> createUserChild(String name, String email, String phone) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("AllUsersActivity").child(uid);

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("email", email);
        userMap.put("status", "Hi there, I'm using Aline.");
        userMap.put("image", "default");
        userMap.put("thumbnail", "default");

        return mDatabase.setValue(userMap);
    }

    public FirebaseDatabase getmDatabase() {
        return mDatabase;
    }
}
