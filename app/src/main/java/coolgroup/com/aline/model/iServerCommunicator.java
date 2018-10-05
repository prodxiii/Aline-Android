package coolgroup.com.aline.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface iServerCommunicator {

    /**
     * Return the Firebase Database reference.
     */
    FirebaseDatabase getFirebaseDatabase();

    /**
     * Authenticate a user by email address and password.
     *
     * @param email    The email registered to the account.
     * @param password The user’s password.
     * @param listener Listener called upon successful authentication.
     */
    void logInUserEmail(String email, String password, OnSuccessListener<User> listener);

    /**
     * Authenticate a user by phone number and password.
     *
     * @param phone    The phone number registered to the account.
     * @param password The user’s password.
     * @param listener Listener called upon successful authentication.
     */
    void logInUserPhone(String phone, String password, OnSuccessListener<User> listener);

    /**
     * Register an account for a user.
     *
     * @param email    The user’s email address.
     * @param password The user’s selected password.
     * @param name     The user’s full name.
     * @param phone    The user’s phone number.
     * @param listener Listener called upon successful registration.
     */
    void signUpUser(String email, String password, String name, String phone, OnSuccessListener<User> listener);

    /**
     * Retrieve a user ID string.
     *
     * @param email    The email of the user to be queried.
     * @param name     The name of the user to be queried.
     * @param phone    The phone of the user to be queried.
     * @param listener Listener called upon retrieval.
     */
    void getUserId(String email, String name, String phone, OnSuccessListener<String> listener);

    /**
     * Retrieve the basic details of a user.
     *
     * @param userId   The user ID of the user to be queried.
     * @param listener Listener called upon retrieval.
     */
    void getBasicUserInfo(String userId, OnSuccessListener<User> listener);

    /**
     * Retrieve all contacts of a user.
     *
     * @param userId   The user to be queried.
     * @param listener Listener called upon retrieval.
     */
    void getContactsList(String userId, OnSuccessListener<List<String>> listener);

    /**
     * Retrieve all contacts of a user as User instances.
     *
     * @param userId   The user to be queried.
     * @param listener Listener called upon retrieval.
     */
    void getContactsUserList(String userId, OnSuccessListener<List<User>> listener);

    /**
     * Add a new user to the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be added.
     */
    void addContact(String userId, String contactUserId);

    /**
     * Remove a user from the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be removed.
     */
    void removeContact(String userId, String contactUserId);

}
