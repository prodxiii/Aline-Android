package coolgroup.com.aline.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import coolgroup.com.aline.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView mName, mStatus, mFriendsTotal;
    private ImageView mImage;
    private Button mSendReq;
    private Button mDeclineReq;


    //**************Firebase******************//
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private FirebaseUser mCurrentUser;

    private int mFriendState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the layout items
        mName = findViewById(R.id.name_profile);
        mStatus = findViewById(R.id.status_profile);
        mFriendsTotal = findViewById(R.id.total_friends_profile);
        mImage = findViewById(R.id.image_profile);

        mSendReq = findViewById(R.id.send_req_btn_profile);
        mDeclineReq = findViewById(R.id.decline_btn_profile);

        mDeclineReq.setVisibility(View.INVISIBLE);
        mDeclineReq.setEnabled(false);

        // Get the User ID for the profile the user clicked on
        String profileUid = getIntent().getStringExtra("userID");

        //****************Firebase********************//
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(profileUid);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //********************************************//

        mFriendState = 0; // Not a friend


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();

                    mName.setText(name);
                    mStatus.setText(status);

                    // Friend List / Request Feature
                    mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(profileUid)) {
                                String req_type = dataSnapshot.child(profileUid).child("request type").getValue().toString();

                                if (req_type.equals("received")) {

                                    mFriendState = 2; // Friend Request Received
                                    mSendReq.setText("Accept Friend Request");

                                    mDeclineReq.setVisibility(View.VISIBLE);
                                    mDeclineReq.setEnabled(true);


                                } else if (req_type.equals("sent")) {

                                    mFriendState = 1; // Friend Request Sent
                                    mSendReq.setText("Cancel Friend Request");

                                    mDeclineReq.setVisibility(View.INVISIBLE);
                                    mDeclineReq.setEnabled(false);

                                }
                            } else {
                                mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(profileUid)) {

                                            mFriendState = 3; // Friends
                                            mSendReq.setText("Unfriend");

                                            mDeclineReq.setVisibility(View.INVISIBLE);
                                            mDeclineReq.setEnabled(false);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDeclineReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFriendState == 1) {
                    mFriendDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
                            .addOnSuccessListener(aVoid -> {

                                mFriendDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
                                        .addOnSuccessListener(aVoid12 -> {

                                            mSendReq.setEnabled(true);
                                            mFriendState = 0; // Not a friend
                                            mSendReq.setText("Send Friend Request");

                                            mDeclineReq.setVisibility(View.INVISIBLE);
                                            mDeclineReq.setEnabled(false);

                                        })
                                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());

                            })
                            .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());
                }
            }
        });



        mSendReq.setOnClickListener(v -> {

            // Disable the Send Request Button from further use
            mSendReq.setEnabled(false);

            // ************************* SENDING A FRIEND REQUEST ***************************** //

            if (mFriendState == 0) {
                mFriendReqDatabase.child(mCurrentUser.getUid())
                        .child(profileUid).child("request type").setValue("sent")
                        .addOnSuccessListener(aVoid -> mFriendReqDatabase.child(profileUid)
                                .child(mCurrentUser.getUid()).child("request type").setValue("received")
                                .addOnSuccessListener(aVoid1 -> {

                                    mFriendState = 1; // Friend Request Sent
                                    mSendReq.setText("Cancel Friend Request");

                                    mDeclineReq.setVisibility(View.INVISIBLE);
                                    mDeclineReq.setEnabled(false);

                                    Toast.makeText(ProfileActivity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();

                                        })
                                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show()));

                mSendReq.setEnabled(true);
            }

            // ************************* CANCELLING A FRIEND REQUEST ***************************** //

            if (mFriendState == 1) {
                mFriendReqDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
                        .addOnSuccessListener(aVoid -> {

                            mFriendReqDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
                                    .addOnSuccessListener(aVoid12 -> {

                                        mSendReq.setEnabled(true);
                                        mFriendState = 0; // Not a friend
                                        mSendReq.setText("Send Friend Request");

                                        mDeclineReq.setVisibility(View.INVISIBLE);
                                        mDeclineReq.setEnabled(false);

                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());

                        })
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());
            }

            // ************************* ACCEPTING A FRIEND REQUEST***************************** //

            if (mFriendState == 2) {

                String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                mFriendDatabase.child(mCurrentUser.getUid()).child(profileUid).setValue(currentDate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mFriendDatabase.child(profileUid).child(mCurrentUser.getUid()).setValue(currentDate)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mFriendReqDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
                                                .addOnSuccessListener(bVoid -> {

                                                    mFriendReqDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
                                                            .addOnSuccessListener(aVoid12 -> {

                                                                mSendReq.setEnabled(true);
                                                                mFriendState = 3; // Friends
                                                                mSendReq.setText("Unfriend");

                                                                mDeclineReq.setVisibility(View.INVISIBLE);
                                                                mDeclineReq.setEnabled(false);

                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());

                                                })
                                                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());



                                    }
                                });
                            }
                        });


            }

            // ************************* UNFRIEND ***************************** //
            if (mFriendState == 3) {
                mFriendDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
                        .addOnSuccessListener(aVoid -> {

                            mFriendDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
                                    .addOnSuccessListener(aVoid12 -> {

                                        mSendReq.setEnabled(true);
                                        mFriendState = 0; // Friend Request Sent
                                        mSendReq.setText("Send Friend Request");

                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());

                        })
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());
            }


        });


    }
}
