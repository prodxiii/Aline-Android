package coolgroup.com.aline.view;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class test {

    public void createUserChild(String name, String email, String phone) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("email", email);
        userMap.put("status", "Hi there, I'm using Aline.");
        userMap.put("image", "default");
        userMap.put("thumb_image", "default");

        mDatabase.setValue(userMap);
    }
}
