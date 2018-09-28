package coolgroup.com.aline.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


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

        // Create the toolbar for the chat activity
        Toolbar mToolbar = findViewById(R.id.chatAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("This is chat activity");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // User is not signed in
        if (currentUser == null) {
            Intent authIntent = new Intent(Chat.this, Authenticate.class);

            authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Take user to authentication
            startActivity(authIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menuSignOut) {
            FirebaseAuth.getInstance().signOut();
            sendToAuth();
        }

        return true;
    }

    private void sendToAuth() {
        Intent authIntent = new Intent(Chat.this, Authenticate.class);

        // Validation to stop user from going to the authenticate activity again
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Take user to authentication
        startActivity(authIntent);
        finish();
    }
}
