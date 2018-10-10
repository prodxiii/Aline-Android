package coolgroup.com.aline.view.options;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import coolgroup.com.aline.R;
import coolgroup.com.aline.fragments.chat.Chats;
import coolgroup.com.aline.model.Users;
import coolgroup.com.aline.view.ProfileActivity;
import coolgroup.com.aline.viewmodels.UsersViewHolder;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mUsersList = (RecyclerView) findViewById(R.id.users_recycler_view);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
//            @NonNull
//            @Override
//            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.layout_single_user, viewGroup, false);
//
//                return new UsersViewHolder(view);
//            }

            /**
             * @param usersViewHolder
             * @param position
             * @param users    the model object containing the data that should be used to populate the view.
             * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
             */
//            @Override
//            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position, @NonNull Users users) {
//                // Set the name and status of the UsersViewHolder
//                usersViewHolder.setName(users.getName());
//                usersViewHolder.setStatus(users.getStatus());
//
//                String userID = getRef(position).getKey();
//
//                // Set up OnClickListener to open Profile activity
//                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent profileIntent = new Intent(AllUsersActivity.this, ProfileActivity.class);
//                        profileIntent.putExtra("userID", userID);
//                        startActivity(profileIntent);
//
//                    }
//                });
//            }

        };

//        mUsersList.setAdapter(firebaseRecyclerAdapter);
//        firebaseRecyclerAdapter.startListening();
//    }
}
