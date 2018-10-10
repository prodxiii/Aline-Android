package coolgroup.com.aline.view;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import coolgroup.com.aline.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mName, mStatus, mFriendsTotal;
    private Button mSendReq, mDeclineReq;

    //**************Firebase******************//
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootReference;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgressDialog;

    private String mFriendState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        // Get the layout items
        mName = (TextView) findViewById(R.id.name_profile);
        mStatus = (TextView) findViewById(R.id.status_profile);
        mFriendsTotal = (TextView) findViewById(R.id.total_friends_profile);
        mImage = (ImageView) findViewById(R.id.image_profile);

        mSendReq = (Button) findViewById(R.id.send_req_btn_profile);
        mDeclineReq = (Button) findViewById(R.id.decline_btn_profile);

        mDeclineReq.setVisibility(View.INVISIBLE);
        mDeclineReq.setEnabled(false);

        // Get the User ID for the profile the user clicked on
        String profileUid = getIntent().getStringExtra("userID");

        //****************Firebase********************//
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(profileUid);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Contacts");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //********************************************//

        mFriendState = "not friends"; // Not a friend

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();

                    mName.setText(name);
                    mStatus.setText(status);

                    Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.avatar_male).into(mImage);

                    if(mCurrentUser.getUid().equals(user_id)){

                        mDeclineReq.setEnabled(false);
                        mDeclineReq.setVisibility(View.INVISIBLE);

                        mSendReq.setEnabled(false);
                        mSendReq.setVisibility(View.INVISIBLE);

                    }


                    // Friend List / Request Feature
                    mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(profileUid)) {
                                String req_type = dataSnapshot.child(profileUid).child("request type").getValue().toString();

                                if (req_type.equals("received")) {

                                    mFriendState = "request received"; // Friend Request Received
                                    mSendReq.setText("Accept Friend Request");

                                    mDeclineReq.setVisibility(View.VISIBLE);
                                    mDeclineReq.setEnabled(true);


                                } else if (req_type.equals("sent")) {

                                    mFriendState = "request sent"; // Friend Request Sent
                                    mSendReq.setText("Cancel Friend Request");

                                    mDeclineReq.setVisibility(View.INVISIBLE);
                                    mDeclineReq.setEnabled(false);

                                }

                                mProgressDialog.dismiss();

                            } else {
                                mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(profileUid)) {

                                            mFriendState = "friends"; // Contacts
                                            mSendReq.setText("Unfriend");

                                            mDeclineReq.setVisibility(View.INVISIBLE);
                                            mDeclineReq.setEnabled(false);

                                        }

                                        mProgressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        mProgressDialog.dismiss();
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

//        mDeclineReq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFriendState == 1) {
//                    mFriendDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
//                            .addOnSuccessListener(aVoid -> {
//
//                                mFriendDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
//                                        .addOnSuccessListener(aVoid12 -> {
//
//                                            mSendReq.setEnabled(true);
//                                            mFriendState = 0; // Not a friend
//                                            mSendReq.setText("Send Friend Request");
//
//                                            mDeclineReq.setVisibility(View.INVISIBLE);
//                                            mDeclineReq.setEnabled(false);
//
//                                        })
//                                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());
//
//                            })
//                            .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());
//                }
//            }
//        });



        mSendReq.setOnClickListener(v -> {

            // Disable the Send Request Button from further use
            mSendReq.setEnabled(false);

            // ************************* NOT A FRIEND STATE ***************************** //

            if (mFriendState.equals("not friends")) {

                DatabaseReference newNotificationref = mRootReference.child("notifications").child(user_id).push();
                String newNotificationId = newNotificationref.getKey();

                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.put("from", mCurrentUser.getUid());
                notificationData.put("type", "request");

                Map requestMap = new HashMap();
                requestMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id + "/request_type", "sent");
                requestMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid() + "/request_type", "received");
                requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);

                mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if(databaseError != null){

                            Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                        } else {

                            mFriendState = "req_sent";
                            mSendReq.setText("Cancel Friend Request");

                        }

                        mSendReq.setEnabled(true);


                    }
                });

            }

            // ************************* CANCELLING A FRIEND REQUEST ***************************** //

            if (mFriendState.equals("request sent")) {
                mFriendReqDatabase.child(mCurrentUser.getUid()).child(profileUid).removeValue()
                        .addOnSuccessListener(aVoid -> {

                            mFriendReqDatabase.child(profileUid).child(mCurrentUser.getUid()).removeValue()
                                    .addOnSuccessListener(aVoid12 -> {

                                        mSendReq.setEnabled(true);
                                        mFriendState = "not friends"; // Not a friend
                                        mSendReq.setText("Send Friend Request");

                                        mDeclineReq.setVisibility(View.INVISIBLE);
                                        mDeclineReq.setEnabled(false);

                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Cancel Friend Request", Toast.LENGTH_SHORT).show());

                        })
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Unable to Send Request", Toast.LENGTH_SHORT).show());
            }

            // ************************* ACCEPTING A FRIEND REQUEST***************************** //

            if (mFriendState.equals("request received")) {

                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                Map friendsMap = new HashMap();
                friendsMap.put("Contacts/" + mCurrentUser.getUid() + "/" + user_id + "/date", currentDate);
                friendsMap.put("Contacts/" + user_id + "/"  + mCurrentUser.getUid() + "/date", currentDate);


                friendsMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id, null);
                friendsMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid(), null);


                mRootReference.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                        if(databaseError == null){

                            mSendReq.setEnabled(true);
                            mFriendState = "friends";
                            mSendReq.setText("Unfriend this Person");

                            mDeclineReq.setVisibility(View.INVISIBLE);
                            mDeclineReq.setEnabled(false);

                        } else {

                            String error = databaseError.getMessage();

                            Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                        }

                    }
                });

            }

            // ************************* UNFRIEND ***************************** //
            if (mFriendState.equals("friends")) {

                Map unfriendMap = new HashMap();
                unfriendMap.put("Contacts/" + mCurrentUser.getUid() + "/" + user_id, null);
                unfriendMap.put("Contacts/" + user_id + "/" + mCurrentUser.getUid(), null);

                mRootReference.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                        if(databaseError == null){

                            mFriendState = "not_friends";
                            mSendReq.setText("Send Friend Request");

                            mDeclineReq.setVisibility(View.INVISIBLE);
                            mDeclineReq.setEnabled(false);

                        } else {

                            String error = databaseError.getMessage();

                            Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                        }

                        mSendReq.setEnabled(true);

                    }
                });

            }


        });


    }
}
