package coolgroup.com.aline.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import coolgroup.com.aline.R;

public class Chat extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent authIntent = new Intent(Chat.this, Authenticate.class);

            // Validation to stop user from going to the authenticate activity again
            authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Take user to authentication
            startActivity(authIntent);
            finish();
        }

    }
}
