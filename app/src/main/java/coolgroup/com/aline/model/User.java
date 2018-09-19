package coolgroup.com.aline.model;

import com.google.firebase.database.Exclude;

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
