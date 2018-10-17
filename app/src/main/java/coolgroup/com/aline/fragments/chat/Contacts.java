package coolgroup.com.aline.fragments.chat;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.R;
import coolgroup.com.aline.model.Contact;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.model.iVOIPCommunicator;
import coolgroup.com.aline.view.ChatActivity;
import coolgroup.com.aline.view.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public Contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_contacts, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<Contact, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Contact, FriendsViewHolder>(

                Contact.class,
                R.layout.layout_single_user,
                FriendsViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, Contact friends, int i) {

                friendsViewHolder.setDate(friends.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        final String userLatitude = dataSnapshot.child("latitude").getValue().toString();
                        final String userLongitude = dataSnapshot.child("longitude").getValue().toString();

                        String userThumb = dataSnapshot.child("thumbnail").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setUserImage(userThumb, getContext());
                        friendsViewHolder.setLocation(userLatitude+ " , " +userLongitude);

                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String callstring;
                                iVOIPCommunicator voipCommunicator = Controller.getInstance().getiVOIPCommunicator();

                                User userInCallWith = voipCommunicator.getUserInCallWith();

                                if (userInCallWith == null) {
                                    callstring = "Call " + userName + "!";
                                }
                                // If this user is the one we're currently in a call with,
                                // then this button will hang us up.
                                else if (list_user_id.equals(userInCallWith.getuID())) {
                                    callstring = "Hang up call with " + userName;
                                }
                                // Otherwise, it'll start a call.
                                else {
                                    callstring = "Call " + userName + "!";
                                }

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message", callstring};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if (i == 0) {

                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);

                                        }

                                        if (i == 1) {

                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);

                                        }

                                        if (i == 2) {
                                            // This happens if the user tries to start a call.

                                            // Create user (this should have happened earlier but
                                            // here we are.)
                                            User otherUser = new User(null, null, userName, null, list_user_id);

                                            if (userInCallWith == null) {
                                                voipCommunicator.startCall(otherUser);
                                            }
                                            // If we're in a call with this person, hang it up.
                                            else if (otherUser.getuID().equals(userInCallWith.getuID())) {
                                                voipCommunicator.hangUpCall();
                                            }
                                            // Otherwise, we want to start the call! Hooray!
                                            else {
                                                voipCommunicator.startCall(otherUser);
                                            }
                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.single_user_status);
            userStatusView.setText(date);

        }

        public void setLocation(String location) {

            TextView userLocationView = (TextView) mView.findViewById(R.id.single_user_location);
            userLocationView.setText(location);

        }

        public void setName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.single_user_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.avatar_male).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.single_user_online_icon);

            if (online_status.equals("true")) {

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }


}
