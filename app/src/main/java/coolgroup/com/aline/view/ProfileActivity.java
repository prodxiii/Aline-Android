package coolgroup.com.aline.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
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

    /**
     * Global variables to store the current contact state
     * between Aline users.
     */
    private static int NOT_A_CONTACT = 0;
    private static int IS_A_CONTACT = 1;
    private static int SENT_CONTACT_REQUEST = 2;
    private static int RECEIVED_CONTACT_REQUEST = 3;

    final String DEFAULT_NAME = "Default Name";
    final String DEFAULT_STATUS = "Default Status";
    final String DEFAULT_IMAGE = "default";

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus;
    private Button mProfileSendReqBtn, mDeclineBtn;



    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private int mContactState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        mRootRef = FirebaseDatabase.getInstance().getReference();

        // This user's database reference
        DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        // This is a reference to the contact request database
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");

        // This is a reference to the added contacts database
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");


        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        // Get all the activity_profile resources
        mProfileImage = (ImageView) findViewById(R.id.image_profile);
        mProfileName = (TextView) findViewById(R.id.name_profile);
        mProfileStatus = (TextView) findViewById(R.id.status_profile);
        mProfileSendReqBtn = (Button) findViewById(R.id.send_req_btn_profile);
        mDeclineBtn = (Button) findViewById(R.id.decline_btn_profile);

        // Initial Contact Request
        mContactState = NOT_A_CONTACT;

        // Initially disable decline contact request button
        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);

        /*
          Progress Dialog
         */
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = DEFAULT_NAME;
                String status = DEFAULT_STATUS;
                String image = DEFAULT_IMAGE;

                // Safety checking to prevent null object calls
                Object temp = dataSnapshot.child("name").getValue();
                if (!(temp == null)) {
                    display_name = temp.toString();
                }
                temp = dataSnapshot.child("status").getValue();
                if (!(temp == null)) {
                    status = temp.toString();
                }
                temp = dataSnapshot.child("image").getValue();
                if (!(temp == null)) {
                    image = temp.toString();
                }

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.avatar_male).into(mProfileImage);


                // If this is the current user's profile
                if (mCurrent_user.getUid().equals(user_id)) {

                    mDeclineBtn.setEnabled(false);
                    mDeclineBtn.setVisibility(View.INVISIBLE);

                    mProfileSendReqBtn.setEnabled(false);
                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);

                }


                /*
                  Initially opening another user's profile
                 */
                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)) {

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

                                mContactState = RECEIVED_CONTACT_REQUEST;
                                mProfileSendReqBtn.setText(R.string.accept_request);

                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);


                            } else if (req_type.equals("sent")) {

                                mContactState = SENT_CONTACT_REQUEST;
                                mProfileSendReqBtn.setText(R.string.cancel_request);

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }

                            mProgressDialog.dismiss();


                        } else {

                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)) {

                                        mContactState = IS_A_CONTACT;
                                        mProfileSendReqBtn.setText(R.string.remove_contact);

                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);

                                    }

                                    mProgressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();

                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
          Functionality for Decline Contact Request Button
         */
        mDeclineBtn.setOnClickListener(View -> {
            mDeclineBtn.setEnabled(false);

            if (mContactState == RECEIVED_CONTACT_REQUEST) {
                Map<String, Object> contactsMap = new HashMap<>();

                contactsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                contactsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);


                mRootRef.updateChildren(contactsMap, (databaseError, databaseReference) -> {


                    if (databaseError == null) {

                        mProfileSendReqBtn.setEnabled(true);
                        mContactState = NOT_A_CONTACT;
                        mProfileSendReqBtn.setText(R.string.send_request);

                        mDeclineBtn.setVisibility(android.view.View.INVISIBLE);

                    } else {

                        String error = databaseError.getMessage();

                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                    }

                });


            }


        });

        /*
          Functionality for Send Contact Request Button
          This button actually has three other functions
               1. Send Contact Request
               2. Accept Contact Request
               3. Cancel Contact Request
         */

        mProfileSendReqBtn.setOnClickListener(view -> {

            mProfileSendReqBtn.setEnabled(false);

            // Send an add contact request.
            if (mContactState == NOT_A_CONTACT) {


                DatabaseReference newNotificationref = mRootRef.child("notifications").child(user_id).push();
                String newNotificationId = newNotificationref.getKey();

                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.put("from", mCurrent_user.getUid());
                notificationData.put("type", "request");

                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");
                requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);

                mRootRef.updateChildren(requestMap, (databaseError, databaseReference) -> {

                    if (databaseError != null) {

                        Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                    } else {

                        mContactState = SENT_CONTACT_REQUEST;
                        mProfileSendReqBtn.setText(R.string.send_request);

                    }

                    mProfileSendReqBtn.setEnabled(true);


                });

            }



            // User have sent an add contact request.
            if (mContactState == SENT_CONTACT_REQUEST) {

                mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue()
                        .addOnSuccessListener(aVoid -> mFriendReqDatabase.child(user_id)
                                .child(mCurrent_user.getUid()).removeValue()
                                .addOnSuccessListener(aVoid1 -> {

                    mProfileSendReqBtn.setEnabled(true);
                    mContactState = NOT_A_CONTACT;
                    mProfileSendReqBtn.setText(R.string.send_request);

                    mDeclineBtn.setVisibility(View.INVISIBLE);
                    mDeclineBtn.setEnabled(false);


                })).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "There was some error in cancelling request", Toast.LENGTH_SHORT).show());

            }


            // User have received an add contact request.
            if (mContactState == RECEIVED_CONTACT_REQUEST) {

                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                Map<String, Object> contactsMap = new HashMap<>();
                contactsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
                contactsMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid() + "/date", currentDate);


                contactsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                contactsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);


                mRootRef.updateChildren(contactsMap, (databaseError, databaseReference) -> {


                    if (databaseError == null) {

                        mProfileSendReqBtn.setEnabled(true);
                        mContactState = IS_A_CONTACT;
                        mProfileSendReqBtn.setText(R.string.remove_contact);

                        mDeclineBtn.setVisibility(View.INVISIBLE);
                        mDeclineBtn.setEnabled(false);

                    } else {

                        String error = databaseError.getMessage();

                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                    }

                });

            }


            // Remove added contact from contact list.
            if (mContactState == IS_A_CONTACT) {

                Map<String, Object> removeContactMap = new HashMap<>();
                removeContactMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
                removeContactMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);

                mRootRef.updateChildren(removeContactMap, (databaseError, databaseReference) -> {


                    if (databaseError == null) {

                        mContactState = NOT_A_CONTACT;
                        mProfileSendReqBtn.setText(R.string.send_request);

                        mDeclineBtn.setVisibility(View.INVISIBLE);
                        mDeclineBtn.setEnabled(false);

                    } else {

                        String error = databaseError.getMessage();

                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                    }

                    mProfileSendReqBtn.setEnabled(true);

                });

            }

        });


    }


}
