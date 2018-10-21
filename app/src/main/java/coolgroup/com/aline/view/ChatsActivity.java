package coolgroup.com.aline.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;



import java.util.Objects;


import coolgroup.com.aline.Controller;

import coolgroup.com.aline.R;
import coolgroup.com.aline.adapters.SectionsPagerAdapter;
import coolgroup.com.aline.view.options.AccountSettingsActivity;
import coolgroup.com.aline.view.options.AllUsersActivity;

public class ChatsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_chats);

        // Create the toolbar for the chat activity
        Toolbar mToolbar = (Toolbar) findViewById(R.id.chat_appbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chats");

        // Create Tabs
        ViewPager mViewPager = (ViewPager) findViewById(R.id.chat_tab_pager);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Include tab layout
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.chat_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // set the bottom navigation bar
        BottomNavigationView mNavBar = (BottomNavigationView) findViewById(R.id.navMainbar);
        mNavBar.setSelectedItemId(R.id.homebar_contacts);
        mNavBar.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.homebar_map:
                            finish();
                            return false;

                        case R.id.homebar_contacts:
                            return true;

                        case R.id.homebar_SOS:
                            return false;
                    }
                    return false;
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Controller.getInstance().getServerCommunicator().isSignedIn())
            Controller.getInstance().getServerCommunicator().setMainUserOnlineNow();
        else
            backToAuth();
    }

    @Override
    protected void onStop() {
        // Get the current user ID
        super.onStop();

        if (Controller.getInstance().getServerCommunicator().isSignedIn())
            Controller.getInstance().getServerCommunicator().setMainUserLastOnlineNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // Sign out of aline
        if (item.getItemId() == R.id.menu_sign_out) {
            Controller.getInstance().getServerCommunicator().signOut();
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
