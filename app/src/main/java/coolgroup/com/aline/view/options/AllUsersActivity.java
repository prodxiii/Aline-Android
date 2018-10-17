package coolgroup.com.aline.view.options;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import coolgroup.com.aline.R;
import coolgroup.com.aline.model.Users;
import coolgroup.com.aline.view.ProfileActivity;
import coolgroup.com.aline.viewmodels.UsersViewHolder;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Abstract our database
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        // Build a base query
        mQuery = mUsersDatabase.orderByKey();

        mUsersList = (RecyclerView) findViewById(R.id.users_recycler_view);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        // Get the intent, verify if there was a search string and get the query, if not do nothing
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Change the query based upon the string
            mQuery = mUsersDatabase.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.layout_single_user,
                UsersViewHolder.class,
                mQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position) {

                usersViewHolder.setName(users.getName());
                String status = users.getStatus();
                if (status.length() > 30) {
                    usersViewHolder.setStatus(status.substring(0, 30) + "...");
                } else {
                    usersViewHolder.setStatus(status);
                }

                usersViewHolder.setImage(users.getThumbnail(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(AllUsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }
}
