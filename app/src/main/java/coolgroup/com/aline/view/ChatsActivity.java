package coolgroup.com.aline.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import coolgroup.com.aline.R;
import coolgroup.com.aline.adapters.SectionsPagerAdapter;
import coolgroup.com.aline.view.options.AccountSettingsActivity;
import coolgroup.com.aline.view.options.AllUsersActivity;

public class ChatsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Done

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DatabaseReference mUserReference;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        mAuth = FirebaseAuth.getInstance();

        // Create the toolbar for the chat activity
        Toolbar mToolbar = (Toolbar) findViewById(R.id.chat_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chats");

        if (mAuth.getCurrentUser() != null) {
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }

        // Create Tabs
        mViewPager = (ViewPager) findViewById(R.id.chat_tab_pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Include tab layout
        mTabLayout = (TabLayout) findViewById(R.id.chat_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // User is not signed in
        if (currentUser == null) {
           backToAuth();
        } else {
            mUserReference.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        // Get the current user ID
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            mUserReference.child("online").setValue(ServerValue.TIMESTAMP);
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

        // Sign out of aline
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            backToAuth();
        }

        // Go to accounts settings activity
        if (item.getItemId() == R.id.menu_account_settings) {
            Intent accountIntent = new Intent(ChatsActivity.this, AccountSettingsActivity.class);
            startActivity(accountIntent);
        }
        // Get a list of all users
        if (item.getItemId() == R.id.menu_all_users) {
            Intent usersIntent = new Intent(ChatsActivity.this, AllUsersActivity.class);
            startActivity(usersIntent);
        }

        return true;
    }

    // Send user to Authentication page
    private void backToAuth() {
        Intent authIntent = new Intent(ChatsActivity.this, AuthenticateActivity.class);

        // Validation to stop user from going to the authenticate activity again
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Take user to authentication
        startActivity(authIntent);
        finish();
    }

}
