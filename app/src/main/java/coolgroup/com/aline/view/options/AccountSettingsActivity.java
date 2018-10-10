package coolgroup.com.aline.view.options;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import coolgroup.com.aline.R;
import coolgroup.com.aline.view.update.StatusActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsActivity extends AppCompatActivity {

    //**************FIREBASE******************//
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    //**************FIREBASE******************//

    // Account Settings Layout
    private CircleImageView mAccountImage;
    private TextView mAccountName;
    private TextView mAccountStatus;
    private TextView mAccountPhone;
    private TextView mAccountEmail;

    private Button mChangeImage;
    private Button mUpdateStatus;

    private Toolbar mToolbar;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mAccountImage = (CircleImageView) findViewById(R.id.account_image);
        mAccountName = (TextView) findViewById(R.id.account_name);
        mAccountStatus = (TextView) findViewById(R.id.account_status);
        mAccountEmail = (TextView) findViewById(R.id.account_email);
        mAccountPhone = (TextView) findViewById(R.id.account_phone);

        mChangeImage = (Button) findViewById(R.id.btn_change_image);
        mUpdateStatus = (Button) findViewById(R.id.btn_update_status);

        mToolbar = (Toolbar) findViewById(R.id.profile_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");

        //**************FIREBASE******************//
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true); // Keep offline
        //***************************************//

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String thumbnail = dataSnapshot.child("thumbnail").getValue().toString();

                    mAccountName.setText(name);
                    mAccountStatus.setText(status);
                    mAccountEmail.setText(email);
                    mAccountPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdateStatus.setOnClickListener(v -> {

            String statusValue = mAccountStatus.getText().toString();

            Intent statusIntent = new Intent(AccountSettingsActivity.this, StatusActivity.class);
            statusIntent.putExtra("statusValue", statusValue);

            startActivity(statusIntent);
        });

        // TODO: 29/9/18 Incomplete. Ref: Picasso, CropImage and Part 13
        mChangeImage.setOnClickListener(v -> {

            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

        });
    }
}
