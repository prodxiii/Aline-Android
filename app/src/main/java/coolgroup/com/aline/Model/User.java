package coolgroup.com.aline.Model;

import com.google.firebase.database.Exclude;

/**
 * Immutable class for representing basic attributes of a user.
 * This class should remain a data structure only.
 * This should not have any functionality.
 */
public final class User {

    @Exclude  // so that id is not duplicated in database
    public final String id;

    public final String email, name, phone;

    public User(String id, String email, String name, String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

}
